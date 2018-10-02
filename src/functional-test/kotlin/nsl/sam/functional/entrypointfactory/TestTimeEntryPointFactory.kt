package nsl.sam.functional.entrypointfactory

import nsl.sam.core.entrypoint.AuthenticationEntryPointFactory
import org.springframework.security.web.AuthenticationEntryPoint

class TestTimeEntryPointFactory: AuthenticationEntryPointFactory() {
    override fun create(): AuthenticationEntryPoint {
        return TestTimeEntryPoint()
    }
}