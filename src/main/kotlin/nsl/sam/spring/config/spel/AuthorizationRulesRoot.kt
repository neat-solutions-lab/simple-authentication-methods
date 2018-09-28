package nsl.sam.spring.config.spel

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer

class AuthorizationRulesRoot(private val httpSecurity: HttpSecurity) {

    fun antMatchers(vararg antPatterns: String): ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl {
        return httpSecurity.authorizeRequests().antMatchers(*antPatterns)
    }

}