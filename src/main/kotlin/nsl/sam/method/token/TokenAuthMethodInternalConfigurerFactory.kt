package nsl.sam.method.token

import nsl.sam.annotation.inject.InjectedObjectsProvider
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.core.entrypoint.factory.DefaultAuthenticationEntryPointFactory
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import nsl.sam.method.token.tokendetails.TokenDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment

class TokenAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {


    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    @Autowired
    lateinit var tokenAuthenticator: TokenDetailsService

    @Autowired
    lateinit var environment: Environment

    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_TOKEN
    }

    override fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer {

        val authenticatedEntryPoint = InjectedObjectsProvider.Builder(AuthenticationEntryPointFactory::class)
                .attributeName("authenticationEntryPointFactory")
                .defaultFactoryPropertyName("nsl.sam.authentication-entry-point.factory")
                .involvedAnnotationTypes(listOf(EnableSimpleAuthenticationMethods::class, SimpleTokenAuthentication::class))
                .annotationMetadata(attributes.enableAnnotationMetadata)
                .environment(environment)
                .defaultFactory(DefaultAuthenticationEntryPointFactory::class)
                .build().getObject()

        return TokenAuthMethodInternalConfigurer(
                tokensFilePath,
                tokenAuthenticator,
                authenticatedEntryPoint
        )
    }
}