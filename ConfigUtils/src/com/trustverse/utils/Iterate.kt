package com.trustverse.utils

/**
 *
 */

private class UniversalIterator<T>(private val closure: () -> T?) : Iterator<T> {
    private var value: T? = null
    override fun hasNext(): Boolean {
        value = closure()
        return value != null
    }

    override fun next(): T {
        val ret = value
        value = null
        return ret!!
    }
}

private class UniversalIterable<T>(closure: () -> T?) : Iterable<T> {
    private val iter = UniversalIterator(closure)

    override fun iterator(): Iterator<T> {
        return iter
    }
}

fun <T> iterate(closure: () -> T?): Iterable<T> {
    return UniversalIterable(closure)
}