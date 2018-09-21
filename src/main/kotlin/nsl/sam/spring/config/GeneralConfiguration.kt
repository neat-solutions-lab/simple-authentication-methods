package nsl.sam.spring.config

import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.configurer.ConfigurersFactories
import nsl.sam.configurer.ConfigurersFactoriesImpl
import nsl.sam.method.basicauth.BasicAuthMethodInternalConfigurerFactory
import nsl.sam.method.basicauth.userdetails.LocalFileUsersSource
import nsl.sam.method.basicauth.userdetails.LocalUserDetailsService
import nsl.sam.method.basicauth.userdetails.UsersSource
import nsl.sam.method.token.TokenAuthMethodInternalConfigurerFactory
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.method.token.localtokens.LocalFileTokensToUserMapper
import nsl.sam.method.token.localtokens.LocalTokensSource
import nsl.sam.spring.sender.ResponseSender
import nsl.sam.spring.sender.UnauthenticatedAccessResponseSender
import nsl.sam.spring.entrypoint.SimpleFailedAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
class GeneralConfiguration {

    @Value("\${sam.passwords-file:}")
    //lateinit var passwordsFile: String


    @Bean
    fun beanDefinitionRegistryPostProcessor(): BeanDefinitionRegistryPostProcessor {
        return BeanDefinitionRegistryPostProcessorImpl()
    }

    @Bean
    fun unauthenticatedAccessResponseSender(): ResponseSender {
        return UnauthenticatedAccessResponseSender()
    }

//    @Bean
//    fun usersSource(@Value("\${sam.passwords-file:}") passwordsFile: String): UsersSource {
//        return LocalFileUsersSource(passwordsFile)
//    }
//
//    @Bean
//    fun localUsersDetailsService(usersSource: UsersSource): UserDetailsService {
//        return LocalUserDetailsService(usersSource)
//    }

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