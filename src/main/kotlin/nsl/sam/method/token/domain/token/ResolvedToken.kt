package nsl.sam.method.token.domain.token

/**
 * Internal representation of individual token read from "tokens file"
 */
class ResolvedToken(
        val tokenValue: String,
        val userName: String = "",
        val roles: Array<String> = emptyArray()
) {
    override fun toString(): String {
        return "ResolvedToken([SECRET_TOKEN], $userName, $roles)"
    }
}