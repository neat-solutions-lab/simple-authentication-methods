package nsl.sam.configurator

import nsl.sam.authenticator.localtokens.LocalFileTokensToUserMapper
import nsl.sam.authenticator.localtokens.LocalTokensStore
import nsl.sam.authenticator.localtokens.TokenToUserMapper
import nsl.sam.registar.AuthMethodRegistar
import nsl.sam.registar.TokenAuthMethodRegistar
import org.springframework.context.annotation.Bean

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