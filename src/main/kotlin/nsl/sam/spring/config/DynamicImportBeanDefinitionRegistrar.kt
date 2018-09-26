package nsl.sam.spring.config

import nsl.sam.annotation.AnnotationProcessor
import nsl.sam.configurer.ConfigurersFactories
import nsl.sam.dynamic.RenamedClassProvider
import nsl.sam.logger.logger
import nsl.sam.sequencer.ClassNameSuffixSequencer
import nsl.sam.spring.annotation.*
import nsl.sam.spring.config.ordering.ConfigurationsOrderingHelper
import nsl.sam.spring.config.ordering.ReservedNumbersFinder
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

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.listableBeanFactory = ListableBeanFactory::class.cast(beanFactory)
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {

        val configurationsOrderingHelper = ConfigurationsOrderingHelper("enable-order")
        val orderingHelper = configurationsOrderingHelper.getObj()
        if(!orderingHelper.isAlreadyInitializedWithRestrictedList) {
            val reservedNumbersFinder = ReservedNumbersFinder(listableBeanFactory)
            val reservedNumbers = reservedNumbersFinder.findReservedNumbers()
            orderingHelper.initializeWithRestrictedList(reservedNumbers)
        }

//        if(!ConfigurationsOrderingManager.isAlreadyInitializedWithRestrictedList) {
//            val reservedNumbersFinder = ReservedNumbersFinder(listableBeanFactory)
//            val reservedNumbers = reservedNumbersFinder.findReservedNumbers()
//            ConfigurationsOrderingManager.initializeWithRestrictedList(reservedNumbers)
//        }

        val annotationAttributes = getAnnotationAttributes(importingClassMetadata)
        log.debug("annotation attributes for ${importingClassMetadata.className}: $annotationAttributes")

        val dynamicClass = RenamedClassProvider.getRenamedClass(
                DynamicWebSecurityConfigurer::class.java,
                DynamicWebSecurityConfigurer::class.java.canonicalName + ClassNameSuffixSequencer.getNextValue()
        )
        val bd = BeanDefinitionBuilder.genericBeanDefinition(dynamicClass as Class<WebSecurityConfigurerAdapter>){
            val configurersFactories = listableBeanFactory.getBean(ConfigurersFactories::class.java)
            var simpleAuthenticationEntryPoint = listableBeanFactory.getBean(AuthenticationEntryPoint::class.java)
            val constructor = dynamicClass.getConstructor(ConfigurersFactories::class.java, AuthenticationEntryPoint::class.java)
            constructor.newInstance(configurersFactories, simpleAuthenticationEntryPoint)
        }.beanDefinition

        bd.propertyValues.add("enableAnnotationAttributes", annotationAttributes)
        registry.registerBeanDefinition(
                dynamicClass.canonicalName, bd
        )
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
                    val configurationsOrderingHelper = ConfigurationsOrderingHelper("enable-order")
                    val orderingHelper = configurationsOrderingHelper.getObj()
                    orderingHelper.getNextNumber()
                    //ConfigurationsOrderingManager.getNextNumber()
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
