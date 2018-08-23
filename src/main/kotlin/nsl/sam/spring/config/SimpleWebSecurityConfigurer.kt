package nsl.sam.spring.config

import nsl.sam.config.SimpleAuthConfigurer
import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.logger.logger
import nsl.sam.sender.ResponseSender
import nsl.sam.spring.entrypoint.SimpleFailedAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.servlet.config.annotation.EnableWebMvc

//@EnableWebMvc
//@EnableWebSecurity
@Order(90)
class SimpleWebSecurityConfigurer : WebSecurityConfigurerAdapter {

    companion object { val log by logger() }

    @Autowired
    @Qualifier("unauthenticatedAccessResponseSender")
    lateinit var errorResponseSender: ResponseSender

    val authMethodRegistars: List<AuthMethodRegistar>
    var simpleAuthConfigurers: List<SimpleAuthConfigurer>

    constructor(@Autowired(required = false) authMethodRegistars: List<AuthMethodRegistar>?,
                @Autowired(required = false) simpleAuthConfigurers: List<SimpleAuthConfigurer>?) : super() {
        this.authMethodRegistars = authMethodRegistars ?: emptyList()
        this.simpleAuthConfigurers = simpleAuthConfigurers ?: emptyList()
    }

    override fun configure(http: HttpSecurity) {
        log.info("${this::class.simpleName} configuration entry point called.")
        if(isAuthMechanismAvailable()) {
            log.info("Enabling authentication mechanisms")
            activateAuthenticationMechanisms(http)
        } else {
            log.info("Enabling anonymous access")
            activateAnonymousAccess(http)
        }

        applyCommonSecuritySettings(http)

        for(simpleAuthConfigurer in this.simpleAuthConfigurers) {
            simpleAuthConfigurer.configure(http)
        }
    }

    @Autowired
    fun globalConfig(authBuilder: AuthenticationManagerBuilder) {
        for(simpleAuthConfigurer in this.simpleAuthConfigurers) {
            simpleAuthConfigurer.configure(authBuilder)
        }
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
    fun simpleAuthenticationEntryPoint(): AuthenticationEntryPoint {
        return SimpleFailedAuthenticationEntryPoint(errorResponseSender)
    }

    private fun applyCommonSecuritySettings(http: HttpSecurity) {

        log.info("Applying common security settings for simple-authentication-methods")

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(simpleAuthenticationEntryPoint())
    }

}