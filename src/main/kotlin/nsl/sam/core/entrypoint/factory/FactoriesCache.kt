package nsl.sam.core.entrypoint.factory

import kotlin.reflect.KClass

@Suppress("UNUSED_PARAMETER")
class FactoriesCache<T:Any>(type: KClass<T>) {

    private val cache: MutableMap<KClass<out T>, T> = mutableMapOf()

    fun getOrPut(key:KClass<out T>, supplier: () -> T):T {
        return cache.getOrPut(key) {
            supplier()
        }
    }

    fun get(key: KClass<out T>): T {
        return cache[key]!!
    }
}
