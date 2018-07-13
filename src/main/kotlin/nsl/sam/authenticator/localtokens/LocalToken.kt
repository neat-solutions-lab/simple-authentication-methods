package nsl.sam.authenticator.localtokens

/**
 * Internal representation of individual token read from "tokens file"
 */
data class LocalToken(
        val tokenValue:String,
        val userAndRole: UserAndRoles
) {
    override fun toString(): String {
        return "LocalToken([SECRET_TOKEN], ${userAndRole})"
    }
}