package nsl.sam.core.annotation

/**
 * Available authentication methods which can be enabled with help [EnableSimpleAuthenticationMethods] annotation. The
 * annotation takes in the `methods` attribute which takes form of the array of this enum elements. To enable the specific
 * authentication method, the corresponding enum value has to be passed through the `methods` array to the
 * [EnableSimpleAuthenticationMethods] annotation.
 *
 *
 *
 * @property SIMPLE_BASIC_AUTH refers to the HTTP Basic Auth method
 * @property SIMPLE_TOKEN refers to the simplified, custom, token based authorization method
 *           implemented  by [nsl.sam.method.token.filter.TokenAuthenticationFilter]
 * @property SIMPLE_NO_METHOD indicates that no authorization method should be activated
 *
 * @see EnableSimpleAuthenticationMethods
 */
enum class AuthenticationMethod {
    SIMPLE_BASIC_AUTH, SIMPLE_TOKEN, SIMPLE_NO_METHOD
}