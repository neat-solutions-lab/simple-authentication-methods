package nsl.sam.functional.token

import nsl.sam.functional.controller.FunctionalTestController
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Bean

@EnableSimpleAuthenticationMethods([AuthenticationMethod.SIMPLE_TOKEN])
//@EnableSimpleAuthenticationMethods
class TokenAuthFunctionalTestConfig {
    @Bean
    fun fakeController() = FunctionalTestController()
}