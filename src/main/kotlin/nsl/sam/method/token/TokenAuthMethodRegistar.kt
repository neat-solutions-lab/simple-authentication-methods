package nsl.sam.method.token

import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.method.token.localtokens.TokenFileImporter
import nsl.sam.method.token.filter.TokenAuthenticationFilter
import nsl.sam.logger.logger
import nsl.sam.registar.AuthMethodRegistar
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Order(20)
class TokenAuthMethodRegistar : AuthMethodRegistar {

    companion object { val log by logger() }

    @Value("\${sam.tokens-file:}")
    lateinit var tokensFilePath: String

    @Value("\${server.address:localhost}")
    lateinit var serverAddress: String

    @Autowired
    lateinit var tokenAuthenticator : TokenToUserMapper

    override fun isActive(): Boolean {
        val tokensNumber = tokensNumber()

        log.info("tokensFilePath: $tokensFilePath")
        log.info("serverAddress: $serverAddress")
        log.info("tokensNumber: $tokensNumber")

        if(tokensFilePath.isNotBlank()) {
            if(serverAddress in arrayOf("localhost", "127.0.0.1") && tokensNumber == 0L) return false
            return true
        }
        return false
    }

    override fun register(http: HttpSecurity): HttpSecurity {
        log.info("Registering ${TokenAuthenticationFilter::class.qualifiedName} fileter.")
        return http.addFilterBefore(TokenAuthenticationFilter(tokenAuthenticator), BasicAuthenticationFilter::class.java)
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
