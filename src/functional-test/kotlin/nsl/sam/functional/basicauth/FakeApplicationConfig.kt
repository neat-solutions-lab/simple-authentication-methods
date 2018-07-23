package nsl.sam.functional.basicauth

import nsl.sam.spring.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Bean

@EnableSimpleAuthenticationMethods
class FakeApplicationConfig {
    @Bean
    fun fakeController() = FakeController()
}