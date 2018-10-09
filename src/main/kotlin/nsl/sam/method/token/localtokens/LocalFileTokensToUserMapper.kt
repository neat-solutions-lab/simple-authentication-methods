package nsl.sam.method.token.localtokens

import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.method.token.filter.UserAndRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException

/**
 * With help of "local tokens store" maps tokens to associated users, and more precisely to
 * structure which represents user and his roles.
 *
 * @see TokenToUserMapper
 */
class LocalFileTokensToUserMapper : TokenToUserMapper {

    @Autowired
    lateinit var localTokensSource: LocalTokensSource

    override fun mapToUser(token: String): UserAndRoles {
        val localToken: LocalToken = localTokensSource.get(token)
                ?: throw BadCredentialsException("No token ${token} in local file")
        return localToken.userAndRole
    }
}