package nsl.sam.spring.config

//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor

import nsl.sam.logger.logger
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

class DynamicBeansRegistar: BeanDefinitionRegistryPostProcessor {

    companion object {
        val log by logger()
        var enableAnnotations: MutableList<AnnotationMetadata> = mutableListOf()
    }

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {

        //beanFactory.
        val beanDefinitionRegistry = beanFactory as BeanDefinitionRegistry
        beanDefinitionRegistry.beanDefinitionCount
        println("+++++++++++++++")
        println("+++++++++++++++")
        println("+++++++++++++++")
        println("+++++++++++++++")
        //beanFactory.getBeanNamesForType(We)
        beanFactory.getBeanNamesForAnnotation(EnableWebSecurity::class.java).forEach {
            println("it: $it")
        }

        beanFactory.getBeanNamesForAnnotation(EnableSimpleAuthenticationMethods::class.java).forEach {
            println("moja annotacja: $it")
        }

        //val beanDefinition = beanFactory.getBeanDefinition("nsl.sam.spring.config.EnableWebSecurityConfiguration")
        //beanDefinition.
    }


    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {

        //registry.getBeanDefinition("")

        //registry.getBeanDefinition("").

        registry.beanDefinitionNames.filter {
            it.startsWith(SpringBootWebSecurityConfiguration::class.qualifiedName!!)
        }.forEach{
                    println("???????? $it")
                    println("???????? $it")
                    println("???????? $it")
                    println("???????? $it")
                    println("???????? $it")
                    println("???????? $it")

                    log.info("Removing $it bean definition.")
                    registry.removeBeanDefinition(it)
        }

        //registry.removeBeanDefinition("org.springframework.boot.autoconfigure.security.servlet.WebSecurityEnablerConfiguration")

        if(isDebugModeEnabled()) {
            log.info("Including EnableWebSecurity in debug mode.")
//            registry.registerBeanDefinition(EnableWebSecurityInDebugMode::class.qualifiedName!!,
//                    BeanDefinitionBuilder.genericBeanDefinition(EnableWebSecurityInDebugMode::class.java).beanDefinition)
        } else {
            log.info("Including EnableWebSecurity in default mode.")
//            registry.registerBeanDefinition(EnableWebSecurityInDefaultMode::class.qualifiedName!!,
//                    BeanDefinitionBuilder.genericBeanDefinition(EnableWebSecurityInDefaultMode::class.java).beanDefinition)
        }

        enableAnnotations.forEach {
            log.info("Creating dynamic WebSecurityConfigurer basing on annotation: $it")
        }
    }

    private fun getAnnotationAttributes(annotationMetadata: AnnotationMetadata):AnnotationAttributes? {
        return AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
        )
    }

    private fun isDebugModeEnabled(): Boolean {

        enableAnnotations.find {
            val annotationAttributes = getAnnotationAttributes(it)
            annotationAttributes?.getBoolean("debug") == true
        }?.let {
            log.info("${EnableSimpleAuthenticationMethods::class.simpleName} annotation with debug mode enabled found.")
            return true
        }

        println("????????????????????????????????")
        println("????????????????????????????????")
        println("????????????????????????????????")
        println("????????????????????????????????")
        println("????????????????????????????????")
        println("????????????????????????????????")
        println("????????????????????????????????")
        println("????????????????????????????????")

        log.info("No ${EnableSimpleAuthenticationMethods::class.simpleName} annotation with debug mode enabled.")
        return false
    }

}