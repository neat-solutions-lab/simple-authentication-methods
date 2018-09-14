package nsl.sam.spring.config

import nsl.sam.logger.logger
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration

class BeanDefinitionRegistryPostProcessorImpl: BeanDefinitionRegistryPostProcessor {

    companion object {
        val log by logger()
    }

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {

        log.debug("${this.javaClass.name}.postProcessBeanDefinitionRegistry()")

        registry.beanDefinitionNames.filter {
            it.startsWith(SpringBootWebSecurityConfiguration::class.qualifiedName!!)
        }.forEach{
            log.info("Removing $it bean definition.")
            registry.removeBeanDefinition(it)
        }

    }
}