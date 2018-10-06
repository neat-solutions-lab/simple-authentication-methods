package nsl.sam.annotation.inject

import nsl.sam.annotation.AnnotationMetadataResolver
import org.springframework.core.env.Environment
import org.springframework.util.Assert
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object FactoryRetriever {

    private val createdFactories: MutableMap<KClass<*>, Factory<out Any>> = mutableMapOf()

    fun <T:Any> getFactory(
            factoryType: KClass<out Factory<T>>,
            attributeName: String,
            annotationMetadataResolver: AnnotationMetadataResolver,
            environment: Environment,
            defaultFactoryPropertyName: String,
            defaultFactory: KClass<out Factory<T>>? = null): Factory<T> {

        /*
         * try to obtain factory basing on explicitly defined, annotation attributes
         */
        val factoryClasses = annotationMetadataResolver?.getAttributeValueAsArray(
                attributeName, factoryType
        )

        Assert.isTrue(factoryClasses?.size == 0 || factoryClasses?.size == 1,
                "There can be only one ${factoryType::class.qualifiedName} provided" +
                        "by $attributeName attribute.")

        if(null != factoryClasses && factoryClasses.isNotEmpty()) {
            val factoryClass = factoryClasses[0]
            return getCachedOrCreate(factoryClass)
        }

        /*
         * 2. Return default factory
         */
        val factoryClassName = when {
            defaultFactory != null -> environment.getProperty(
                    defaultFactoryPropertyName, defaultFactory::class.qualifiedName!!
            )
            else -> environment.getProperty(defaultFactoryPropertyName)
        }

        val factoryClass = Class.forName(factoryClassName).kotlin as KClass<out Factory<T>>

        return getCachedOrCreate(factoryClass)
    }

    private fun <T> getCachedOrCreate(factoryClass: KClass<out Factory<T>>): Factory<T> {

        val factory  = createdFactories.getOrPut(factoryClass) {
            SingletonObjectFactoryWrapper(factoryClass.createInstance()) as Factory<out Any>
        }
        return factory as Factory<T>
    }
}
