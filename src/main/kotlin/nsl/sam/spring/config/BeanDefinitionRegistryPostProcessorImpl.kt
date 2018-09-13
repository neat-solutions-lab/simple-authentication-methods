package nsl.sam.spring.config

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration

class BeanDefinitionRegistryPostProcessorImpl: BeanDefinitionRegistryPostProcessor {

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {

        registry.beanDefinitionNames.filter {
            it.startsWith(SpringBootWebSecurityConfiguration::class.qualifiedName!!)
        }.forEach{
            println("???????? $it")
            println("???????? $it")
            println("???????? $it")
            println("???????? $it")
            println("???????? $it")
            println("???????? $it")

            //DynamicBeansRegistar.log.info("Removing $it bean definition.")
            registry.removeBeanDefinition(it)
        }

    }
}