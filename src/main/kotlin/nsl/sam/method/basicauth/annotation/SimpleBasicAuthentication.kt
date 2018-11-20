package nsl.sam.method.basicauth.annotation

import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import kotlin.reflect.KClass

/**
 * This annotation, if used, always accompanies the
 * [nsl.sam.core.annotation.EnableSimpleAuthenticationMethods] annotation. It is used to
 * point out the source of the authenticated users list and optionally to customize the
 * [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
 * used in the case when the received HTTP Basic Auth credentials cannot be authenticated.
 *
 * There are three different types of the sources of the users list but there is always only one list which is selected and used.
 * The rule is that the first found source wins.
 *
 * The assumption is that all the types of the users sources provide the list of the users in the same format. Each user
 * and the associated credentials are encoded in one line of the text. The individual text line which encodes one user has the
 * following format:
 *
 * ```
 * &lt;user-name&gt;:{&lt;pass-enc-alg&gt;}&lt;password&gt; &#91;&lt;role1&gt; &lt;role2&gt; .... &lt;roleN&gt;&#93
 * ```
 * Where the meaning of the placeholders is as follows:
 *
 * `&lt;user-name&gt;` is the name of the user.
 *
 * `&lt;pass-enc-alg&gt;` is the symbol of the algorithm used to encode the consecutive password
 * (it is required from Spring Security 5 to enclose this information,
 * see [here](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/reference/htmlsingle/#core-services-password-encoding)).
 *
 * `&lt;password&gt;` is the password encoded with the algorithm determined by &lt;pass-enc-alg&gt;.
 *
 * `&#91;&lt;role1&gt; &lt;role2&gt; .... &lt;roleN&gt;&#93;` is the optional list of the user roles
 * (or the granted authorities in Spring Security lingo)
 *
 * The available types of the users sources are enlisted below in the order they are looked for:
 *
 * - _File based users list._ The file containing the text lines with the users can be declared in a few ways.
 * First, the file is being searched under the path determined by the `passwordsFilePath` attribute value
 * (if it is set). Second, the `passwordsFilePathProperty` attribute is read and if it is set, its value is
 * considered to be the name of the application property which, in turn, contains the path to the file. Finally,
 * the value of the `nsl.sam.passwords-file` application property is read and if it is defined the
 * assumption is that its value determines the path to the file.
 *
 * - _Environment variables based user list._ The list of the users can be read from the environment variables.
 * The `usersEnvPrefix` attribute declares the name prefix of the environment variables whose values are
 * treated as individual lines which encode the user, password and roles.
 *
 * - _Annotation's attribute based user list._ Finally, the list of the text lines with the users is read from the
 * array hold in the `users` annotation attribute.
 *
 * Simple usage examples showing how to get the users list from the diverse sources can be found in these two projects:
 * - [kotlin-sam-example](https://github.com/neat-solutions-lab/kotlin-sam-exqample)
 * - [java-sam-example](https://github.com/neat-solutions-lab/java-sam-exqample)
 */
@Target(AnnotationTarget.CLASS)
annotation class SimpleBasicAuthentication(

        /**
         * The path to the file with the users and passwords.
         */
        val passwordsFilePath: String = "",

        /**
         * The name of the application property which should hold the path to the file with the users and passwords.
         */
        val passwordsFilePropertyName: String = "",

        /**
         *  If set to true, the file with the users and passwords will be monitored for changes (with one second frequency).
         *  In the case the file changes, it is reread in order to refresh the list of the authenticated users.
         */
        val detectPasswordsFileChanges: Boolean = false,

        /**
         * The name prefix of the environment variables which hold the text line with the encoded user, password and roles. Each
         * environment variable, whose name begins with this prefix, is expected to define one user and associated credentials.
         */
        val usersEnvPrefix: String = "",

        /**
         * Allows the developer to hardcode the list of the users into the source code. Each item of this array is
         * treated as the text line which adheres to the format described in this documentation and which defines
         * the user, password and roles (authorities).
         */
        val users: Array<String> = [],

        /**
         * This attribute can be used to replace the
         * [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
         * being used with HTTP Basic Auth with
         * the custom one. This attribute takes the form of an array but only the first item in the array is relevant.
         * Additional items, if present, are ignored. If the array is not empty then the AuthenticationEntryPoint
         * created by the provided factory will serve unauthenticated requests adhering to the HTTP Basic Auth authentication
         * schema.
         */
        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = []
)