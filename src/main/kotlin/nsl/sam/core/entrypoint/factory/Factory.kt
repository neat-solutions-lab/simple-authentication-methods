package nsl.sam.core.entrypoint.factory

interface Factory<out T> {

    fun create(): T

}