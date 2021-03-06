package nsl.sam.core.sender

import javax.servlet.http.HttpServletResponse

interface ResponseSender {
    fun send(httpServletResponse: HttpServletResponse, responseDto: Any)
}