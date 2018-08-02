package nsl.sam.spring.config

import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.logger.logger
import nsl.sam.spring.entrypoint.Simple401EntryPoint
import nsl.sam.spring.handler.SimpleAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@EnableWebSecurity
@Order(90)
class WebSecurityConfigurer : WebSecurityConfigurerAdapter {

    companion object { val log by logger() }

    val authMethodRegistars: List<AuthMethodRegistar>

    constructor(authMethodRegistars: List<AuthMethodRegistar>) : super() {
        this.authMethodRegistars = authMethodRegistars
    }

    override fun configure(http: HttpSecurity) {
        log.info("HttpSecurity configuration entry point called.")
        if(isAuthMechanismAvailable()) {
            log.info("Enabling authentication mechanisms")
            activateAuthenticationMechanisms(http)
        } else {
            log.info("Enabling anonymous access")
            activateAnonymousAccess(http)
        }

        applyCommonSecuritySettings(http)
    }

    private fun isAuthMechanismAvailable() : Boolean {
        authMethodRegistars.asSequence().find {
            log.info("Checking if authentication method ${it.methodName()} is active.")
            val isActive = it.isActive()
            log.info("Check result for authentication method ${it.methodName()}: $isActive")
            isActive
        }?.let{
            return true
        }
        return false
    }

    private fun activateAuthenticationMechanisms(http: HttpSecurity) {
        http.authorizeRequests().anyRequest().fullyAuthenticated()

        authMethodRegistars.filter {
            log.info("Checking if authentication method ${it.methodName()} is active.")
            val isActive = it.isActive()
            log.info("Check result for authentication method ${it.methodName()}: $isActive")
            isActive
        }.forEach {
            log.info("Registering authentication mechanism: ${it.methodName()}")
            it.register(http)
        }
    }

    private fun activateAnonymousAccess(http: HttpSecurity) {
        /*
         * it is only for the sake of clarity, the default settings seems to be the same as being set
         * by the below code
         */
        http.authorizeRequests().anyRequest().permitAll()
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return SimpleAccessDeniedHandler()
    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return Simple401EntryPoint()
    }

    private fun applyCommonSecuritySettings(http: HttpSecurity) {

        log.info("Applying common security settings for simple-authentication-methods")

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                //.authenticationEntryPoint(authenticationEntryPoint())
    }

}