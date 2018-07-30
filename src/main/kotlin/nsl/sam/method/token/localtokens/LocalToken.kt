package nsl.sam.method.token.localtokens

import nsl.sam.method.token.filter.UserAndRoles

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