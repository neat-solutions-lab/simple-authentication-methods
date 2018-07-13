package nsl.sam.authenticator.localtokens

import org.springframework.beans.factory.annotation.Autowired
import javax.naming.AuthenticationException

/**
 * With help of "local tokens store" maps tokens to associated users, and more precisely to
 * structure which represents user and his roles.
 *
 * @see TokenToUserMapper
 */
class LocalFileTokensToUserMapper : TokenToUserMapper {

    @Autowired
    lateinit var localTokensStore: LocalTokensStore

    override fun mapToUser(token: String): UserAndRoles {
        val localToken : LocalToken = localTokensStore.get(token)
                ?: throw AuthenticationException("No token ${token} in local file")
        return localToken.userAndRole
    }
}