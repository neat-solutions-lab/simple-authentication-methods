package nsl.sam.functional.multiannotation

import nsl.sam.functional.controller.FunctionalTestController
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Bean

class MultiEnabledAnnotationFunctionalTestConfig {
    @Bean
    fun fakeController() = FunctionalTestController()
}