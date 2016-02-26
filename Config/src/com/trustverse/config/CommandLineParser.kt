/**
 * Created on 25.02.2016.
 * @author Goussein Minkailov
 */

package com.trustverse.config

import java.util.*
import com.trustverse.utils.toType

/**
 * Parses command line arguments
 * @param args Collection of command line arguments
 * @param params List of ParamInfo values, describing command line params that can be parsed, their types and values
 * @param isParam Lambda used to determine if command line argument is a parameter
 * @property UnnamedParams Collection of command line arguments that are not either parameters or parameter values
 */
class CommandLineParser(private val args: Array<String>,
                        params: List<ParamInfo>,
                        private var isParam: (str: String) -> Boolean = { x -> x.startsWith("-")}) {
    val UnnamedParams = ArrayList<String>()
    private val Params = HashMap<String, ParamInfo>()

    init {
        Params.putAll(params.map { x -> Pair(x.Name, x) })
        process()
    }

    /**
     * Implementation of [] operator. Returns ParamInfo value for specified parameter name
     * @param param Parameter name
     * @return ParamInfo value if specified parameter was parsed or null otherwise.
     */
    operator fun get(param: String): ParamInfo? {
        val paramInfo = Params[param]
        return if (paramInfo != null && paramInfo.Presents) paramInfo else null
    }

    /**
     * Parses command line arguments and fills in Params and UnnamedParams collections
     * @throws IllegalArgumentException
     */
    private fun process() {
        var i = 0
        while (i < args.size) {
            try {
                var param = args[i]
                var value = if (i + 1 < args.size) args[i + 1] else ""

                if (isParam(param)) {
                    val paramInfo = Params[param]
                    if (paramInfo != null) {
                        paramInfo.Presents = true

                        if (paramInfo.WithValue) {
                            try {
                                if (isParam(value)) {
                                    value = ""
                                    throw Exception()
                                } else {
                                    paramInfo.valueHandler?.invoke(value.toType(paramInfo.Type))
                                    i++
                                }
                            } catch (e: Exception) {
                                throw IllegalArgumentException("Invalid \"$param\" param value \"$value\". Value must be of ${paramInfo.Type} type.")
                            }
                        } else {
                            paramInfo.valueHandler?.invoke(Any())
                        }
                    } else {
                        throw IllegalArgumentException("Unknown parameter \"$param\" specified.")
                    }
                } else {
                    UnnamedParams.add(param)
                }

                i++
            }
            catch (e: Exception) {
                throw e
            }
        }
    }
}
