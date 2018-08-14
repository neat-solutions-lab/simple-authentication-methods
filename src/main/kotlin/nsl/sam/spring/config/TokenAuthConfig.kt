package nsl.sam.spring.config

import nsl.sam.method.token.localtokens.LocalFileTokensToUserMapper
import nsl.sam.method.token.localtokens.LocalTokensStore
import nsl.sam.method.token.filter.TokenToUserMapper
import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.method.token.TokenAuthMethodRegistar
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenAuthConfig {

    @Bean
    fun localTokensStore() : LocalTokensStore {
        return LocalTokensStore()
    }

    @Bean
    fun tokenToUserMapper(): TokenToUserMapper {
        return LocalFileTokensToUserMapper()
    }

    @Bean
    fun tokenRegistar():AuthMethodRegistar {
        return TokenAuthMethodRegistar()
    }


}