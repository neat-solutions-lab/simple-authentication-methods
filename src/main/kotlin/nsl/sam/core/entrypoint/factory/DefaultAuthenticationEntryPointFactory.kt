package nsl.sam.core.entrypoint.factory

import nsl.sam.core.entrypoint.SimpleFailedAuthenticationEntryPoint
import nsl.sam.core.sender.UnauthenticatedAccessResponseSender
import org.springframework.security.web.AuthenticationEntryPoint

class DefaultAuthenticationEntryPointFactory: AuthenticationEntryPointFactory {
    override fun create(): AuthenticationEntryPoint {
        return SimpleFailedAuthenticationEntryPoint(UnauthenticatedAccessResponseSender())
    }
}