package nsl.sam.method.token.tokensimporter.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.envvar.SteeredEnvironmentVariablesAccessor
import nsl.sam.importer.reader.impl.EnvironmentCredentialsReader
import nsl.sam.method.token.tokensimporter.TokenCredentialsImporterFactory
import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.method.token.tokensimporter.extractor.TokensArrayEnvVarExtractor
import org.springframework.core.env.Environment

class EnvironmentTokensCredentialsImportFactory : TokenCredentialsImporterFactory {
    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): TokensCredentialsImporter {
        val extractor = TokensArrayEnvVarExtractor(SteeredEnvironmentVariablesAccessor())
        return TokensCredentialsImporter(EnvironmentCredentialsReader(attributes, extractor))
    }
}