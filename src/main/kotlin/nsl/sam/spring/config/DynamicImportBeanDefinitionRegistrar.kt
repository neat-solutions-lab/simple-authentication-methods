package nsl.sam.spring.config

import nsl.sam.logger.logger
import nsl.sam.spring.annotation.*
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import java.lang.IllegalArgumentException
import kotlin.reflect.full.cast

class DynamicImportBeanDefinitionRegistrar: ImportBeanDefinitionRegistrar, BeanFactoryAware {

    companion object {
        val log by logger()
        //val cachingMetadataReaderFactory = CachingMetadataReaderFactory()
    }


    lateinit var listableBeanFactory: ListableBeanFactory

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.listableBeanFactory = ListableBeanFactory::class.cast(beanFactory)
    }


    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {

        val annotationAttributes = getAnnotationAttributes(importingClassMetadata)

        val bd = BeanDefinitionBuilder.genericBeanDefinition(DynamicWebSecurityConfigurer::class.java).beanDefinition
        bd.propertyValues.add("enableAnnotationAttributes", annotationAttributes)
        registry.registerBeanDefinition(DynamicWebSecurityConfigurer::class.qualifiedName!!, bd)



        val enabledMethods = getEnabledMethods(importingClassMetadata)
        // TODO: after refactoring remove the filter before forEach
        //enabledMethods.filter{ it != AuthenticationMethod.SIMPLE_BASIC_AUTH }.forEach {
        //    val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(
        //            getMethodConfigurationClass(it)
        //    ).beanDefinition
        //    registry.registerBeanDefinition(getMethodConfigurationClass(it).name, beanDefinition)
        //} // forEach()


        // TODO: refactoring: check enabledMethods array, and not beans registry
//        if(!registry.containsBeanDefinition(getMethodConfigurationClass(AuthenticationMethod.SIMPLE_BASIC_AUTH).name)) {
//            val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DisableBasicAuthConfig::class.java).beanDefinition
//            registry.registerBeanDefinition(DisableBasicAuthConfig::class.java.name, beanDefinition)
//        }
        if(!enabledMethods.contains(AuthenticationMethod.SIMPLE_BASIC_AUTH)) {
            val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DisableBasicAuthConfig::class.java).beanDefinition
            registry.registerBeanDefinition(DisableBasicAuthConfig::class.java.name, beanDefinition)
        }


        println(">>>>>>>>>> annotationAttributes: $annotationAttributes")

    }

    private fun getMethodConfigurationClass(method: AuthenticationMethod): Class<*> =  when(method) {
        AuthenticationMethod.SIMPLE_TOKEN -> TokenAuthConfiguration::class.java
        AuthenticationMethod.SIMPLE_BASIC_AUTH -> BasicAuthConfiguration::class.java
        else -> throw IllegalArgumentException("Illegal AuthenticationMethod used: $method")
    }

    private fun matchesSimpleNoMethod(method: AuthenticationMethod): Boolean {
        return method == AuthenticationMethod.SIMPLE_NO_METHOD
    }

    private fun getEnabledMethods(importingClassMetadata: AnnotationMetadata):  Array<AuthenticationMethod> {

        val annotationAttributes: AnnotationAttributes =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes(
                                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
                ) ?: return arrayOf()

        val enabledMethods: Array<AuthenticationMethod> = annotationAttributes.run {
            Array<AuthenticationMethod>::class.cast(get("methods"))
        }

        /*
         * if there is [AuthenticationMethod.SIMPLE_NO_METHOD] in attributes then return empty
         * array
         */
        enabledMethods
                .firstOrNull { matchesSimpleNoMethod(it) }
                ?.let { return arrayOf() }

        return Array<AuthenticationMethod>::class.cast(annotationAttributes["methods"])
    }




    private fun getAnnotationAttributes(importingClassMetadata: AnnotationMetadata): EnableAnnotationAttributes {

        return EnableAnnotationAttributes.create {
            methods {
                AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_METHODS_ATTRIBUTE_NAME,
                        Array<AuthenticationMethod>::class
                )
            }
            match {
                AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_MATCH_ATTRIBUTE_NAME,
                        String::class
                )
            }
            debug {
                AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_DEBUG_ATTRIBUTE_NAME,
                        Boolean::class
                )
            }
            order {
                AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_ORDER_ATTRIBUTE_NAME,
                        Int::class
                )
            }
            anonymousFallback {
                AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_ANONYMOUS_FALLBACK_ATTRIBUTE_NAME,
                        Boolean::class
                )
            }
            deactivateNotConfigured {
                AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_DEACTIVATE_ATTRIBUTE_NAME,
                        Boolean::class
                )
            }
            authentications {
                AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_AUTHORIZATIONS_ATTRIBUTE_NAME,
                        String::class
                )
            }
        }

    }


}
