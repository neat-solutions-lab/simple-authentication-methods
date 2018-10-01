package nsl.sam.spring.entrypoint

import org.springframework.security.web.AuthenticationEntryPoint

interface AuthenticationEntryPointFactory {
    fun create(): AuthenticationEntryPoint
}