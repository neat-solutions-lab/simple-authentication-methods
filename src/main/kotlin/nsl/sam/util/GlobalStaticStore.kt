package nsl.sam.util

import java.util.concurrent.ConcurrentHashMap

object GlobalStaticStore {

    val objectsStore = ConcurrentHashMap<String, Any>()

    fun put(key: String, obj: Any): Any? {
        return objectsStore.put(key, obj)
    }

    fun get(key: String): Any? {
        return objectsStore[key]
    }

}