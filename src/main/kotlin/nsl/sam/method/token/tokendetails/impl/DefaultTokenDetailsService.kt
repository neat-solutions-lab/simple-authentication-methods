package nsl.sam.method.token.tokendetails.impl

import nsl.sam.logger.logger
import nsl.sam.method.token.tokendetails.TokenDetailsService
import nsl.sam.method.token.domain.token.ResolvedToken
import nsl.sam.method.token.tokendetails.AvailabilityAwareTokenDetailsService
import nsl.sam.method.token.tokensresolver.TokensResolver
import nsl.sam.utils.prune
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

/**
 * With the help of TokensResolver maps tokens to associated users, and more precisely to
 * structure which represents user and his roles.
 *
 * @see TokenDetailsService
 */
class DefaultTokenDetailsService(private val tokensResolver: TokensResolver) : AvailabilityAwareTokenDetailsService {

    override fun hasItems(): Boolean {
        return tokensResolver.hasItems()
    }

    companion object {
        val log by logger()
    }

    override fun loadUserByToken(token: String): UserDetails {

        try {

            val resolvedToken: ResolvedToken = tokensResolver.getResolvedToken(token)

            return User.builder()
                    .username(resolvedToken.userName)
                    .password("")
                    .authorities(*resolvedToken.roles)
                    .build()

        } catch (ex: BadCredentialsException) {

            log.debug("The ${token.prune(5)} token could not be found by underlying TokensResolver")
            throw ex
        }
    }
}
