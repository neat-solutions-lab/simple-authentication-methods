package nsl.sam.functional.basicauth

import nsl.sam.functional.controller.FunctionalTestController
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Bean

@EnableSimpleAuthenticationMethods([AuthenticationMethod.SIMPLE_BASIC_AUTH])
class NarrowConfBasicAuthFunctionalTestConfig {
    @Bean
    fun fakeController() = FunctionalTestController()
}