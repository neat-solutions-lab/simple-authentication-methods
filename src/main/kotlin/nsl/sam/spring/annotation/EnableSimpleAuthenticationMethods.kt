package nsl.sam.spring.annotation

import nsl.sam.spring.config.*
import org.springframework.context.annotation.Import

const val ENABLE_ANNOTATION_METHODS_ATTRIBUTE_NAME = "methods"
const val ENABLE_ANNOTATION_ANONYMOUS_FALLBACK_ATTRIBUTE_NAME = "localAnonymousFallback"
const val ENABLE_ANNOTATION_MATCH_ATTRIBUTE_NAME = "match"
const val ENABLE_ANNOTATION_DEBUG_ATTRIBUTE_NAME = "debug"
const val ENABLE_ANNOTATION_ORDER_ATTRIBUTE_NAME = "order"
const val ENABLE_ANNOTATION_AUTHORIZATIONS_ATTRIBUTE_NAME = "authorizations"

/**
 * Annotation to be used by simple-authentication-methods' consumers to enable
 * authentication methods implemented by this this library.
 *
 * By default all methods are enabled but they can be narrow down selectively with [methods] parameter.
 *
 * @param methods array of authentication methods to be enabled
 */
//@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Target(AnnotationTarget.CLASS)
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

        val debug: Boolean = false
)
