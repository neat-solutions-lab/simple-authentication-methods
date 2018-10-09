package nsl.test.beandefinition

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.AbstractBeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@Tag("exploratory")
@SpringJUnitConfig(classes = [TestContextConfiguration::class])
class BeanDefinitionExaminationTest {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @Autowired
    lateinit var beanFactory: ConfigurableListableBeanFactory

    @Test
    fun exploreBeanDefinition() {

        val bigBeanDefinition = beanFactory.getBeanDefinition("bigBean")
        println("bigBeanDefinition: $bigBeanDefinition")

        println("definition class: ${bigBeanDefinition::class.qualifiedName}")

        if (bigBeanDefinition is AbstractBeanDefinition) {
            println("BigBean's qualifiers:")
            bigBeanDefinition.qualifiers.forEach {
                println("qualifier: $it")
            }

            println("Attribute names:")
            bigBeanDefinition.attributeNames().forEach {
                println(it)
            }
        }
    }
}

class SmallBeanOne {
    val name = "Small Bean One"
}

class SmallBeanTwo {
    val name = "Small Bean Two"
}

class SmallBeanThree {
    val name = "Small Bean Three"
}

class BigBean {

    @Autowired
    @Qualifier("nameOfSmallOneBean")
    lateinit var smallBeanOne: SmallBeanOne

    @Autowired
    lateinit var smallBeanTwo: SmallBeanTwo

    @Autowired
    lateinit var smallBeanThree: SmallBeanThree

}

@Configuration
@ImportResource("classpath:/beans.xml")
class TestContextConfiguration {

    @Bean("nameOfSmallOneBean")
    fun smallOne(): SmallBeanOne {
        return SmallBeanOne()
    }

    @Bean
    fun smallTwo(): SmallBeanTwo {
        return SmallBeanTwo()
    }

    @Bean
    fun smallThree(): SmallBeanThree {
        return SmallBeanThree()
    }

}