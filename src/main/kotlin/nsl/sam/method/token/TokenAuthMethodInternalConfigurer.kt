package nsl.sam.method.token

import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.method.token.localtokens.TokenFileImporter
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.logger.logger
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.core.sender.ResponseSender
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class TokenAuthMethodInternalConfigurer(
        private val tokensFilePath: String,
        private val tokenAuthenticator : TokenToUserMapper,
        //private val unauthenticatedResponseSender: ResponseSender,
        private val authenticationEntryPoint: AuthenticationEntryPoint) : AuthMethodInternalConfigurer {

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
        //log.info("serverAddress: $serverAddress")
        log.info("tokensNumber: $tokensNumber")

        if(tokensNumber == 0L) return false

        //if(serverAddress in arrayOf("localhost", "127.0.0.1") && tokensNumber == 0L) return false
        return true
    }

    override fun isAvailable(): Boolean {
        if (!isActiveVariableCalculated) {
            isActiveValue = isActiveInternal()
            isActiveVariableCalculated = true
        }
        return isActiveValue
    }

    override fun configure(http: HttpSecurity): HttpSecurity {
        log.info("Registering ${TokenAuthenticationFilter::class.qualifiedName} filter.")
        return http.addFilterBefore(
                //TokenAuthenticationFilter(tokenAuthenticator, unauthenticatedResponseSender, authenticationEntryPoint),
                TokenAuthenticationFilter(tokenAuthenticator,  authenticationEntryPoint),
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