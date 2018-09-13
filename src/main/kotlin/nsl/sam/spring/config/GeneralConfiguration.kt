package nsl.sam.spring.config

import nsl.sam.config.SimpleAuthConfigurer
import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.sender.ResponseSender
import nsl.sam.sender.UnauthenticatedAccessResponseSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 */
@Configuration
@EnableWebMvc
class GeneralConfiguration {

    //@Autowired(required = false)
    //@Autowired
    //lateinit var authMethodRegistars: List<AuthMethodRegistar>

    //@Autowired(required = false)
    //var simpleAuthConfigurers: List<SimpleAuthConfigurer>? = null

    //@Bean
    //fun simpleWebSecurityConfigurer(): WebSecurityConfigurerAdapter {
    //    return SimpleWebSecurityConfigurer(
    //            this.authMethodRegistars, this.simpleAuthConfigurers
    //    )
    //}

    @Bean
    fun beanDefinitionRegistryPostProcessor(): BeanDefinitionRegistryPostProcessor {
        return BeanDefinitionRegistryPostProcessorImpl()
    }

    @Bean
    fun unauthenticatedAccessResponseSender(): ResponseSender {
        return UnauthenticatedAccessResponseSender()
    }
}