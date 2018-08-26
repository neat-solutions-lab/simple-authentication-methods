package nsl.sam.spring.annotation

import nsl.sam.spring.config.EnableWebSecurityInDebugMode
import nsl.sam.spring.config.EnableWebSecurityInDefaultMode
import nsl.sam.spring.config.GeneralConfigurationActivator
import org.springframework.context.annotation.Import

/**
 * Annotation to be used by simple-authentication-methods' consumers to enable
 * authentication methods implemented by this this library.
 *
 * By default all methods are enabled but they can be narrow down selectively with [methods] parameter.
 *
 * @param methods array of authentication methods to be enabled
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Import(value =
    [
        GeneralConfigurationActivator::class,
        EnabledEntrypointsSelector::class,
        EnableWebSecurityInDebugMode::class,
        EnableWebSecurityInDefaultMode::class
    ])
annotation class EnableSimpleAuthenticationMethods(

        val methods: Array<AuthenticationMethod> =
                [(AuthenticationMethod.SIMPLE_BASIC_AUTH), (AuthenticationMethod.SIMPLE_TOKEN)],

        val deactivateIfNotFullyConfigured: Boolean = false,

        val fallbackToAnonymousAccessIfNoMethodAvailable: Boolean = false,

        val match: String = "/",

        val order: Int = 100,

        val authorizations: String = "",

        val debug: Boolean = false
)
