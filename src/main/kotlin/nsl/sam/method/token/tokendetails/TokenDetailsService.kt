package nsl.sam.method.token.tokendetails

import nsl.sam.method.token.token.UserAndRoles
import org.springframework.security.core.userdetails.UserDetails

/**
 * To be implemented by class which will map token (probably acquired from HTTP header) to
 * internal representation of associated user and his roles.
 */
interface TokenDetailsService {

    /**
     * Maps given [token] value to underlying user (in form of [UserAndRoles] data class).
     *
     * @param token Token value which the method will try to map to associated user (and his roles).
     *
     * @throws javax.naming.AuthenticationException  Exception being thrown when given [token] doesn't map
     *         to any existing user.
     */
    //fun loadUserByToken(token: String): UserAndRoles
    fun loadUserByToken(token: String): UserDetails
}