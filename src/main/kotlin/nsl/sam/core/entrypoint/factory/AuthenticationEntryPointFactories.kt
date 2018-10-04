package nsl.sam.core.entrypoint.factory

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.logger.logger
import org.springframework.core.env.Environment
import org.springframework.util.Assert
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object AuthenticationEntryPointFactories {

    val log by logger()

    private const val AUTHENTICATION_ENTRY_POINT_FACTORY = "nsl.sam.authentication-entry-point.factory"

    private val cachedFactories = mutableMapOf<KClass<AuthenticationEntryPointFactory>, AuthenticationEntryPointFactory>()

    @Synchronized
    private fun getCachedOrCreate(clazz: KClass<AuthenticationEntryPointFactory>): AuthenticationEntryPointFactory {
        return cachedFactories.getOrPut(clazz) {
            SingletoneFactoryWrapper(clazz.createInstance())
        }
    }

    fun getFactory(annotationMetadataResolver: AnnotationMetadataResolver, environment: Environment): AuthenticationEntryPointFactory {

        /*
         * 1. Check if factory class is declared on annotation level
         */
        val factoryClasses = annotationMetadataResolver.getAttributeValueAsArray(
                "authenticationEntryPointFactory", AuthenticationEntryPointFactory::class
        )

        Assert.isTrue(factoryClasses?.size == 0 || factoryClasses?.size == 1,
                "There can be only one ${AuthenticationEntryPointFactory::class.qualifiedName} provided" +
                        "by authenticationEntryPointFactory attribute.")

        if(null != factoryClasses &&factoryClasses.isNotEmpty()) {
            val factoryClass = factoryClasses[0]
            return getCachedOrCreate(factoryClass)
        }

        /*
         * 2. Return default factory
         */
        val factoryClassName = environment.getProperty(
                AUTHENTICATION_ENTRY_POINT_FACTORY,
                DefaultAuthenticationEntryPointFactory::class.qualifiedName!!
        )
        val factoryClass = Class.forName(factoryClassName).kotlin as KClass<AuthenticationEntryPointFactory>
        return getCachedOrCreate(factoryClass)
    }
}
