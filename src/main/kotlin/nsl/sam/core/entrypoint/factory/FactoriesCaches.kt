package nsl.sam.core.entrypoint.factory

import kotlin.reflect.KClass

object FactoriesCaches {

    private val caches: MutableMap<KClass<*>, FactoriesCache<out Any>> = mutableMapOf()

    fun <T:Any> getFactoriesRegistry(type: KClass<T>): FactoriesCache<T> {
        val factoriesCache = caches.getOrPut(type) {
            FactoriesCache(type)
        }

        return factoriesCache as FactoriesCache<T>
    }

}