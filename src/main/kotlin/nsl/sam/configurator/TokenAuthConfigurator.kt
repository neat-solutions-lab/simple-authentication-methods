package nsl.sam.configurator

import nsl.sam.authenticator.localtokens.LocalFileTokensToUserMapper
import nsl.sam.authenticator.localtokens.LocalTokensStore
import nsl.sam.authenticator.localtokens.TokenToUserMapper
import nsl.sam.registrant.AuthMethodRegistrant
import nsl.sam.registrant.TokenAuthMethodRegistrant
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
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
    fun tokenRegisar():AuthMethodRegistrant {
        return TokenAuthMethodRegistrant()
    }

}