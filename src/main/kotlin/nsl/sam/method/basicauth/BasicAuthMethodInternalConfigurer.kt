package nsl.sam.method.basicauth

import nsl.sam.logger.logger
import nsl.sam.method.basicauth.userdetails.LocalFileUsersImporter
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.method.basicauth.userdetails.SourceAwareUserDetailsService
import org.springframework.context.ApplicationContext
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint

class BasicAuthMethodInternalConfigurer(
        private val localUsersDetailsService: SourceAwareUserDetailsService,
        private val simpleAuthenticationEntryPoint: AuthenticationEntryPoint)
    : AuthMethodInternalConfigurer {

    companion object { val log by logger() }

    override fun configure(authBuilder: AuthenticationManagerBuilder) {
        authBuilder.userDetailsService(localUsersDetailsService)
    }

    override fun isAvailable(): Boolean {
        return this.localUsersDetailsService.isUsersSourceAvailable()
    }

    override fun configure(http: HttpSecurity): HttpSecurity {
        log.info("Enabling HttpBasic Auth method.")
        return http.httpBasic().authenticationEntryPoint(simpleAuthenticationEntryPoint).and()
    }

    override fun methodName(): String {
        return "Local Basic Auth Method"
    }
}