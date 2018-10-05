package nsl.sam.annotation.inject

interface Factory<out T> {
    fun create(): T
}