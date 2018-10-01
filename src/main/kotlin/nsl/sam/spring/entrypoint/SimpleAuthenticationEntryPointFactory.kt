package nsl.sam.spring.entrypoint

import nsl.sam.spring.sender.UnauthenticatedAccessResponseSender
import org.springframework.security.web.AuthenticationEntryPoint

class SimpleAuthenticationEntryPointFactory: AuthenticationEntryPointFactory {
    override fun create(): AuthenticationEntryPoint {
        return SimpleFailedAuthenticationEntryPoint(
                UnauthenticatedAccessResponseSender()
        )
    }
}