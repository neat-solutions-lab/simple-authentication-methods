package nsl.sam.authenticator.localtokens

import nsl.sam.authenticator.TokenAuthenticator
import nsl.sam.user.UserAndRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.naming.AuthenticationException

@Component
class LocalFileTokensAuthenticator : TokenAuthenticator {

    @Autowired
    lateinit var localTokensStore: LocalTokensStore

    override fun mapToUser(token: String): UserAndRoles {
        val localToken : LocalToken = localTokensStore.get(token)
                ?: throw AuthenticationException("No token ${token} in local file")
        return localToken.userAndRole
    }
}