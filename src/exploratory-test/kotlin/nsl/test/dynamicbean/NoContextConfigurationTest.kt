package nsl.test.dynamicbean

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("exploratory")
@ExtendWith(SpringExtension::class)
class NoContextConfigurationTest {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @Test
    fun printBeanDefinitions() {
        println("List of registered bean definitions:")
        appCtx.beanDefinitionNames.forEach(::println)

        println("\nNumber of bean definitions: ${appCtx.beanDefinitionCount}")
    }

}