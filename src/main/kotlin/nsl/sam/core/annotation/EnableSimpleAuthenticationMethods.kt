package nsl.sam.core.annotation

import nsl.sam.core.annotation.attrtypes.PortsMapping
import nsl.sam.core.condition.SimpleNoMethodValueIsAbsent
import nsl.sam.core.config.DynamicImportBeanDefinitionRegistrar
import nsl.sam.core.config.EnableWebSecurityInDebugMode
import nsl.sam.core.config.EnableWebSecurityInDefaultMode
import nsl.sam.core.config.GeneralConfiguration
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import kotlin.reflect.KClass

const val ENABLE_ANNOTATION_METHODS_ATTRIBUTE_NAME = "methods"
const val ENABLE_ANNOTATION_ANONYMOUS_FALLBACK_ATTRIBUTE_NAME = "localAnonymousFallback"
const val ENABLE_ANNOTATION_MATCH_ATTRIBUTE_NAME = "match"
const val ENABLE_ANNOTATION_DEBUG_ATTRIBUTE_NAME = "debug"
const val ENABLE_ANNOTATION_ORDER_ATTRIBUTE_NAME = "order"
const val ENABLE_ANNOTATION_AUTHORIZATIONS_ATTRIBUTE_NAME = "authorizations"
const val ENABLE_ANNOTATION_FORCE_HTTPS_ATTRIBUTE_NAME = "forceHttps"

/**
 * This annotation is the main "entry point" to the simple-authorization-methods library.
 * It activates the individual authentication methods and configures the attributes shared by all of them.
 *
 * Apart from some shared configuration attributes, the individual authentication methods have also some specific
 * configuration attributes. Actually, these specific attributes mostly are meant to point out the source of
 * the list of the authorized users (or the tokens together with the mappings to the user names).
 * To take control over these more specific attributes, the library provides one additional
 * configuration annotation dedicated to each supported authentication method.
 *
 * As for now, this library supports two authentication methods (so there are two additional configuration annotations):
 *
 * - [nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication]
 *   This annotation is used to configure the attributes specific to the HTTP Basic Auth based authentication method.
 *
 * - [nsl.sam.method.token.annotation.SimpleTokenAuthentication]
 *   This annotation is used to configure the attributes specific to the custom, hardcoded token based authentication method.
 *
 * By default this annotation enables all the authentication methods, but they can be narrowed down selectively with the
 * `methods` attribute.
 *
 * @since 1.0.0
 */
@Target(AnnotationTarget.CLASS)
@Conditional(SimpleNoMethodValueIsAbsent::class)
@PropertySource("classpath:sam.properties")
@Import(value =
[
    DynamicImportBeanDefinitionRegistrar::class,
    GeneralConfiguration::class,
    EnableWebSecurityInDebugMode::class,
    EnableWebSecurityInDefaultMode::class
])
annotation class EnableSimpleAuthenticationMethods(

        /**
         * Array of [AuthenticationMethod] type values which indicate what authentication methods are to be enabled.
         *
         * Possible values are:
         *
         * [AuthenticationMethod.SIMPLE_BASIC_AUTH] - Enables the HTTP Basic Auth based authentication method, so the
         * user name and the password will be required to authenticate. To find out how to provide the list of the
         * authenticated users, refer to the [nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication]
         * annotation documentation.
         *
         * [AuthenticationMethod.SIMPLE_TOKEN] - Enables authentication based on the simple Bearer tokens. The
         * assumption is that each HTTP requests contains the Authentication header with a secret token.
         * The token is not encoded in any specific way, the only premise is that the server knows the exact value
         * of the token upfront. To find out how to provide the list of the valid tokens and how to map them to
         * the user names, refer to the [nsl.sam.method.token.annotation.SimpleTokenAuthentication] annotation documentation.
         *
         * [AuthenticationMethod.SIMPLE_NO_METHOD] - Disables all the methods. By default, when no method is specified,
         * all the available methods are activated. Therefore, the AuthenticationMethod.SIMPLE_NO_METHOD value can be
         * used to disable all the authentication methods temporarily (for example in development environment).
         */
        val methods: Array<AuthenticationMethod> =
                [AuthenticationMethod.SIMPLE_BASIC_AUTH, AuthenticationMethod.SIMPLE_TOKEN],

        /**
         * If this attribute is set to true, then all the requests are authenticated as an anonymous user if the following
         * conditions are met.
         *
         * First, there is no even one user available in the sources configured by either
         * [nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication] or
         * [nsl.sam.method.token.annotation.SimpleTokenAuthentication] (depending on what the authentication methods
         * are enabled).
         *
         * Second, the application is explicitly configured to listen only on the local loopback interface
         * (the value of the server.address application property is set to 127.0.0.1 or to the localhost).
         */
        val localAnonymousFallback: Boolean = false,

        /**
         * The match attribute defines the pattern that the path part of the request has to match in order to be "protected"
         * by the authentication methods enabled by this annotation.
         *
         * The format of the match pattern has to adhere to so called ant pattern used in Spring Security:
         * [org.springframework.security.config.annotation.web.builders.HttpSecurity.antMatcher](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#antMatcher-java.lang.String-)
         *
         * Example usage:
         *
         * ```
         * match="/admin-area/&#42;&#42;"
         * ```
         */
        val match: String = "",

        /**
         * Defines the order in which this annotation will be processed by the the internal mechanisms.
         * It can be meaningful if the annotation is applied multiple times (the multiple usage of this annotation is possible
         * thanks to the `match` attribute).
         */
        val order: Int = -1,

        /**
         * This attribute can be used to optionally define the authorization rules, that is to specify the roles
         * the user has to have in order to be authorized.
         *
         * The value of this attribute takes the form of the chain of the methods added to the Spring Security
         * org.springframework.security.config.annotation.web.builders.HttpSecurity#authorizeRequests
         * method as it is described here:
         * [https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/#jc-authorize-requests](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/#jc-authorize-requests)
         *
         * Example usage:
         *
         * ```
         * authorizations="antMatchers('/user-area/&#42;&#42;').hasRole('USER').antMatchers('/admin-area/&#42;&#42;').hasRole('ADMIN')"
         * ```
         */
        val authorizations: String = "",

        /**
         * If set to true, only the requests received via the HTTPS channel are accepted. Otherwise, the client is
         * redirected to the HTTPS channel.
         *
         * In the case of redirection, the port number of the secure channel has to be determined. If the port number is
         * different from the default one, the `portMapping` attribute can be used to provide information about the
         * secure channel's port number.
         */
        val forceHttps: Boolean = false,

        /**
         * Helps to determine the port number to which the client is redirected if the `forceHttps` attribute is set to true
         * and the request comes in through the non secure HTTP protocol.
         *
         * It is an array of KClass(es) of the
         * [PortsMapping]
         * interface instances. The interface defines only one function
         * which should return an instance of the
         * [Pair<Int, Int>](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html).
         * The first Int in this Pair indicates the port number of the
         * insecure channel and the second Int determines the port number of the secure channel to which the
         * client will be redirected.
         *
         */
        val portMapping: Array<KClass<out PortsMapping>> = [],

        /**
         * This attribute is directly passed to the underlying EnableWebSecurity Spring Security annotation.
         * If set to true, Spring Security prints out some additional debugging messages.
         */
        val debug: Boolean = false,

        /**
         * Allows the developer to customize the response sent in the case of the failed authentication.
         *
         * Even though the attribute is declared as an array, only the first element in the array
         * will be taken into account.
         *
         * The type of the element in the array is KClass of [AuthenticationEntryPointFactory]. This factory is
         * responsible for creating the instances of the regular Spring Security
         * [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
         *
         * The
         * [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
         * created by the provided factory will be
         * used to send a response to the client in the following cases:
         *
         * - Neither of the enabled authentication methods is able to decide if the request is authenticated or not and
         *   the anonymous authentication is not in place.
         *
         * - The HTTP Basic Auth based configuration method recognizes the invalid credentials and there is no
         *   custom
         *   [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
         *   associated with the HTTP
         *   Basic Auth method (such customization can be done with the
         *   [nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication] annotation).
         *
         * - The token based authentication method recognizes the invalid token and there is no
         *   custom [org.springframework.security.web.AuthenticationEntryPoint] associated with the token based
         *   method (such customization can be done with the
         *   [nsl.sam.method.token.annotation.SimpleTokenAuthentication] annotation).
         *
         * NOTE: The authorization error response can also be customized but there is an assumption that the
         * whole application, regardless of the used authentication method or requested path, is handled by the
         * same Spring Security
         * [AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/access/AccessDeniedHandler.html).
         * Therefore, in order to use one custom
         * [AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/access/AccessDeniedHandler.html)
         * it is sufficient to provide it in the application context, that is to define a bean of
         * [AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/access/AccessDeniedHandler.html)
         * type.
         */
        val authenticationEntryPointFactory: Array<KClass<out AuthenticationEntryPointFactory>> = []
)
