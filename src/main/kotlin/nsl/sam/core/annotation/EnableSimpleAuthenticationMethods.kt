package nsl.sam.core.annotation

import nsl.sam.core.config.DynamicImportBeanDefinitionRegistrar
import nsl.sam.core.config.EnableWebSecurityInDebugMode
import nsl.sam.core.config.EnableWebSecurityInDefaultMode
import nsl.sam.core.config.GeneralConfiguration
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import org.springframework.context.annotation.Import
import kotlin.reflect.KClass

const val ENABLE_ANNOTATION_METHODS_ATTRIBUTE_NAME = "methods"
const val ENABLE_ANNOTATION_ANONYMOUS_FALLBACK_ATTRIBUTE_NAME = "localAnonymousFallback"
const val ENABLE_ANNOTATION_MATCH_ATTRIBUTE_NAME = "match"
const val ENABLE_ANNOTATION_DEBUG_ATTRIBUTE_NAME = "debug"
const val ENABLE_ANNOTATION_ORDER_ATTRIBUTE_NAME = "order"
const val ENABLE_ANNOTATION_AUTHORIZATIONS_ATTRIBUTE_NAME = "authorizations"

/**
 * Annotation to be used by simple-authorization-methods' consumers to enable
 * authorization methods implemented by this this library.
 *
 * By default all methods are enabled but they can be narrow down selectively with [methods] parameter.
 *
 * @param methods array of authorization methods to be enabled
 */
@Target(AnnotationTarget.CLASS)
//@Conditional(SimpleNoMethodValueIsAbsent::class)
@Import(value =
[
    DynamicImportBeanDefinitionRegistrar::class,
    GeneralConfiguration::class,
    EnableWebSecurityInDebugMode::class,
    EnableWebSecurityInDefaultMode::class
])
annotation class EnableSimpleAuthenticationMethods(

        val methods: Array<AuthenticationMethod> =
                [(AuthenticationMethod.SIMPLE_BASIC_AUTH), (AuthenticationMethod.SIMPLE_TOKEN)],

        /**
         * if there is no even one user in any UsersSource available and this attribute is set to true
         * then anonymous access to all resources is activated
         */
        val localAnonymousFallback: Boolean = false,

        /**
         * path used to define matching request with the help of
         * [org.springframework.security.config.annotation.web.builders.HttpSecurity.antMatcher]
         * examples:
         * match="/\*\*"
         */
        val match: String = "",

        val order: Int = -1,

        val authorizations: String = "",

        val forceHttps: Boolean = false,

        val debug: Boolean = false,

        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = []
)
