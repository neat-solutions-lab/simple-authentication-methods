package nsl.test.duplicatedefinitions

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@Tag("exploratory")
@SpringJUnitConfig(classes = [TestConfiguration::class])
class DuplicateDefinitionTests {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @Test
    fun printBeansNames() {
        appCtx.beanDefinitionNames.forEach(::println)
    }

    @Test
    fun printDynamicBeans() {
        appCtx.beanDefinitionNames.filter { it == "dynamicBean" }.forEach(::println)
    }

    @Test
    fun getDynamicBeanByName() {
        val dynamicBean: SomeDynamicBean = appCtx.getBean("dynamicBean") as SomeDynamicBean
        println("someProperty value: ${dynamicBean.someProperty}")
    }

    @Test
    fun getDynamicBeanByType() {
        val dynamicBean: SomeDynamicBean = appCtx.getBean(SomeDynamicBean::class.java)
        println("someProperty value: ${dynamicBean.someProperty}")
    }

}

@Configuration
class TestConfiguration {

    @Bean
    fun registryPostprocessor(): BeanDefinitionRegistryPostProcessor {
        return RegistryPostprocessor()
    }
}