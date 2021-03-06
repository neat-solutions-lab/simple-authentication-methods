package nsl.sam.core.config.spel

import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.security.config.annotation.web.builders.HttpSecurity

class AuthorizationRulesProcessor(private val httpSecurity: HttpSecurity) {

    fun process(expression: String) {
        val root = AuthorizationRulesRoot(httpSecurity)
        val parser = SpelExpressionParser()
        val parsedExpression = parser.parseExpression(expression)
        parsedExpression.getValue(root)
    }
}