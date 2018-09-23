package nsl.test.duplicatedefinitions

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.ApplicationContext
import kotlin.reflect.full.cast

class RegistryPostprocessor: BeanDefinitionRegistryPostProcessor, BeanFactoryAware {

    lateinit var beanFactoryInstance: BeanFactory

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactoryInstance = beanFactory
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
        println("1111111111111111111111111111111")
    }

    @Autowired
    lateinit var appCtx: ApplicationContext

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {

        println("AAAAAAAAAAAAAAAAAAAAAAAA")
        println("AAAAAAAAAAAAAAAAAAAAAAAA")
        println("AAAAAAAAAAAAAAAAAAAAAAAA")
        println("AAAAAAAAAAAAAAAAAAAAAAAA")
        println("AAAAAAAAAAAAAAAAAAAAAAAA")
        println("AAAAAAAAAAAAAAAAAAAAAAAA")
        println("AAAAAAAAAAAAAAAAAAAAAAAA")
        println("AAAAAAAAAAAAAAAAAAAAAAAA")

    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {

        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBB")

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

        val beanDefinitionThree = BeanDefinitionBuilder.genericBeanDefinition(AnotherDynamicBean::class.java) {

//            appCtx.beanDefinitionNames.forEach {
//                println(it)
//            }

            ListableBeanFactory::class.cast(beanFactoryInstance).beanDefinitionNames.forEach { println(it) }
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            println("CCCCCCCCCCCCCCCCCCCCCCCc")
            AnotherDynamicBean("bla")
        }
        registry.registerBeanDefinition("three", beanDefinitionThree.beanDefinition)

    }
}