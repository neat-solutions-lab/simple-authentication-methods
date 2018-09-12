package nsl.test.duplicatedefinitions

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor

class RegistryPostprocessor: BeanDefinitionRegistryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {

        val beanDefinitionOne = BeanDefinitionBuilder.genericBeanDefinition(SomeDynamicBean::class.java).beanDefinition
        beanDefinitionOne.propertyValues.add("someProperty", "one")
        registry.registerBeanDefinition(
                "dynamicBean",
                beanDefinitionOne
        )

        val beanDefinitionTwo = BeanDefinitionBuilder.genericBeanDefinition(SomeDynamicBean::class.java).beanDefinition
        beanDefinitionTwo.propertyValues.add("someProperty", "two")
        registry.registerBeanDefinition(
                "dynamicBean",
                beanDefinitionTwo
        )


    }
}