package nsl.sam.method.token.filter

/**
 * To be implemented by class which will map token (probably acquired from HTTP header) to
 * internal representation of associated user and his roles.
 */
interface TokenToUserMapper {

    /**
     * Maps given [token] value to underlying user (in form of [UserAndRoles] data class).
     *
     * @param token Token value which the method will try to map to associated user (and his roles).
     *
     * @throws javax.naming.AuthenticationException  Exception being thrown when given [token] doesn't map
     *         to any existing user.
     */
    fun mapToUser(token: String): UserAndRoles
}