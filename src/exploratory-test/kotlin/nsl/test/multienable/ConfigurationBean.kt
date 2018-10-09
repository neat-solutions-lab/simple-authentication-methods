package nsl.test.multienable

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigurationBean {

    @Bean
    fun anyBean(): String {
        return "any string"
    }

}