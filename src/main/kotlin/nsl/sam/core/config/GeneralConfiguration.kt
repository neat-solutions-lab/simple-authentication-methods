package nsl.sam.core.config

import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.configurer.ConfigurersFactories
import nsl.sam.configurer.ConfigurersFactoriesImpl
import nsl.sam.method.basicauth.BasicAuthMethodInternalConfigurerFactory
import nsl.sam.method.token.TokenAuthMethodInternalConfigurerFactory
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.method.token.localtokens.LocalFileTokensToUserMapper
import nsl.sam.method.token.localtokens.LocalTokensSource
import nsl.sam.core.entrypoint.AuthenticationEntryPointFactory
import nsl.sam.core.entrypoint.SimpleAuthenticationEntryPointFactory
import nsl.sam.core.sender.ResponseSender
import nsl.sam.core.sender.UnauthenticatedAccessResponseSender
import nsl.sam.core.entrypoint.SimpleFailedAuthenticationEntryPoint
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
class GeneralConfiguration {

    @Bean
    fun authenticationEntryPointFactory(): AuthenticationEntryPointFactory {
        return SimpleAuthenticationEntryPointFactory()
    }

    @Bean
    fun beanDefinitionRegistryPostProcessor(): BeanDefinitionRegistryPostProcessor {
        return BeanDefinitionRegistryPostProcessorImpl()
    }

    @Bean
    fun unauthenticatedAccessResponseSender(): ResponseSender {
        return UnauthenticatedAccessResponseSender()
    }

    @Bean
    fun simpleAuthenticationEntryPointForHttpBasic(): AuthenticationEntryPoint {
        return SimpleFailedAuthenticationEntryPoint(unauthenticatedAccessResponseSender())
    }

    @Bean
    fun localTokensStore() : LocalTokensSource {
        return LocalTokensSource()
    }

    @Bean
    fun tokenToUserMapper(): TokenToUserMapper {
        return LocalFileTokensToUserMapper()
    }

    @Bean
    fun configurersFactories(): ConfigurersFactories {
        return ConfigurersFactoriesImpl()
    }

    @Bean
    fun basicAuthMethodInternalConfigurerFactory(configurersFactories: ConfigurersFactories): AuthMethodInternalConfigurerFactory {
        val factory = BasicAuthMethodInternalConfigurerFactory("default-basic-auth-factory")
        configurersFactories.addFactory(factory)
        return factory
    }

    @Bean
    fun tokenAuthMethodInternalConfigurerFactory(configurersFactories: ConfigurersFactories): AuthMethodInternalConfigurerFactory {
        val factory = TokenAuthMethodInternalConfigurerFactory("default-simple-token-factory")
        configurersFactories.addFactory(factory)
        return factory
    }
}