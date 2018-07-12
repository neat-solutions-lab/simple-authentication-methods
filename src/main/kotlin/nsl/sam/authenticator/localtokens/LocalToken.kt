package nsl.sam.authenticator.localtokens

import nsl.sam.user.UserAndRoles

data class LocalToken(
        val tokenValue:String,
        val userAndRole:UserAndRoles
) {
    override fun toString(): String {
        return "LocalToken([SECRET_TOKEN], ${userAndRole})"
    }
}