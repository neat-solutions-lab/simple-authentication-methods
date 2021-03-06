package nsl.sam.core.config

import nsl.sam.annotation.inject.InjectedObjectsProvider
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.ConfigurersFactories
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.annotation.attrtypes.PortsMapping
import nsl.sam.core.config.spel.AuthorizationRulesProcessor
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.core.entrypoint.factory.DefaultAuthenticationEntryPointFactory
import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.util.Assert
import javax.annotation.PostConstruct
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance


open class InstrumentedWebSecurityConfigurerTemplate(
        private val configurersFactories: ConfigurersFactories)
    : WebSecurityConfigurerAdapter(), Ordered {

    companion object {
        val BASE_NAME_FOR_AUTOGENERATED_CLASS =
                InstrumentedWebSecurityConfigurerTemplate::class.java.`package`.name + ".AutogeneratedWebSecurityConfigurer"

        val log by logger()
    }

    @Value("\${nsl.sam.anonymous-fallback:false}")
    private var anonymousFallbackAttr: Boolean = false

    @Value("\${server.address:}")
    private var serverAddressAttr = ""

    @Autowired
    private lateinit var environment: Environment

    @Autowired(required = false)
    private var accessDeniedHandler: AccessDeniedHandler? = null

    private var areActivationConditionsChecked = false
    private var areActivationConditionsMet = false

    /**
     * NOTE: This property is "injected" with the help of DynamicImportBeanDefinitionRegistar,
     * it holds values of attributes used with [nsl.sam.core.annotation.EnableSimpleAuthenticationMethods]
     */
    @Suppress
    lateinit var enableAnnotationAttributes: EnableAnnotationAttributes

    private val authMethodInternalConfigurers: MutableList<AuthMethodInternalConfigurer> = mutableListOf()

    private lateinit var authenticationEntryPoint: AuthenticationEntryPoint

    @PostConstruct
    fun initialize() {


        authenticationEntryPoint = InjectedObjectsProvider.Builder(AuthenticationEntryPointFactory::class)
                .attributeName("authenticationEntryPointFactory")
                .defaultFactoryPropertyName("nsl.sam.authentication-entry-point.factory")
                .involvedAnnotationTypes(listOf(EnableSimpleAuthenticationMethods::class))
                .annotationMetadata(enableAnnotationAttributes.enableAnnotationMetadata)
                .environment(environment)
                .defaultFactory(DefaultAuthenticationEntryPointFactory::class)
                .build().getObject()

        log.info("selected ${AuthenticationEntryPoint::class.simpleName}: ${authenticationEntryPoint::class.simpleName}")

        /*
         * for each enabled authorization method create "internal configurer" to which further configuration
         * steps will be delegated
         */
        enableAnnotationAttributes.methods
                .filter { it != AuthenticationMethod.SIMPLE_NO_METHOD }
                .forEach {
                    log.info("${EnableSimpleAuthenticationMethods::class.simpleName} annotation enables $it method")
                    val factory = configurersFactories.getFactoryForMethod(it)
                    Assert.notNull(factory, "There is no AuthMethodInternalConfigurerFactory registered " +
                            "for ${it.name} authorization method")
                    log.info("Factory to create internal configurer for $it method is ${factory!!::class.qualifiedName}")
                    this.authMethodInternalConfigurers.add(factory.create(enableAnnotationAttributes))
                }
    }

    override fun getOrder(): Int {
        log.debug("InstrumentedWebSecurityConfigurerTemplate order: ${this.enableAnnotationAttributes.order}")
        return this.enableAnnotationAttributes.order
    }

    override fun configure(http: HttpSecurity) {

        /*
         * select path handled by this security chain
         */
        if (this.enableAnnotationAttributes.match != "") {
            log.info("Configuring security for path: ${this.enableAnnotationAttributes.match}")
            http.antMatcher(this.enableAnnotationAttributes.match)
        }

        /*
         * force https if required
         */
        if(this.enableAnnotationAttributes.forceHttps) {
           log.info("Forcing HTTPS channel (forceHttps attribute is set to true)")
            http.requiresChannel().anyRequest().requiresSecure()
            this.enableAnnotationAttributes.portMapping.forEach {
                val mapping = it.createInstance()
                http.portMapper().http(mapping.getMapping().first).mapsTo(mapping.getMapping().second)
            }
        }

        /*
         * configure authentication methods
         */
        log.info("${this::class.simpleName} configuration entry point called [configure(HttpSecurity)].")
        if (areActivationConditionsMet()) {
            log.info("Enabling authorization mechanisms")
            activateAuthenticationMechanisms(http)
        } else {
            log.info("Enabling anonymous access")
            permitAll(http)
        }

        applyCommonSecuritySettings(http)
    }

    override fun configure(authBuilder: AuthenticationManagerBuilder) {
        if(!areActivationConditionsMet()) return
        for (registar in this.authMethodInternalConfigurers) {
            registar.configure(authBuilder)
        }
    }

    private fun _areActivationConditionsMet(): Boolean {
        /*
         * if at least one underlying UsersSource is able to provide at least one user then
         * authentication should be activated
         */
        if (isAtLeastOneAuthMechanismAvailable()) return true

        /*
         * it can be explicitly configured that services which listen only
         * on localhost interface fallback to mode in which anonymous access
         * is allowed to all resources
         */
        if (areLocalAnonymousAccessConditionsMet()) return false

        /*
         * even if all UserSource(s) are 'empty' still activate authorization rules
         * to NOT ACCIDENTALLY OPEN ACCESS to protected resources
         */
        return true
    }

    private fun areActivationConditionsMet(): Boolean {

        if(!areActivationConditionsChecked) {
            areActivationConditionsMet = _areActivationConditionsMet()
            areActivationConditionsChecked = true
        }
        return areActivationConditionsMet
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
        if (this.serverAddressAttr in arrayOf("localhost", "127.0.0.1")) return true
        return false
    }

    /*
     *
     */
    private fun isLocalAnonymousAccessFallbackModeEnabled(): Boolean {
        if (this.enableAnnotationAttributes.anonymousFallback) return true
        return this.anonymousFallbackAttr
    }

    private fun isAtLeastOneAuthMechanismAvailable(): Boolean {
        authMethodInternalConfigurers.asSequence().find {
            log.info("Checking if authorization method ${it.methodName()} is available.")
            val isAvailable = it.hasItems()
            log.info("Check result for authorization method ${it.methodName()}: $isAvailable")
            isAvailable
        }?.let {
            return true
        }
        return false
    }

    private fun activateAuthenticationMechanisms(http: HttpSecurity) {

        applyAuthenticationRules(http)

        authMethodInternalConfigurers.filter {
            log.info("Checking if authentication method ${it.methodName()} is active.")
            val isAvailable = it.hasItems()
            log.info("Check result for authentication method ${it.methodName()}: $isAvailable")
            isAvailable
        }.forEach {
            log.info("Registering authorization mechanism: ${it.methodName()}")
            it.configure(http)
        }
    }

    private fun applyAuthenticationRules(httpSecurity: HttpSecurity) {
        when {
            enableAnnotationAttributes.authorizations.isBlank() ->
                fullyAuthenticatedAccess(httpSecurity)
            else ->
                processAuthorizationRulesExpression(httpSecurity)
        }
    }

    private fun processAuthorizationRulesExpression(httpSecurity: HttpSecurity) {
        log.info("Applying authorization rules: ${enableAnnotationAttributes.authorizations}")
        AuthorizationRulesProcessor(httpSecurity).process(enableAnnotationAttributes.authorizations)

        /*
         * it is required to block access to all other places (paths) then those explicitly
         * mentioned by authentications attribute
         */
        httpSecurity.authorizeRequests().antMatchers("/**").denyAll()
    }

    private fun fullyAuthenticatedAccess(httpSecurity: HttpSecurity) {
        httpSecurity.authorizeRequests().anyRequest().fullyAuthenticated()
    }

    private fun permitAll(http: HttpSecurity) {

        /*
         * it is only for the sake of clarity, the default settings seems to be the same
         * as the ones being set by the code below
         */
        http.authorizeRequests().anyRequest().permitAll()
    }

    private fun applyCommonSecuritySettings(http: HttpSecurity) {

        log.info("Applying common security settings for simple-authorization-methods")

        http
                //.anonymous().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
        if(this.accessDeniedHandler != null) {
            http.exceptionHandling().accessDeniedHandler(this.accessDeniedHandler)
        }

    }
}
