package nsl.sam.configurator

import nsl.sam.authenticator.localtokens.LocalFileTokensToUserMapper
import nsl.sam.authenticator.localtokens.LocalTokensStore
import nsl.sam.authenticator.localtokens.TokenToUserMapper
import nsl.sam.ragistar.AuthMethodRegistar
import nsl.sam.ragistar.TokenAuthMethodRegistar
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class TokenAuthConfigurator {

    @Bean
    fun localTokensStore() : LocalTokensStore {
        return LocalTokensStore()
    }

    @Bean
    fun tokenToUserMapper(): TokenToUserMapper {
        return LocalFileTokensToUserMapper()
    }

    @Bean
    fun tokenRegisar():AuthMethodRegistar {
        return TokenAuthMethodRegistar()
    }

}