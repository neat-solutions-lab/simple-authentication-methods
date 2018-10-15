package nsl.sam.method.token.tokendetails.impl

import nsl.sam.logger.logger
import nsl.sam.method.token.tokendetails.TokenDetailsService
import nsl.sam.method.token.token.ResolvedToken
import nsl.sam.method.token.tokendetails.AvailabilityAwareTokenDetailsService
import nsl.sam.method.token.tokensresolver.TokensResolver
import nsl.sam.method.token.tokensresolver.impl.InMemoryTokensResolver
import nsl.sam.utils.prune
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

/**
 * With the help of TokensResolver maps tokens to associated users, and more precisely to
 * structure which represents user and his roles.
 *
 * @see TokenDetailsService
 */
class DefaultTokenDetailsService(private val tokensSource: TokensResolver) : AvailabilityAwareTokenDetailsService {

    override fun hasItems(): Boolean {
        return true
    }

    companion object {
        val log by logger()
    }

    override fun loadUserByToken(token: String): UserDetails {

        try {

            val resolvedToken: ResolvedToken = tokensSource.getResolvedToken(token)

            return User.builder()
                    .username(resolvedToken.userAndRole.name)
                    .password("")
                    .authorities(*resolvedToken.userAndRole.roles)
                    .build()

        } catch (ex: BadCredentialsException) {

            log.debug("The ${token.prune(5)} token could not be found by underlying TokensResolver")
            throw ex
        }
    }
}
