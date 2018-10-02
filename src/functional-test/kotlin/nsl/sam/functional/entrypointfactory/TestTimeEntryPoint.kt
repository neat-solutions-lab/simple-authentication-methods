package nsl.sam.functional.entrypointfactory

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TestTimeEntryPoint: AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.print("Response from ${this::class.qualifiedName}")
        response.flushBuffer()
    }
}