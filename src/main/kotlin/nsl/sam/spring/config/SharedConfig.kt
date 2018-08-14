package nsl.sam.spring.config

import nsl.sam.sender.ResponseSender
import nsl.sam.sender.UnauthenticatedAccessResponseSender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SharedConfig {

    @Bean
    fun unauthenticatedAccessResponseSender(): ResponseSender {
        return UnauthenticatedAccessResponseSender()
    }
}