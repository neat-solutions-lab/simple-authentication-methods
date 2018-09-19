package nsl.test.spel

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.expression.BeanResolver
import org.springframework.expression.EvaluationContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@Tag("exploratory")
@SpringJUnitConfig()
@SpringBootApplication
class SpelTest {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @Autowired
    lateinit var composedComponent: ComposedComponent

    @Autowired
    lateinit var independentComponent: IndependentComponent

    @Test
    fun contextLoaded() {
        println("OK - context loaded")
        println("stringBean: ${appCtx.getBean("stringBean")}")
    }

    @Test
    fun trivialSpelTest() {
        val expressionParser = SpelExpressionParser()
        val expression = expressionParser.parseExpression("1+1")
        println("expression value: ${expression.value}")
    }

    @Test
    fun referToBeanTest() {
        val expressionParser = SpelExpressionParser()
        val expression = expressionParser.parseExpression("@stringBean")
        val evaluationContext = StandardEvaluationContext()
        evaluationContext.setBeanResolver(MyBeanResolver(this.appCtx))
        println("expression value: ${expression.getValue(evaluationContext)}")
    }

    @Test
    fun composedComponentTest() {

        println("value from componentOne: ${composedComponent.valueFromComponentOne}")
        println("value from componentTwo: ${composedComponent.valueFromComponentTwo}")
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun stringBean(): String{
            return "Hello, I'm simple string bean"
        }

    }

}

class MyBeanResolver(private val appContext: ApplicationContext): BeanResolver {

    override fun resolve(context: EvaluationContext, beanName: String): Any {
        return this.appContext.getBean(beanName)
    }

}

@Component
class IndependentComponent

@Component("componentOne")
class CompontntOne {

    fun getValueFromComponentOne() = "COMPLEX VALUE CALCULATED BY COMPONENT ONE"

}

@Component("componentTwo")
class ComponentTwo {

    fun getValueFromComponentTwo() = "COMPLEX VALUE, CALCULATED BY COMPONENT TWO"

}

@Component
class ComposedComponent {

    lateinit var valueFromComponentTwo: String

    @Value("#{@componentOne.getValueFromComponentOne()}")
    lateinit var valueFromComponentOne: String

    @Autowired
    fun consume(@Value("#{@componentTwo.getValueFromComponentTwo()}") value: String) {
        this.valueFromComponentTwo = value
    }

}