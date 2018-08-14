package nsl.sam.spring.entrypoint

import nsl.sam.dto.UnauthenticatedResponseDto
import nsl.sam.sender.ResponseSender
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SimpleFailedAuthenticationEntryPoint(val errorResponseSender: ResponseSender): AuthenticationEntryPoint {
    override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException) {

        errorResponseSender.send(response, UnauthenticatedResponseDto.Builder(request).build())

        //sender.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
    }
}