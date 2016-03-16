package com.trustverse.parser

/**
 *
 */
interface Evaluable<out T> {
    val value: T
        get
}
interface Operator<out T> : Evaluable<T> {
}

interface UnaryOperator<T>: Operator<T> {
    val operand: Evaluable<T>
        get
}

interface BinaryOperator<T>: Operator<T> {
    val operand1: Evaluable<T>
        get
    val operand2: Evaluable<T>
        get
}

class UnaryMinusOperator(override val operand: Evaluable<Double>) : UnaryOperator<Double> {
    override val value: Double
        get() = -operand.value
}

class SubOperator(override val operand1: Evaluable<Double>, override val operand2: Evaluable<Double>) : BinaryOperator<Double> {
    override val value: Double
        get() = operand1.value - operand2.value
}

class AddOperator(override val operand1: Evaluable<Double>, override val operand2: Evaluable<Double>) : BinaryOperator<Double> {
    override val value: Double
        get() = operand1.value + operand2.value
}

class MulOperator(override val operand1: Evaluable<Double>, override val operand2: Evaluable<Double>) : BinaryOperator<Double> {
    override val value: Double
        get() = operand1.value * operand2.value
}

class DivOperator(override val operand1: Evaluable<Double>, override val operand2: Evaluable<Double>) : BinaryOperator<Double> {
    override val value: Double
        get() = operand1.value / operand2.value
}

class DoubleValue(private val strVal: String) : Evaluable<Double> {
    override val value: Double
        get() = strVal.toDouble()
}