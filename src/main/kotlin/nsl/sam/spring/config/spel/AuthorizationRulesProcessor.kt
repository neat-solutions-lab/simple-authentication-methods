package nsl.sam.spring.config.spel

import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.security.config.annotation.web.builders.HttpSecurity

class AuthorizationRulesProcessor(private val httpSecurity: HttpSecurity) {

    fun process(expression: String) {
        val root = AuthorizationRulesRoot(httpSecurity)
        val parser = SpelExpressionParser()
        val expression = parser.parseExpression(expression)
        expression.getValue(root)
    }
}