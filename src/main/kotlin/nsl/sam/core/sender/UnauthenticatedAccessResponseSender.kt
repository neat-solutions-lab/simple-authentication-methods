package nsl.sam.core.sender

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import javax.servlet.http.HttpServletResponse

class UnauthenticatedAccessResponseSender: ResponseSender {

    private val objectMapper: ObjectMapper = Jackson2ObjectMapperBuilder.json().build()

    override fun send(httpServletResponse: HttpServletResponse, responseDto: Any) {
        httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
        httpServletResponse.writer.print(objectMapper.writeValueAsString(responseDto))
        httpServletResponse.flushBuffer()
    }
}