package nsl.sam.spring.config

import nsl.sam.sender.ResponseSender
import nsl.sam.sender.UnauthenticatedAccessResponseSender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
@EnableWebSecurity
class SimpleAuthenticationMethodsGeneralConfiguration {

    @Bean
    fun dynamicBeansRegistar(): DynamicBeansRegistar {
        return DynamicBeansRegistar()
    }

    @Bean
    fun unauthenticatedAccessResponseSender(): ResponseSender {
        return UnauthenticatedAccessResponseSender()
    }
}