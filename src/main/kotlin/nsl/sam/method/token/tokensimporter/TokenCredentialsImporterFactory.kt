package nsl.sam.method.token.tokensimporter

import nsl.sam.core.annotation.EnableAnnotationAttributes
import org.springframework.core.env.Environment

interface TokenCredentialsImporterFactory {
    fun create(attributes: EnableAnnotationAttributes, environment: Environment): TokensCredentialsImporter
}