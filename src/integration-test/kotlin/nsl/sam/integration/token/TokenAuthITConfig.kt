package nsl.sam.integration.token

import nsl.sam.integration.basicauth.IntegrationTestController
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.ComponentScan

@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_TOKEN])
//@EnableSimpleAuthenticationMethods
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class TokenAuthITConfig