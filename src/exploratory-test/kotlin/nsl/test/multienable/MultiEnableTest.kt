package nsl.test.multienable

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@Tag("exploratory")
@SpringJUnitConfig(classes =
[ConfigurationOne::class, ConfigurationTwo::class, ConfigurationThree::class, ConfigurationFour::class])
class MultiEnableTest {

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Test
    fun printBeanNames() {
        applicationContext.beanDefinitionNames.forEach(::println)
    }

    @Test
    fun checkNumberOfConfigurationBeans() {
        print("Number of beans of type ${ConfigurationBean::class.qualifiedName}: ")
        println(applicationContext.getBeansOfType(ConfigurationBean::class.java).size)
    }

}

@Configuration
@EnableAnnotation
class ConfigurationOne

@Configuration
@EnableAnnotation
class ConfigurationTwo

@Configuration
@EnableAnnotation
class ConfigurationThree

@Configuration
@EnableAnnotation
class ConfigurationFour