package nsl.sam.core.config

import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.configurer.ConfigurersFactories
import nsl.sam.configurer.ConfigurersFactoriesImpl
import nsl.sam.method.basicauth.BasicAuthMethodInternalConfigurerFactory
import nsl.sam.method.token.TokenAuthMethodInternalConfigurerFactory
import nsl.sam.method.token.tokendetails.AvailabilityAwareTokenDetailsService
import nsl.sam.method.token.tokendetails.impl.DefaultTokenDetailsService
import nsl.sam.method.token.tokensresolver.impl.InMemoryTokensResolver
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
class GeneralConfiguration {

//    @Bean
//    fun beanDefinitionRegistryPostProcessor(): BeanDefinitionRegistryPostProcessor {
//        return BeanDefinitionRegistryPostProcessorImpl()
//    }

    @Bean
    fun configurersFactories(): ConfigurersFactories {
        return ConfigurersFactoriesImpl()
    }

    @Bean
    fun basicAuthMethodInternalConfigurerFactory(
            configurersFactories: ConfigurersFactories
    ): AuthMethodInternalConfigurerFactory {
        val factory = BasicAuthMethodInternalConfigurerFactory("default-basic-auth-factory")
        configurersFactories.addFactory(factory)
        return factory
    }

    @Bean
    fun tokenAuthMethodInternalConfigurerFactory(
            configurersFactories: ConfigurersFactories
    ): AuthMethodInternalConfigurerFactory {
        val factory = TokenAuthMethodInternalConfigurerFactory("default-simple-token-factory")
        configurersFactories.addFactory(factory)
        return factory
    }
}