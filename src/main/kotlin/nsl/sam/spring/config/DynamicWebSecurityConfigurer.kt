package nsl.sam.spring.config

import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.ConfigurersFactories
import nsl.sam.logger.logger
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableAnnotationAttributes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.Ordered
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.util.Assert
import javax.annotation.PostConstruct


open class DynamicWebSecurityConfigurer(
        private val configurersFactories: ConfigurersFactories,
        private val simpleAuthenticationEntryPoint: AuthenticationEntryPoint): WebSecurityConfigurerAdapter(), Ordered {

    companion object { val log by logger() }

    /**
     * NOTE: This property is "injected" with the help of DynamicImportBeanDefinitionRegistar,
     * it holds values of attributes used with [nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods]
     */
    lateinit var enableAnnotationAttributes: EnableAnnotationAttributes

    private val authMethodInternalConfigurers: MutableList<AuthMethodInternalConfigurer> = mutableListOf()

    @PostConstruct
    fun initialize() {

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
        log.debug("DynamicWebSecurityConfigurer order: ${this.enableAnnotationAttributes.order}")
        return this.enableAnnotationAttributes.order
    }

    override fun configure(http: HttpSecurity) {

        if(this.enableAnnotationAttributes.match != "") {
            http.antMatcher(this.enableAnnotationAttributes.match)
        }

        log.info("${this::class.simpleName} configuration entry point called [configure(HttpSecurity)].")
        if(isAtLeastOneAuthMechanismAvailable()) {
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

    private fun isAtLeastOneAuthMechanismAvailable() : Boolean {
        authMethodInternalConfigurers.asSequence().find {
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

        authMethodInternalConfigurers.filter {
            log.info("Checking if authentication method ${it.methodName()} is active.")
            val isActive = it.isActive()
            log.info("Check result for authentication method ${it.methodName()}: $isActive")
            isActive
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
                .exceptionHandling().authenticationEntryPoint(simpleAuthenticationEntryPoint)
    }

}
