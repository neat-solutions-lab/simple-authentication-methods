package nsl.sam.method.token.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.method.token.tokensimporter.TokenCredentialsImporter
import org.springframework.core.env.Environment

interface TokenCredentialsImporterFactory {
    fun create(attributes: EnableAnnotationAttributes, environment: Environment): TokenCredentialsImporter
}