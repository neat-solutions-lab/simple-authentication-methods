package nsl.sam.spring.config

import nsl.sam.config.SimpleAuthConfigurer
import nsl.sam.method.basicauth.userdetails.LocalFileUsersSource
import nsl.sam.method.basicauth.userdetails.LocalUserDetailsService
import nsl.sam.method.basicauth.userdetails.UsersSource
import nsl.sam.method.token.TokenAuthMethodRegistar
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.method.token.localtokens.LocalFileTokensToUserMapper
import nsl.sam.method.token.localtokens.LocalTokensSource
import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.sender.ResponseSender
import nsl.sam.sender.UnauthenticatedAccessResponseSender
import nsl.sam.spring.entrypoint.SimpleFailedAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
class GeneralConfiguration {

    @Bean
    fun beanDefinitionRegistryPostProcessor(): BeanDefinitionRegistryPostProcessor {
        return BeanDefinitionRegistryPostProcessorImpl()
    }

    @Bean
    fun unauthenticatedAccessResponseSender(): ResponseSender {
        return UnauthenticatedAccessResponseSender()
    }

    @Bean
    fun usersSource(): UsersSource {
        return LocalFileUsersSource()
    }

    @Bean
    fun localUsersDetailsService(): UserDetailsService {
        return LocalUserDetailsService()
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

}