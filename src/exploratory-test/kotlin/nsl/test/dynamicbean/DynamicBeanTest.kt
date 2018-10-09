package nsl.test.dynamicbean

import nsl.test.dynamicbean.annotation.EnableSomething
import nsl.test.dynamicbean.beans.DynamicBean
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import kotlin.reflect.full.cast

@Tag("exploratory")
@SpringJUnitConfig(classes = [DynamicBeanTestConfigurationOne::class])
class DynamicBeanTest {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @Test
    fun printBeanDefinitions() {
        println("List of registered bean definitions:")
        appCtx.beanDefinitionNames.forEach(::println)

        println("\nNumber of bean definitions: ${appCtx.beanDefinitionCount}")
    }

    @Test
    fun gelAllBeansOfTypeDynamicBean() {
        val beansMap = appCtx.getBeansOfType(DynamicBean::class.java)
        for ((name, bean) in beansMap) {
            println("Bean of name $name: $bean, configurationString: ${DynamicBean::class.cast(bean).configurationString}")
        }
    }
}

@Configuration
@EnableSomething
class DynamicBeanTestConfigurationOne
