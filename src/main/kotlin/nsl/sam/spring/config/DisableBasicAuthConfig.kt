package nsl.sam.spring.config

import nsl.sam.config.DisableBasicAuthSimpleConfigurer
import nsl.sam.config.SimpleAuthConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DisableBasicAuthConfig {

    @Bean
    fun disableBasicAuthSimpleConfigurer(): SimpleAuthConfigurer {
        return DisableBasicAuthSimpleConfigurer()
    }

}