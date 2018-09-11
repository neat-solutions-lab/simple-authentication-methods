package nsl.test.dynamicbean

import nsl.test.dynamicbean.annotation.DynamicBeansRegistar
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@Tag("exploratory")
@SpringJUnitConfig(classes = [TestConfiguration::class])
class NoEnableSomethingAnnotationTest {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @Test
    fun printBeanDefinitions() {
        println("List of registered bean definitions:")
        appCtx.beanDefinitionNames.forEach (::println)

        println("\nNumber of bean definitions: ${appCtx.beanDefinitionCount}")
    }

    @Test
    fun testConfigurationBeanPresent() {
        val configurationBean = appCtx.getBean("testConfiguration")
        println("configurationBean: $configurationBean")
    }

    @Test
    fun noDynamicBeansRegistar() {
        try {
            appCtx.getBean(DynamicBeansRegistar::class.java)
        } catch (e: Exception) {
            println("Exception caught: $e")
        }
    }
}

@Configuration
class TestConfiguration