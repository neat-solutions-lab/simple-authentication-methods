package nsl.sam.functional.basicauth

import nsl.sam.functional.controller.FunctionalTestController
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Bean

@EnableSimpleAuthenticationMethods([])
class NoBasicAuthEnabledFunctionalTestConfig {
    @Bean
    fun fakeController() = FunctionalTestController()
}