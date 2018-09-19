package nsl.sam.spring.config

import nsl.sam.config.SimpleAuthConfigurer
import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.BasicAuthMethodRegistar
import nsl.sam.method.token.TokenAuthMethodRegistar
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.sender.ResponseSender
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.entrypoint.SimpleFailedAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint
import javax.annotation.PostConstruct

class DynamicWebSecurityConfigurer: WebSecurityConfigurerAdapter, Ordered {

    companion object { val log by logger() }

    @Autowired
    lateinit var localUsersDetailsService: UserDetailsService

    @Autowired
    lateinit var simpleAuthenticationEntryPoint: AuthenticationEntryPoint

    @Value("\${sam.passwords-file:}")
    lateinit var passwordsFile: String


    @Value("\${server.address:localhost}")
    lateinit var serverAddress: String


    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String


    @Autowired
    lateinit var tokenAuthenticator : TokenToUserMapper

    @Autowired
    @Qualifier("unauthenticatedAccessResponseSender")
    lateinit var unauthenticatedResponseSender: ResponseSender


    /**
     * NOTE: This property is "injected" with the help of DynamicImportBeanDefinitionRegistar,
     * it holds values of attributes used with [nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods]
     */
    lateinit var enableAnnotationAttributes: EnableAnnotationAttributes

    @Autowired
    @Qualifier("unauthenticatedAccessResponseSender")
    lateinit var errorResponseSender: ResponseSender

    private val authMethodRegistars: MutableList<AuthMethodRegistar> = mutableListOf()
    var simpleAuthConfigurers: List<SimpleAuthConfigurer>

    constructor(@Autowired(required = false) simpleAuthConfigurers: List<SimpleAuthConfigurer>?) : super() {
        this.simpleAuthConfigurers = simpleAuthConfigurers ?: emptyList()
    }

    @PostConstruct
    fun initialize() {
        if(enableAnnotationAttributes.methods.contains(AuthenticationMethod.SIMPLE_BASIC_AUTH)) {
            this.authMethodRegistars.add(
                    BasicAuthMethodRegistar(
                            localUsersDetailsService, simpleAuthenticationEntryPoint, passwordsFile, serverAddress)
            )
        } // if()

        if(enableAnnotationAttributes.methods.contains(AuthenticationMethod.SIMPLE_TOKEN)) {
            this.authMethodRegistars.add(
                    TokenAuthMethodRegistar(tokensFilePath, serverAddress, tokenAuthenticator, unauthenticatedResponseSender)
            )
        }

    }

    override fun getOrder(): Int {
        log.debug("DynamicWebSecurityConfigurer order: ${this.enableAnnotationAttributes.order}")
        return this.enableAnnotationAttributes.order
    }

    override fun configure(http: HttpSecurity) {

        if(this.enableAnnotationAttributes.match != "") {
            http.antMatcher(this.enableAnnotationAttributes.match)
        }

        log.info("${this::class.simpleName} configuration entry point called.")
        if(isAtLeastOneAuthMechanismAvailable()) {
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



    override fun configure(authBuilder: AuthenticationManagerBuilder) {
        for(registar in this.authMethodRegistars) {
            registar.configure(authBuilder)
        }
        for(simpleAuthConfigurer in this.simpleAuthConfigurers) {
            simpleAuthConfigurer.configure(authBuilder)
        }
    }

    private fun isAtLeastOneAuthMechanismAvailable() : Boolean {
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
         * it is only for the sake of clarity, the default settings seems to be the same
         * as the ones being set by the below code
         */
        http.authorizeRequests().anyRequest().permitAll()
    }


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