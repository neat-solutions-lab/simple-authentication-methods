package nsl.sam.functional.entrypointfactory

import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import org.springframework.security.web.AuthenticationEntryPoint

class FirstTestTimeEntryPointFactory : AuthenticationEntryPointFactory {
    override fun create(): AuthenticationEntryPoint {
        return FirstTestTimeEntryPoint()
    }
}