package nsl.sam.spring.config

//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor

import nsl.sam.logger.logger
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.core.type.AnnotationMetadata

class DynamicBeansRegistar: BeanDefinitionRegistryPostProcessor {

    companion object {
        val log by logger()
        val enableAnnotations: MutableList<AnnotationMetadata> = mutableListOf()
    }

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {

        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")
        println("###################################3")

        enableAnnotations.forEach {
            log.info("Creating dynamic WebSecurityConfigurer basing on annotation: $it")
        }

    }

}