package nsl.sam.method.token

import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.logger.logger
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.method.token.tokendetails.AvailabilityAwareTokenDetailsService
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class TokenAuthMethodInternalConfigurer(
        private val tokenDetailsService: AvailabilityAwareTokenDetailsService,
        private val authenticationEntryPoint: AuthenticationEntryPoint) : AuthMethodInternalConfigurer {

    override fun configure(auth: AuthenticationManagerBuilder) {
        // empty, the TokenAuthenticationFiler doesn't use AunthenticationManager
    }

    companion object {
        val log by logger()
    }


    override fun hasItems(): Boolean {
        return tokenDetailsService.hasItems()
    }

    override fun configure(http: HttpSecurity): HttpSecurity {
        log.info("Registering ${TokenAuthenticationFilter::class.qualifiedName} filter.")
        return http.addFilterBefore(
                TokenAuthenticationFilter(tokenDetailsService, authenticationEntryPoint),
                BasicAuthenticationFilter::class.java)
    }

    override fun methodName(): String {
        return "Local Token Method"
    }
}