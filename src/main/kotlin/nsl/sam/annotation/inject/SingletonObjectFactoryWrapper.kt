package nsl.sam.annotation.inject

class SingletonObjectFactoryWrapper<T>(private val wrappedFactory: Factory<T>) : Factory<T> {

    private var createdInstance: T? = null

    @Synchronized
    override fun create(): T {
        if (null == createdInstance) {
            createdInstance = wrappedFactory.create()
        }
        return createdInstance!!
    }
}
