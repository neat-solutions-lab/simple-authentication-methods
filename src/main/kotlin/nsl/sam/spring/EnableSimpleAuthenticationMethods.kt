package nsl.sam.spring

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
@Import(SimpleAuthenticationMethodsSelector::class)
annotation class EnableSimpleAuthenticationMethods(
        val methods: Array<AuthenticationMethod> =
                [AuthenticationMethod.SIMPLE_BASIC_AUTH, AuthenticationMethod.SIMPLE_TOKEN]
)
