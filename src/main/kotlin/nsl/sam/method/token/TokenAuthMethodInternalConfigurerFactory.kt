package nsl.sam.method.token

import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.core.entrypoint.factory.DefaultAuthenticationEntryPointFactory
import nsl.sam.annotation.inject.InjectedObjectsProvider
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment

class TokenAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {


    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    @Autowired
    lateinit var tokenAuthenticator : TokenToUserMapper

    @Autowired
    lateinit var environment: Environment

    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_TOKEN
    }

    override fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer {

        val authenticatedEntryPoint = InjectedObjectsProvider.getObject(
                "authenticationEntryPointFactory",
                "nsl.sam.authentication-entry-point.factory",
                listOf(EnableSimpleAuthenticationMethods::class, SimpleTokenAuthentication::class),
                attributes.enableAnnotationMetadata,
                environment,
                AuthenticationEntryPointFactory::class,
                DefaultAuthenticationEntryPointFactory::class
        )

        return  TokenAuthMethodInternalConfigurer(
                tokensFilePath,
                tokenAuthenticator,
                authenticatedEntryPoint
        )
    }
}