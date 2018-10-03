package nsl.sam.method.token

import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.core.sender.ResponseSender
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value

class TokenAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {

    @Value("\${server.address:localhost}")
    lateinit var serverAddress: String

    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    @Autowired
    lateinit var tokenAuthenticator : TokenToUserMapper

    @Autowired
    @Qualifier("unauthenticatedAccessResponseSender")
    lateinit var unauthenticatedResponseSender: ResponseSender

    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_TOKEN
    }

    override fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer {


        return  TokenAuthMethodInternalConfigurer(
                tokensFilePath,
                serverAddress,
                tokenAuthenticator,
                unauthenticatedResponseSender
        )
    }
}