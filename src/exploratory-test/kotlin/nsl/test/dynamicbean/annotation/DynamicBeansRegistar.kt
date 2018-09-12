package nsl.test.dynamicbean.annotation

import nsl.test.dynamicbean.beans.DynamicBean
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata
import org.springframework.beans.MutablePropertyValues



class DynamicBeansRegistar: ImportBeanDefinitionRegistrar {

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        println(">>> registerBeanDefinitions()")

        val beanDefinitionOne = BeanDefinitionBuilder.genericBeanDefinition(DynamicBean::class.java).beanDefinition
        val mpv = MutablePropertyValues()
        mpv.add("configurationString", "one two three")
        beanDefinitionOne.propertyValues = mpv
        registry.registerBeanDefinition(
                "myBeanOne", beanDefinitionOne
                )

        val beanDefinitionTwo = BeanDefinitionBuilder.genericBeanDefinition(DynamicBean::class.java).beanDefinition
        beanDefinitionTwo.propertyValues.add("configurationString", "flying bee")
        //beanDefinitionTwo.
        registry.registerBeanDefinition(
                "myBeanTwo",
                beanDefinitionTwo)

    }

}
