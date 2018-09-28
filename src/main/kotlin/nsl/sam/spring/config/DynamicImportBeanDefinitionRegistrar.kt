package nsl.sam.spring.config

import nsl.sam.annotation.AnnotationProcessor
import nsl.sam.configurer.ConfigurersFactories
import nsl.sam.instrumentation.InstrumentedClassProvider
import nsl.sam.logger.logger
import nsl.sam.spring.annotation.*
import nsl.sam.spring.config.ordering.OrderingHelper
import nsl.sam.spring.config.ordering.ReservedNumbersFinder
import nsl.sam.spring.config.sequencer.SimpleVolatileSequencer
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.AuthenticationEntryPoint
import kotlin.reflect.full.cast

class DynamicImportBeanDefinitionRegistrar: ImportBeanDefinitionRegistrar, BeanFactoryAware {

    companion object {
        val log by logger()
    }

    lateinit var listableBeanFactory: ListableBeanFactory

    private val orderingHelper = OrderingHelper.getSingleton()

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.listableBeanFactory = ListableBeanFactory::class.cast(beanFactory)
    }

    @Synchronized
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {

        /*
         * due to auto-ordering mechanism, I need to know all reserved order number,
         * so that they will not be used by auto-ordering
         */
        if(!orderingHelper.isAlreadyInitializedWithRestrictedList) {
            log.debug("Looking for and remembering all order numbers used explicitly with @EnableSimpleAuthenticationMethods annotation.")
            orderingHelper.initializeWithRestrictedList(findReservedOrderNumbers())
        }

        val annotationAttributes = getAnnotationAttributes(importingClassMetadata)
        log.debug("annotation attributes for ${importingClassMetadata.className}: $annotationAttributes")

        /*
         * generating brand new class which extends WebSecurityConfigurerAdapter
         */
        val dynamicConfigurerClass = InstrumentedClassProvider.getRenamedClass(
                InstrumentedWebSecurityConfigurerTemplate::class.java,
                InstrumentedWebSecurityConfigurerTemplate::class.java.canonicalName +
                        SimpleVolatileSequencer.getSingleton().getNextValue()
        )

        /*
         * bean definition with supplier to provide instance of the above generated class which
         * extends WebSecurityConfigurerAdapter
         */
        val bd = BeanDefinitionBuilder.genericBeanDefinition(dynamicConfigurerClass as Class<WebSecurityConfigurerAdapter>){
            /*
             * supplier logic
             */
            val configurersFactories = listableBeanFactory.getBean(ConfigurersFactories::class.java)
            var simpleAuthenticationEntryPoint = listableBeanFactory.getBean(AuthenticationEntryPoint::class.java)
            val constructor = dynamicConfigurerClass.getConstructor(ConfigurersFactories::class.java, AuthenticationEntryPoint::class.java)
            constructor.newInstance(configurersFactories, simpleAuthenticationEntryPoint)
        }.beanDefinition

        /*
         * ensure the anntation properties will be injected to the bean defined by the above definition
         */
        bd.propertyValues.add("enableAnnotationAttributes", annotationAttributes)

        /*
         * register the bean definition with the bean definition registry
         */
        registry.registerBeanDefinition(
                dynamicConfigurerClass.canonicalName, bd
        )
    }

    private fun findReservedOrderNumbers(): List<Int> {
        return ReservedNumbersFinder(listableBeanFactory).findReservedNumbers()
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
                val value = AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_ORDER_ATTRIBUTE_NAME,
                        Int::class
                )
                if(value == -1) {
                    OrderingHelper.getSingleton().getNextNumber()
                } else {
                    value
                }
            }
            anonymousFallback {
                AnnotationProcessor.getAnnotationAttributeValue(
                        importingClassMetadata,
                        EnableSimpleAuthenticationMethods::class,
                        ENABLE_ANNOTATION_ANONYMOUS_FALLBACK_ATTRIBUTE_NAME,
                        Boolean::class
                )
            }
            //deactivateNotConfigured {
            //    AnnotationProcessor.getAnnotationAttributeValue(
            //            importingClassMetadata,
            //            EnableSimpleAuthenticationMethods::class,
            //            ENABLE_ANNOTATION_DEACTIVATE_ATTRIBUTE_NAME,
            //            Boolean::class
            //    )
            //}
            authorizations {
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
