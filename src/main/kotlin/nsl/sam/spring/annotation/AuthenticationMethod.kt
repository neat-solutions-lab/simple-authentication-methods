package nsl.sam.spring.annotation

/**
 * Available authentication methods which can be enabled with [EnableSimpleAuthenticationMethods]
 *
 * @property SIMPLE_BASIC_AUTH refers to HTTP BasicAuth method
 * @property SIMPLE_TOKEN refers to simplified, custom, token based authentication method
 *           implemented  by [nsl.sam.method.token.filter.TokenAuthenticationFilter]
 *
 * @see EnableSimpleAuthenticationMethods
 */
enum class AuthenticationMethod {
    SIMPLE_BASIC_AUTH, SIMPLE_TOKEN
}