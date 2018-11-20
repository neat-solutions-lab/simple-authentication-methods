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
import nsl.sam.method.token.tokendetails.AvailabilityAwareTokenDetailsService
import nsl.sam.method.token.tokendetails.impl.DefaultTokenDetailsService
import nsl.sam.method.token.tokensresolver.TokensResolver
import nsl.sam.method.token.tokensresolver.TokensResolverFactory
import nsl.sam.method.token.tokensresolver.factory.InMemoryTokensResolverFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.security.web.AuthenticationEntryPoint

class TokenAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {

    @Autowired
    lateinit var environment: Environment

    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_TOKEN
    }

    override fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer {

        return TokenAuthMethodInternalConfigurer(
                getTokenDetailsService(attributes),
                getAuthenticationEntryPoint(attributes)
        )
    }

    private fun getAuthenticationEntryPoint(attributes: EnableAnnotationAttributes): AuthenticationEntryPoint {
        return InjectedObjectsProvider.Builder(AuthenticationEntryPointFactory::class)
                .attributeName("authenticationEntryPointFactory")
                .defaultFactoryPropertyName("nsl.sam.authentication-entry-point.factory")
                .involvedAnnotationTypes(listOf(EnableSimpleAuthenticationMethods::class, SimpleTokenAuthentication::class))
                .annotationMetadata(attributes.enableAnnotationMetadata)
                .environment(environment)
                .defaultFactory(DefaultAuthenticationEntryPointFactory::class)
                .build().getObject()
    }

    private fun getTokenDetailsService(attributes: EnableAnnotationAttributes): AvailabilityAwareTokenDetailsService {
        val tokensResolver = getTokensResolver(attributes)
        return DefaultTokenDetailsService(tokensResolver)
    }

    private fun getTokensResolver(attributes: EnableAnnotationAttributes): TokensResolver {
        val resolverFactory = getTokensResolverFactory()
        return resolverFactory.create(attributes, environment)
    }

    private fun getTokensResolverFactory(): TokensResolverFactory {
        return InMemoryTokensResolverFactory()
    }

}