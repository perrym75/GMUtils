/**
 * Created on 25.02.2016.
 * @author Goussein Minkailov
 */

package com.trustverse.config

/**
 * Describes command line parameter
 * @param Name Mame of command line parameter
 * @param WithValue true if parameter has a value
 * @param Type Type of command line parameter
 * @param valueHandler Lambda used to handle parsed parameter value
 */
class ParamInfo(val Name: String, val WithValue: Boolean = false, val Type: Class<*> = Any::class.java,
                internal val valueHandler: ((value: Any) -> Unit)? = null) {
    var Presents: Boolean = false
    internal set

    init {
        if (WithValue && valueHandler == null) {
            throw IllegalArgumentException("Parameter with value must have type.")
        }
    }
}