package nsl.sam.method.token.tokensimporter.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.reader.impl.AnnotationCredentialsReader
import nsl.sam.method.token.tokensimporter.TokenCredentialsImporterFactory
import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.method.token.tokensimporter.extractor.TokensArrayAnnotationExtractor
import org.springframework.core.env.Environment

class AnnotationTokensCredentialsImporterFactory : TokenCredentialsImporterFactory {

    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): TokensCredentialsImporter {
        val extractor = TokensArrayAnnotationExtractor()
        return TokensCredentialsImporter(AnnotationCredentialsReader(attributes, extractor))
    }
}