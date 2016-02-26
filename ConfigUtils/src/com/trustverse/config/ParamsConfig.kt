/**
 * Created 24.02.2016.
 * @author Goussein Minkailov
 */

package com.trustverse.config

import java.lang.reflect.Modifier
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.jvm.javaField

class ParamsConfig(private val args: Array<String>) {
    var Debug: Boolean = false
    var Log: String = ""
    var Trace: Boolean = false
    var Count: Int = 0
    val UnnamedParams: List<String>

    init {
        val parser = CommandLineParser(args, listOf(
                ParamInfo("-debug", true, Debug.javaClass, { Debug = it as Boolean }),
                ParamInfo("-trace", false, Trace.javaClass, { Trace = true }),
                ParamInfo("-log", true, Log.javaClass, { Log = it as String }),
                ParamInfo("-count", true, Count.javaClass, { Count = it as Int })),
                { x -> x.startsWith("-") })

        UnnamedParams = parser.UnnamedParams
    }


    fun print() {
        val cl = ParamsConfig::class
        for (field in cl.declaredMemberProperties.filter {x -> x.javaField?.modifiers == Modifier.PRIVATE}) {
            println("${field.name} = ${field.get(this)}")
        }

        for (value in UnnamedParams) {
            println("Unnamed value = $value")
        }
    }
}