package nsl.sam.spring.config

import nsl.sam.spring.annotation.*
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata

class DynamicImportBeanDefinitionRegistrar: ImportBeanDefinitionRegistrar {


    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {

        val annotationAttributes: AnnotationAttributes = getAnnotationAttributes(importingClassMetadata)

        println(">>>>>>>>>> annotationAttributes: $annotationAttributes")

    }


    private fun getAnnotationAttributes(importingClassMetadata: AnnotationMetadata): AnnotationAttributes {

        return AnnotationAttributes.create {
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


    /**
     * Represents attributes of [@EnableSimpleAuthentication] methods annotation
     */
    data class AnnotationAttributes private constructor (
            val methods: Array<AuthenticationMethod>,
            val match: String,
            val debug: Boolean,
            val order: Int,
            val anonymousFallback: Boolean,
            val authentications: String,
            val deactivateNotConfigured: Boolean
    ) {

        companion object {
            fun create(init: Builder.() -> Unit) = Builder(init).build()
        }

        class Builder private constructor(){

            constructor(init: Builder.()->Unit):this() {
                init()
            }

            var methods: Array<AuthenticationMethod> = emptyArray()

            var match = ""
            var debug = false
            var order = 0
            var anonymousFallback = false
            var authentications = ""
            var deactivateNotConfigured = false

            fun methods(methods: () -> Array<AuthenticationMethod>) = apply { this.methods = methods()}

            fun match(match: () -> String) = apply { this.match = match() }

            fun debug(debug: () -> Boolean) = apply { this.debug = debug() }

            fun order(order: () -> Int) = apply { this.order = order() }

            fun anonymousFallback(anonymousFallback: () -> Boolean) = apply { this.anonymousFallback = anonymousFallback() }

            fun authentications(authentications: () -> String) = apply { this.authentications = authentications() }

            fun deactivateNotConfigured(deactivateNotConfigured: () -> Boolean) = apply { this.deactivateNotConfigured = deactivateNotConfigured() }

            fun build() = AnnotationAttributes(
                    this.methods, this.match, this.debug, this.order, this.anonymousFallback,
                    this.authentications, this.deactivateNotConfigured
            )
        }

    }


}
