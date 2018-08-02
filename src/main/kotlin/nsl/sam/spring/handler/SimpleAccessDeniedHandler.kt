package nsl.sam.spring.handler

import nsl.sam.logger.logger
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SimpleAccessDeniedHandler: AccessDeniedHandler {

    companion object { val log by logger() }

    override fun handle(
            request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {

        response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        // todo: dodaj generowanie JSON (ObjectMapper)
        response.writer.print("TUTAJ ZNOWU JAKIS GENEROWANY JSON")
        response.flushBuffer()
    }

}