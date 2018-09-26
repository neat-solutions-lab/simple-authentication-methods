package nsl.sam.util

abstract class AbstractSharingBox<T:Any> private constructor() {

    private lateinit var wrappedObj: T
    private lateinit var key: String

    constructor(key: String):this() {
        this.key = key
        initialize(key)
    }

    @Synchronized
    private fun initialize(key: String, forceNew: Boolean = false) {
        var obj: T? = GlobalStaticStore.get(key) as T?
        if(obj == null || forceNew) {
            obj = supply()
            GlobalStaticStore.put(key, obj!!)
        }
        this.wrappedObj = obj
    }

    protected abstract fun supply(): T

    fun getObj(forceNew: Boolean = false): T {
        if (forceNew) initialize(this.key, forceNew)
        return wrappedObj
    }
}