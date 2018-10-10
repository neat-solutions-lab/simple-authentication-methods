package nsl.sam.method.token.tokensimporter.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.method.token.annotation.SimpleTokenAuthenticationAttibutesExtractor
import nsl.sam.method.token.annotation.SimpleTokenAuthenticationAttributes
import nsl.sam.method.token.tokensimporter.TokensImporter
import nsl.sam.method.token.tokensimporter.TokensImporterFactory
import nsl.sam.method.token.tokensimporter.impl.TokenFileImporter
import org.springframework.core.env.Environment

class TokenFileImporterFactory: TokensImporterFactory {

    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): TokensImporter {
        val tokensFilePath = getTokensFilePath(attributes, environment)
        return TokenFileImporter(tokensFilePath)
    }

    private fun getTokensFilePath(attributes: EnableAnnotationAttributes, environment: Environment): String {

        val simpleTokenAuthenticationAttributes = SimpleTokenAuthenticationAttibutesExtractor.extractAttributes(
                attributes.enableAnnotationMetadata
        )

        return decideOnTokensFilePath(simpleTokenAuthenticationAttributes, environment)
    }

    private fun decideOnTokensFilePath(
            simpleTokenAuthenticationAttributes: SimpleTokenAuthenticationAttributes,
            environment: Environment
    ): String {

        if (simpleTokenAuthenticationAttributes.tokensFilePath.isNotBlank())
            return simpleTokenAuthenticationAttributes.tokensFilePath

        if (simpleTokenAuthenticationAttributes.tokensFilePropertyName.isNotBlank())
            return simpleTokenAuthenticationAttributes.tokensFilePropertyName

        if(environment.containsProperty("sam.tokens-file"))
            return environment.getProperty("sam.tokens-file", "")

        return ""
    }
}