package nsl.sam.core.entrypoint.factory

import nsl.sam.annotation.inject.Factory
import org.springframework.security.web.AuthenticationEntryPoint

interface AuthenticationEntryPointFactory: Factory<AuthenticationEntryPoint>