package nsl.sam.functional.debugmode

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

//@EnableWebSecurity(debug = true)
@EnableSimpleAuthenticationMethods(debug = true)
class SingleEnableSimpleAuthenticationMethodsWithDebugModeFunctionalTestConfiguration