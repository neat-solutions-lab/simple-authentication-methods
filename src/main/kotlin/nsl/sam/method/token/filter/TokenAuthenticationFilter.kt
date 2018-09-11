package nsl.sam.method.token.filter

import nsl.sam.dto.UnauthenticatedResponseDto
import nsl.sam.logger.logger
import nsl.sam.sender.ResponseSender
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.core.AuthenticationException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val AUTHORIZATION_HEADER = "Authorization"

class TokenAuthenticationFilter(
        private val tokenAuthenticator: TokenToUserMapper,
        private val errorResponseSender: ResponseSender) : OncePerRequestFilter() {

    companion object { val log by logger() }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        log.debug("TokenAuthenticationFilter >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)
        if(null == authorizationHeader) {
            log.debug("No ($AUTHORIZATION_HEADER) header found in request")
            chain.doFilter(request, response)
            return
        }

        try {
            authorizationHeader.let {
                log.debug("Processing Authentication header")
                if(isBearerToken(authorizationHeader)) {
                    log.debug("This is Bearer token based authentication")
                    tryToAuthenticate(authorizationHeader)
                } else {
                    log.debug("Skipping Bearer token based authentication")
                }
            }
        } catch (e: AuthenticationException) {
            SecurityContextHolder.clearContext()
            log.debug("Access denied by ${this::class.qualifiedName} filter")
            errorResponseSender.send(response, UnauthenticatedResponseDto.Builder(request).build())
            return
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
        } catch (e: BadCredentialsException) {
            SecurityContextHolder.clearContext()
            log.debug("BadCredentialsException while trying to authenticate with token ${authToken}")
            throw e
        } catch (e: AuthenticationException) {
            SecurityContextHolder.clearContext()
            log.error("AuthenticationException while trying to authenticate with token ${authToken}")
            throw e
        }
    }

}