package nsl.sam.core.entrypoint.factory

interface Factory<T> {

    fun create(): T

}