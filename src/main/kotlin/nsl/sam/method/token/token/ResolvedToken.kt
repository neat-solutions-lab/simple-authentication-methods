package nsl.sam.method.token.token

/**
 * Internal representation of individual token read from "tokens file"
 */
data class ResolvedToken(
        val tokenValue: String,
        val userAndRole: UserAndRoles
) {
    override fun toString(): String {
        return "ResolvedToken([SECRET_TOKEN], $userAndRole)"
    }
}