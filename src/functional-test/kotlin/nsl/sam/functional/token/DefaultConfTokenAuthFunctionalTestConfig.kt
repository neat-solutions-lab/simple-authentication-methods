package nsl.sam.functional.token

import nsl.sam.functional.controller.FunctionalTestController
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Bean

@EnableSimpleAuthenticationMethods
class DefaultConfTokenAuthFunctionalTestConfig {
    @Bean
    fun fakeController() = FunctionalTestController()
}