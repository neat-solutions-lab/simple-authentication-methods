package nsl.sam.registrant

import nsl.sam.authenticator.TokenAuthenticator
import nsl.sam.authenticator.localtokens.TokenFileImporter
import nsl.sam.filter.TokenAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import java.io.File

@Component
@Order(20)
class TokenAuthMethodRegistrant : AuthMethodRegistrant {

    @Value("\${sms.tokens-file:}")
    lateinit var tokensFilePath: String

    @Value("\${server.address:localhost}")
    lateinit var serverAddress: String

    @Autowired
    lateinit var tokenAuthenticator : TokenAuthenticator

    override fun isActive(): Boolean {
        if(tokensFilePath.isNotBlank()) {
            if(serverAddress in arrayOf("localhost", "127.0.0.1") && tokensNumber() == 0L) return false
            return true
        }
        return false
    }

    override fun register(http: HttpSecurity): HttpSecurity {
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
