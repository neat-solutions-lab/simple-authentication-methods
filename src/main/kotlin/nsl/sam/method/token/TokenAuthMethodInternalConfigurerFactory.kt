package nsl.sam.method.token

import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.core.sender.ResponseSender
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.helper.AuthenticationEntryPointHelper
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment

class TokenAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {


    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    @Autowired
    lateinit var tokenAuthenticator : TokenToUserMapper

    @Autowired
    lateinit var environment: Environment

    //@Autowired
    //@Qualifier("unauthenticatedAccessResponseSender")
    //lateinit var unauthenticatedResponseSender: ResponseSender

    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_TOKEN
    }

    override fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer {

        val authenticatedEntryPoint = AuthenticationEntryPointHelper.getAuthenticationEntryPoint(
                environment, attributes.enableAnnotationMetadata,
                arrayOf(EnableSimpleAuthenticationMethods::class, SimpleTokenAuthentication::class)
        )

        return  TokenAuthMethodInternalConfigurer(
                tokensFilePath,
                tokenAuthenticator,
                //unauthenticatedResponseSender,
                authenticatedEntryPoint
        )
    }
}