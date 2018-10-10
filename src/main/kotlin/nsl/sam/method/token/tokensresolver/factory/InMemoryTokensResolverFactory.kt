package nsl.sam.method.token.tokensresolver.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.method.token.tokensimporter.TokensImporter
import nsl.sam.method.token.tokensimporter.factory.FileTokenImporterFactory
import nsl.sam.method.token.tokensresolver.TokensResolver
import nsl.sam.method.token.tokensresolver.TokensResolverFactory
import nsl.sam.method.token.tokensresolver.impl.InMemoryTokensResolver
import org.springframework.core.env.Environment

class InMemoryTokensResolverFactory : TokensResolverFactory {

    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): TokensResolver {
        val tokensImporter = getTokensImporter(attributes, environment)
        return InMemoryTokensResolver(tokensImporter)
    }

    private fun getTokensImporter(attributes: EnableAnnotationAttributes, environment: Environment): TokensImporter {
        val factory = FileTokenImporterFactory()
        return factory.create(attributes, environment)
    }
}