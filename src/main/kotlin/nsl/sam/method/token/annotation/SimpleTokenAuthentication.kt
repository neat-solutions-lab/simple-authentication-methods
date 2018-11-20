package nsl.sam.method.token.annotation

import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import kotlin.reflect.KClass

/**
 * This annotation, if used, always accompanies the
 * [nsl.sam.core.annotation.EnableSimpleAuthenticationMethods] annotation. It is meant
 * to point out the source of the authenticated tokens and optionally to customize the
 * [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
 * used by the token based authentication mechanism implemented by the simple-authentication-methods library.
 *
 * There are three different types of the token sources but only one type can be selected and used at the same time.
 * The different source types are searched for in a defined order, and the first found and active source type wins.
 * Whereby, as active is considered the source which can provide at least one valid token.
 *
 * All the types of the tokens sources are able to provide the list of the legitimate tokens in the same format. Each token and the
 * associated credentials are encoded in one text line of the following format:
 *
 * ```
 * &lt;token-value&gt; &lt;user-name&gt; [&lt;role1&gt; &lt;role2&gt; ... &lt;rolneN&gt;]
 * ```
 *
 * Where the meaning of the placeholders is as follows:
 *
 * `&lt;token-value&gt;` is the exact value of the authenticated token.
 *
 * `&lt;user-name&gt;` is the name of the user the token is mapped to.
 *
 * `&#91;&lt;role1&gt; &lt;role2&gt; ... &lt;rolneN&gt;&#93;` is the optional list of the user roles the token is associated with
 * (or the granted authorities in Spring Security lingo)
 *
 * The list of the available types of the tokens sources is enlisted below in the order they are looked for:
 *
 * - _File based tokens list_. The list of the authenticated tokens is read from a file. The file itself can be located
 * in a few ways. First, the path determined by the value of the `tokensFilePath` attribute is checked. Next, the
 * `tokensFilePathProperty` attribute is read. The value of this attribute is interpreted as the name of the application property
 * which holds the path to the file. Finally, the application property `nsl.sam.tokens-file` is read. The
 * value of this property is assumed to be the path to the file with the tokens.
 *
 * - _Environment variables based tokens list._ The list of the tokens can be read from the environment variables.
 * The `tokensEnvPrefix` attribute declares the name prefix of the environment variables whose values are
 * interpreted as individual lines with the encoded tokens and associated credentials.
 *
 * - _Annotation's attribute based tokens list._ If no found elsewhere, the list of the text lines with the tokens
 * is taken from the `tokens` attribute. This attribute is an array of String(s), and each item is considered as
 * a separate text line with encoded token and associated credentials.
 *
 * Simple usage examples showing how to get the tokens list from the diverse sources can be found in these two projects:
 * - [kotlin-sam-example](https://github.com/neat-solutions-lab/kotlin-sam-exqample)
 * - [java-sam-example](https://github.com/neat-solutions-lab/java-sam-exqample)
 */
@Target(AnnotationTarget.CLASS)
annotation class SimpleTokenAuthentication (

        /**
         * The path to the file with the tokens.
         */
        val tokensFilePath: String = "",

        /**
         * The name of the application property which holds the path to the file with tokens.
         */
        val tokensFilePropertyName: String = "",

        /**
         * If set to true, the tokens file will be monitored for changes (with one second rate). If a change is detected
         * then the list of the authenticated tokens used by server is reloaded.
         */
        val detectTokensFileChanges: Boolean = false,

        /**
         * The name prefix of the environment variables which hold the text line with the encoded token. Each environment
         * variable, whose name begins with this prefix, defines one legitimate token and the associated credentials
         * (see annotation level documentation).
         */
        val tokensEnvPrefix: String = "",

        /**
         * Allows the developer to hardcode the list of the authenticated tokens. Each item in the array is interpreted
         * as the text line with the encoded token and the associated credentials (the user name of the user the token is mapped to,
         * and optionally roles assigned to this user).
         */
        val tokens: Array<String> = [],

        /**
         * This attribute can be used to set the custom
         * [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
         * being used by the token based authentication
         * method when the unauthenticated token is recognized. This attribute takes the form of an array but only the
         * first item in the array is relevant. Additional items, if present, are ignored.
         * If the array is not empty then the AuthenticationEntryPoint created by the provided factory will
         * handle the requests containing the unauthenticated tokens.
         */
        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = []
)