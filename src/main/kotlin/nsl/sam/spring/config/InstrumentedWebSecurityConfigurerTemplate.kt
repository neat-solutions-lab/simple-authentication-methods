package nsl.sam.spring.config

import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.ConfigurersFactories
import nsl.sam.logger.logger
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableAnnotationAttributes
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.util.Assert
import javax.annotation.PostConstruct


open class InstrumentedWebSecurityConfigurerTemplate(
        private val configurersFactories: ConfigurersFactories,
        // TODO: defaultAuthenticationEntryPoint should be configured
        private val defaultAuthenticationEntryPoint: AuthenticationEntryPoint)
    : WebSecurityConfigurerAdapter(), Ordered {

    companion object { val log by logger() }

    @Value("\${nsl.sam.anonymous-fallback:false}")
    private var anonymousFallbackAttr: Boolean = false

    @Value("\${server.address:}")
    private var serverAddressAttr = ""

    /**
     * NOTE: This property is "injected" with the help of DynamicImportBeanDefinitionRegistar,
     * it holds values of attributes used with [nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods]
     */
    @Suppress
    lateinit var enableAnnotationAttributes: EnableAnnotationAttributes

    private val authMethodInternalConfigurers: MutableList<AuthMethodInternalConfigurer> = mutableListOf()

    //private

    @PostConstruct
    fun initialize() {

        /*
         * for each enabled authentication method create "internal configurer" to which further configuration
         * steps will be delegated
         */
        enableAnnotationAttributes.methods
                .filter{ it != AuthenticationMethod.SIMPLE_NO_METHOD }
                .forEach {
                    val factory = configurersFactories.getFactoryForMethod(it)
                    Assert.notNull(factory,"There is no AuthMethodInternalConfigurerFactory registered " +
                        "for ${it.name} authentication method")
                    this.authMethodInternalConfigurers.add(factory!!.create(enableAnnotationAttributes))
                }
    }

    override fun getOrder(): Int {
        log.debug("InstrumentedWebSecurityConfigurerTemplate order: ${this.enableAnnotationAttributes.order}")
        return this.enableAnnotationAttributes.order
    }

    override fun configure(http: HttpSecurity) {

        if(this.enableAnnotationAttributes.match != "") {
            log.info("Configuring security for path: ${this.enableAnnotationAttributes.match}")
            http.antMatcher(this.enableAnnotationAttributes.match)
        }

        log.info("${this::class.simpleName} configuration entry point called [configure(HttpSecurity)].")
        if(areActivationConditionsMet()) {
            log.info("Enabling authentication mechanisms")
            activateAuthenticationMechanisms(http)
        } else {
            log.info("Enabling anonymous access")
            permitAll(http)
        }

        applyCommonSecuritySettings(http)
    }

    override fun configure(authBuilder: AuthenticationManagerBuilder) {
        for(registar in this.authMethodInternalConfigurers) {
            registar.configure(authBuilder)
        }
    }

    private fun areActivationConditionsMet(): Boolean {

        /*
         * if at least one underlying UsersSource is able to provide at least one user then
         * AA should be activated
         */
        if (isAtLeastOneAuthMechanismAvailable()) return true

        /*
         * it can be explicitly configured that services which listen only
         * on localhost interface fallback to mode in which anonymous access
         * is allowed to all resources
         */
        if(areLocalAnonymousAccessConditionsMet()) return false


        /*
         * even if all UserSource(s) are 'empty' still activate authentication rules
         * to NOT ACCIDENTALLY OPEN ACCESS to protected resources
         */
        return true
    }

    /*
     *
     */
    private fun areLocalAnonymousAccessConditionsMet(): Boolean {
        return isItOnlyLocalService() && isLocalAnonymousAccessFallbackModeEnabled()
    }

    /*
     * checks if the server listens only on localhost/127.0.0.1
     */
    private fun isItOnlyLocalService(): Boolean {
        if(this.serverAddressAttr in arrayOf("localhost", "127.0.0.1")) return true
        return false
    }

    /*
     *
     */
    private fun isLocalAnonymousAccessFallbackModeEnabled(): Boolean {
        if (this.enableAnnotationAttributes.anonymousFallback) return true
        return this.anonymousFallbackAttr
    }

    private fun isAtLeastOneAuthMechanismAvailable() : Boolean {
        authMethodInternalConfigurers.asSequence().find {
            log.info("Checking if authentication method ${it.methodName()} is available.")
            val isAvailable = it.isAvailable()
            log.info("Check result for authentication method ${it.methodName()}: $isAvailable")
            isAvailable
        }?.let{
            return true
        }
        return false
    }

    private fun activateAuthenticationMechanisms(http: HttpSecurity) {

        http.authorizeRequests().anyRequest().fullyAuthenticated()

        authMethodInternalConfigurers.filter {
            log.info("Checking if authentication method ${it.methodName()} is active.")
            val isAvailable = it.isAvailable()
            log.info("Check result for authentication method ${it.methodName()}: $isAvailable")
            isAvailable
        }.forEach {
            log.info("Registering authentication mechanism: ${it.methodName()}")
            it.configure(http)
        }
    }

    private fun permitAll(http: HttpSecurity) {

        /*
         * it is only for the sake of clarity, the default settings seems to be the same
         * as the ones being set by the below code
         */
        http.authorizeRequests().anyRequest().permitAll()
    }

    private fun applyCommonSecuritySettings(http: HttpSecurity) {

        log.info("Applying common security settings for simple-authentication-methods")

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(defaultAuthenticationEntryPoint)
    }
}

