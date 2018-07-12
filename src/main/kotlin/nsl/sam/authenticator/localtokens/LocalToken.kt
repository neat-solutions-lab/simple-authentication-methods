package nsl.sam.authenticator.localtokens

data class LocalToken(
        val tokenValue:String,
        val userAndRole: UserAndRoles
) {
    override fun toString(): String {
        return "LocalToken([SECRET_TOKEN], ${userAndRole})"
    }
}