package nsl.sam.spring.config

import nsl.sam.annotation.AnnotationProcessor
import nsl.sam.logger.logger
import nsl.sam.spring.annotation.*
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.cglib.proxy.Enhancer
import org.springframework.cglib.proxy.MethodInterceptor
import org.springframework.cglib.proxy.MethodProxy
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import kotlin.reflect.full.cast

class ForwardingInterceptor: MethodInterceptor {
    override fun intercept(obj: Any, method: Method, args: Array<out Any>, proxy: MethodProxy): Any {
        return proxy.invokeSuper(obj, args)
    }

}



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

        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")
        println("!!!!!!!!!!!!!!! import called for ${importingClassMetadata.className}")

        val annotationAttributes = getAnnotationAttributes(importingClassMetadata)


        val bd = BeanDefinitionBuilder.genericBeanDefinition(DynamicWebSecurityConfigurer::class.java).beanDefinition
        bd.propertyValues.add("enableAnnotationAttributes", annotationAttributes)
        registry.registerBeanDefinition(
                DynamicWebSecurityConfigurer::class.qualifiedName!!+annotationAttributes.order.toString(), bd
        )


//        val enhancer = Enhancer()
//        enhancer.setSuperclass(DynamicWebSecurityConfigurer::class.java)
//        enhancer.setCallbackType(ForwardingInterceptor::class.java)
//        val enhancedClass = enhancer.createClass()
//
//
//        val bd = BeanDefinitionBuilder.genericBeanDefinition(enhancedClass).beanDefinition
//        bd.propertyValues.add("enableAnnotationAttributes", annotationAttributes)
//        registry.registerBeanDefinition(
//                enhancedClass.canonicalName, bd
//        )

        println(">>>>>>>>>> annotationAttributes: $annotationAttributes")
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
            annotationMetadata {
                importingClassMetadata
            }
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
