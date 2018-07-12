package nsl.sam.filter

import nsl.sam.authenticator.TokenAuthenticator
import nsl.sam.logger.logger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.filter.OncePerRequestFilter
import javax.naming.AuthenticationException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val AUTHORIZATION_HEADER = "Authorization"

class TokenAuthenticationFilter(val tokenAuthenticator: TokenAuthenticator) : OncePerRequestFilter() {

    companion object { val log by logger() }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        log.debug("TokenAuthenticationFilter >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)

        authorizationHeader?.let {
            log.debug("Processing Authentication header")
            if(isBearerToken(authorizationHeader)) {
                log.debug("This is Bearer token based authentication")
                tryToAuthenticate(authorizationHeader)
            } else {
                log.debug("Skipping Bearer token based authentication")
            }
        }

        chain.doFilter(request, response)

        log.debug("TokenAuthenticationFilter <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")

    }

    private fun isBearerToken(header: String) : Boolean {
        if(header.trim().startsWith("Bearer")) return true
        return false
    }

    private fun extractBearerToken(header:String) : String {
        val headerParts = header.split(" ")
        return headerParts[1]
    }

    private fun tryToAuthenticate(header:String) {

        val authToken = extractBearerToken(header)
        log.debug("authToken: ${authToken}")

        try {
            val localUser = tokenAuthenticator.mapToUser(authToken)
            log.debug("user: ${localUser}")

            val userDetails =
                    User.builder().username(localUser.name).password("").authorities(*localUser.roles).build()

            SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(
                            userDetails, "",
                            userDetails.authorities)

        } catch (e: AuthenticationException) {
            log.debug("Token ${authToken} cannot be authenticated")
        }
    }

}