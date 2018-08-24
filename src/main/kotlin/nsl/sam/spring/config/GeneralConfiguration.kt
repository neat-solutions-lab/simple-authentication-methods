package nsl.sam.spring.config

import nsl.sam.sender.ResponseSender
import nsl.sam.sender.UnauthenticatedAccessResponseSender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * This configuration is activated by [GeneralConfigurationActivator] which itself if
 * [org.springframework.context.annotation.ImportSelectorector] and is used to select configuration
 * files in dynamic way when [nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods] is
 * processed.
 */
@Configuration
@EnableWebMvc
class GeneralConfiguration {

    @Bean
    fun dynamicBeansRegistar(): DynamicBeansRegistar {
        return DynamicBeansRegistar()
    }

    @Bean
    fun unauthenticatedAccessResponseSender(): ResponseSender {
        return UnauthenticatedAccessResponseSender()
    }
}