package nsl.sam.method.token.tokendetails.impl

import nsl.sam.method.token.tokendetails.TokenDetailsService
import nsl.sam.method.token.token.UserAndRoles
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokenssource.impl.InMemoryTokensSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException

/**
 * With the help of TokensSource maps tokens to associated users, and more precisely to
 * structure which represents user and his roles.
 *
 * @see TokenDetailsService
 */
class DefaultTokenDetailsService : TokenDetailsService {

    @Autowired
    lateinit var inMemoryTokensSource: InMemoryTokensSource

    override fun loadUserByToken(token: String): UserAndRoles {
        val resolvedToken: ResolvedToken = inMemoryTokensSource.getResolvedToken(token)
                ?: throw BadCredentialsException("No token $token in local file")
        return resolvedToken.userAndRole
    }
}
