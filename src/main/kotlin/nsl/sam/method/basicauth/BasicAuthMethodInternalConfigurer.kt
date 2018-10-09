package nsl.sam.method.basicauth

import nsl.sam.logger.logger
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.method.basicauth.userdetails.AvailabilityAwareUserDetailsService
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.AuthenticationEntryPoint

class BasicAuthMethodInternalConfigurer(
        private val localUsersDetailsService: AvailabilityAwareUserDetailsService,
        private val simpleAuthenticationEntryPoint: AuthenticationEntryPoint)
    : AuthMethodInternalConfigurer {

    companion object { val log by logger() }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(localUsersDetailsService)
    }

    override fun hasItems(): Boolean {
        return this.localUsersDetailsService.hasItems()
    }

    override fun configure(http: HttpSecurity): HttpSecurity {
        log.info("Enabling HttpBasic Auth method.")
        return http.httpBasic().authenticationEntryPoint(simpleAuthenticationEntryPoint).and()
    }

    override fun methodName(): String {
        return "Local Basic Auth Method"
    }
}