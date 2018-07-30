package nsl.sam.integration.basicauth

import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.ComponentScan

@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH])
@ComponentScan(basePackageClasses = [IntegrationTestController::class])
class BasicAuthITConfig