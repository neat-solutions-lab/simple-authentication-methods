package nsl.sam.functional.disabled

import nsl.sam.functional.controller.FunctionalTestController
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Bean

@EnableSimpleAuthenticationMethods([])
class NoAuthMethodEnabledFunctionalTestConfig {
    @Bean
    fun fakeController() = FunctionalTestController()
}