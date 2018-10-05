package nsl.sam.core.entrypoint.factory

class SingletonObjectFactoryWrapper<T>(private val wrappedFactory: Factory<T>): Factory<T> {

    @Volatile
    private var createdInstance: T? = null

    @Synchronized
    override fun create(): T {
        if (null == createdInstance) {
            createdInstance = wrappedFactory.create()
        }
        return createdInstance!!
    }
}