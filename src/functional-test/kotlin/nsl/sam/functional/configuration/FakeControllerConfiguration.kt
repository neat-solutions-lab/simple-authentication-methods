package nsl.sam.functional.configuration

import nsl.sam.functional.controller.FunctionalTestController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FakeControllerConfiguration {
    @Bean
    fun fakeController() = FunctionalTestController()
}