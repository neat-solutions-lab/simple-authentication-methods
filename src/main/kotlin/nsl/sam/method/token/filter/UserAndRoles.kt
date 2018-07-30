package nsl.sam.method.token.filter

/**
 * Internal representation of user and his roles.
 */
data class UserAndRoles(val name:String, val roles:Array<String> = arrayOf("ROLE_USER"))
