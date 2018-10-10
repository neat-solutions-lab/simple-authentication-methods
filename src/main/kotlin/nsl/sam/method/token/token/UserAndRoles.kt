package nsl.sam.method.token.token

/**
 * Internal representation of user and his roles.
 */
class UserAndRoles(val name: String, val roles: Array<String> = arrayOf("ROLE_USER"))
