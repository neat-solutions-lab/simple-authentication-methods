package nsl.sam.util

abstract class AbstractNamedSingleton<T:Any> private constructor() {

    private var wrappedObj: T? = null
    private lateinit var name: String

    protected abstract fun supply(): T

    constructor(name: String):this() {
        this.name = name
        initialize(name)
    }

    @Synchronized
    private fun initialize(name: String, forceNew: Boolean = false) {
        var obj: T? = GlobalStaticStore.get(name) as T?
        if(obj == null || forceNew) {
            obj = supply()
            GlobalStaticStore.put(name, obj!!)
        }
        this.wrappedObj = obj
    }

    @Synchronized
    fun getObj(forceNew: Boolean = false): T {
        if (forceNew) initialize(this.name, forceNew)
        return wrappedObj!!
    }

    @Synchronized
    fun purge() {
        GlobalStaticStore.remove(this.name)
        this.wrappedObj = null
    }
}