package nsl.sam.dto

import org.springframework.http.HttpStatus
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest

data class UnauthenticatedResponseDto private constructor(
        val success: Boolean,
        val method: String,
        val timestamp: String,
        val status: Int,
        val error: String,
        val message: String,
        val path: String
) {

    private constructor(builder: Builder)
            : this(builder.success, builder.method, builder.timestamp, builder.status, builder.error, builder.message, builder.path)

    class Builder(httpServletRequest: HttpServletRequest) {
        var success: Boolean = false
            private set

        var method: String = httpServletRequest.method
            private set

        var timestamp: String = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now())
            private set

        var status: Int = HttpStatus.UNAUTHORIZED.value()
            private set

        var error: String = "Unauthorized"
            private set

        var message: String = "Authentication failed"
            private set

        var path: String = "${httpServletRequest.servletPath}"
            private set

        fun success(success: Boolean) = apply { this.success = success }
        fun status(method: String) = apply { this.method = method }
        fun timestamp(timestamp: String) = apply { this.timestamp = timestamp }
        fun status(status: Int) = apply { this.status = status }
        fun error(error: String) = apply { this.error = error }
        fun message(message: String) = apply { this.message = message }
        fun path(path: String) = apply { this.path = path }

        fun build() = UnauthenticatedResponseDto(this)

    }

}