package nsl.sam.core.entrypoint

import nsl.sam.core.sender.UnauthenticatedAccessResponseSender
import org.springframework.security.web.AuthenticationEntryPoint

class SimpleAuthenticationEntryPointFactory: AuthenticationEntryPointFactory() {
    override fun create(): AuthenticationEntryPoint {

        return SimpleFailedAuthenticationEntryPoint(UnauthenticatedAccessResponseSender())

    }
}