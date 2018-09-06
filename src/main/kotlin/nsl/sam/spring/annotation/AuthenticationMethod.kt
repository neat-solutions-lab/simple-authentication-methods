package nsl.sam.spring.annotation

/**
 * Available authentication methods which can be enabled with [EnableSimpleAuthenticationMethods]
 *
 * @property SIMPLE_BASIC_AUTH refers to the HTTP BasicAuth method
 * @property SIMPLE_TOKEN refers to the simplified, custom, token based authentication method
 *           implemented  by [nsl.sam.method.token.filter.TokenAuthenticationFilter]
 * @property SIMPLE_NO_METHOD indicates that no authentication method should be activated
 *
 * @see EnableSimpleAuthenticationMethods
 */
enum class AuthenticationMethod {
    SIMPLE_BASIC_AUTH, SIMPLE_TOKEN, SIMPLE_NO_METHOD
}