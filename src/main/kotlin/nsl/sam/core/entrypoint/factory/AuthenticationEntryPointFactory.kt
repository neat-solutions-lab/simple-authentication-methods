package nsl.sam.core.entrypoint.factory

import org.springframework.security.web.AuthenticationEntryPoint

interface AuthenticationEntryPointFactory: Factory<AuthenticationEntryPoint> {

    //fun create(): AuthenticationEntryPoint
}