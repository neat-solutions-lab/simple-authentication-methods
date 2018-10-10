package nsl.sam.method.token.tokendetails.impl

import nsl.sam.logger.logger
import nsl.sam.method.token.tokendetails.TokenDetailsService
import nsl.sam.method.token.token.UserAndRoles
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokenssource.impl.InMemoryTokensSource
import nsl.sam.utils.prune
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException

/**
 * With the help of TokensSource maps tokens to associated users, and more precisely to
 * structure which represents user and his roles.
 *
 * @see TokenDetailsService
 */
class DefaultTokenDetailsService : TokenDetailsService {

    companion object {
        val log by logger()
    }

    @Autowired
    lateinit var inMemoryTokensSource: InMemoryTokensSource

    override fun loadUserByToken(token: String): UserAndRoles {

        try {

            val resolvedToken: ResolvedToken = inMemoryTokensSource.getResolvedToken(token)
            return resolvedToken.userAndRole

        } catch ( ex: BadCredentialsException) {

            log.debug("The ${token.prune(5)} token could not be found by underlying TokensSource")
            throw ex
        }
    }
}
