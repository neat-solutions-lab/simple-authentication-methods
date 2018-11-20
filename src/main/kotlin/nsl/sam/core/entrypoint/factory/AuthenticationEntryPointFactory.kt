package nsl.sam.core.entrypoint.factory

import nsl.sam.annotation.inject.Factory
import org.springframework.security.web.AuthenticationEntryPoint

/**
 * Factory which creates a custom Spring Security
 * [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html).
 *
 * KClass of this factory can be passed thorough the `authenticationEntryPointFactory` attribute to one
 * of these annotations:
 *
 * - [nsl.sam.core.annotation.EnableSimpleAuthenticationMethods]
 * - [nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication]
 * - [nsl.sam.method.token.annotation.SimpleTokenAuthentication]
 *
 * Custom [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
 * can be used to change the response the server sends to the client in the
 * case of the unauthenticated access.
 *
 */
interface AuthenticationEntryPointFactory : Factory<AuthenticationEntryPoint>
