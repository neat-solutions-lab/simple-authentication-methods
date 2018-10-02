package nsl.sam.core.entrypoint

import org.springframework.security.web.AuthenticationEntryPoint

interface AuthenticationEntryPointFactory {
    fun create(): AuthenticationEntryPoint
}