package nsl.sam.method.token

import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.method.token.localtokens.TokenFileImporter
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.logger.logger
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.spring.sender.ResponseSender
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class TokenAuthMethodInternalConfigurer(
        val tokensFilePath: String,
        val serverAddress: String,
        val tokenAuthenticator : TokenToUserMapper,
        val unauthenticatedResponseSender: ResponseSender) : AuthMethodInternalConfigurer {
    override fun configure(auth: AuthenticationManagerBuilder) {
        // empty, the TokenAuthenticationFiler doesn't use AunthenticationManager
    }

    companion object { val log by logger() }

    private var isActiveVariableCalculated = false

    private var isActiveValue = false
    private fun isActiveInternal(): Boolean {

        if (tokensFilePath.isBlank()) {
            log.info("Simple Token Authentication method is not active. Path to tokens file is blank.")
            return false
        }

        val tokensNumber = tokensNumber()

        log.info("tokensFilePath: $tokensFilePath")
        log.info("serverAddress: $serverAddress")
        log.info("tokensNumber: $tokensNumber")

        if(serverAddress in arrayOf("localhost", "127.0.0.1") && tokensNumber == 0L) return false
        return true
    }

    override fun isActive(): Boolean {
        if (!isActiveVariableCalculated) {
            isActiveValue = isActiveInternal()
            isActiveVariableCalculated = true
        }
        return isActiveValue
    }

    override fun isAvailable(): Boolean {
        return true
    }

    override fun configure(http: HttpSecurity): HttpSecurity {
        log.info("Registering ${TokenAuthenticationFilter::class.qualifiedName} filter.")
        return http.addFilterBefore(
                TokenAuthenticationFilter(tokenAuthenticator, unauthenticatedResponseSender),
                BasicAuthenticationFilter::class.java)
    }

    override fun methodName(): String {
        return "Local Token Method"
    }

    private fun tokensNumber() : Long {
        val tokenFileImporter = TokenFileImporter(tokensFilePath)

        var tokensCounter = 0L

        while(tokenFileImporter.hasNext()) {
            tokensCounter++
            tokenFileImporter.next()
        }
        return tokensCounter
    }
}
