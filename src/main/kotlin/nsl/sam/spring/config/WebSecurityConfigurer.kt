package nsl.sam.spring.config

import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.logger.logger
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@EnableWebSecurity
@Order(90)
class WebSecurityConfigurer(
        val authMethodRegistars : List<AuthMethodRegistar>
) : WebSecurityConfigurerAdapter() {

    companion object { val log by logger() }

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

    private fun applyCommonSecuritySettings(http: HttpSecurity) {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}