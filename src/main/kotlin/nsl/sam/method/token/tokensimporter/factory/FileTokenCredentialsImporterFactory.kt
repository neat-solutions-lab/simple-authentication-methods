package nsl.sam.method.token.tokensimporter.factory

import nsl.sam.changes.resource.FileChangeDetector
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.method.token.annotation.SimpleTokenAuthenticationAttributesExtractor
import nsl.sam.method.token.annotation.SimpleTokenAuthenticationAttributes
import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.method.token.tokensimporter.TokenCredentialsImporterFactory
import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.logger.logger
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import org.springframework.core.env.Environment

class FileTokenCredentialsImporterFactory : TokenCredentialsImporterFactory {

    companion object {
        val log by logger()
    }

    override fun create(
            attributes: EnableAnnotationAttributes,
            environment: Environment
    ): TokensCredentialsImporter {

        val simpleTokenAuthenticationAttributes =
                SimpleTokenAuthenticationAttributesExtractor.extractAttributes(
                        attributes.enableAnnotationMetadata
                )

        val tokensFilePath = getTokensFilePath(simpleTokenAuthenticationAttributes, environment)
        log.info("Tokens file path to be used by " +
                 "${TokensCredentialsImporter::class.simpleName}: $tokensFilePath")

        val importer = TokensCredentialsImporter(FileCredentialsReader(tokensFilePath))

        injectChangeDetectorIfConfigured(
                importer, tokensFilePath, simpleTokenAuthenticationAttributes, environment
        )

        return importer
    }

    private fun injectChangeDetectorIfConfigured(
            importer: TokensCredentialsImporter,
            observablePath: String,
            simpleTokenAuthenticationAttributes: SimpleTokenAuthenticationAttributes,
            environment: Environment
    ) {

        if(!isTokensFileChangeDetectionEnabled(
                        simpleTokenAuthenticationAttributes, environment)
        ) return

        val fileChangeDetector = FileChangeDetector(observablePath)
        log.info("Passwords file changes detector created for path: $observablePath")

        importer.setChangeDetector(fileChangeDetector)
        log.info("Tokens file changes detector injected to the importer: " +
                 "${importer::class.qualifiedName}")

    }

    private fun isTokensFileChangeDetectionEnabled(
            simpleTokenAuthenticationAttributes: SimpleTokenAuthenticationAttributes,
            environment: Environment

    ): Boolean {

        val propertyValue = environment.getProperty("sam.detect-tokens-file-changes")
        if(propertyValue != null && propertyValue.toBoolean()) {
            log.info("Tokens file change detection is enabled by external configuration.")
            return true
        }

        val hardcodedValue = simpleTokenAuthenticationAttributes.detectTokensFileChanges
        if(hardcodedValue) {
            log.info("Tokens file change detection is enabled by " +
                     "${SimpleTokenAuthentication::class.simpleName} annotation attribute.")
        } else {
            log.debug("Tokens file change detection is NOT enabled.")
        }
        return hardcodedValue
    }

    private fun getTokensFilePath(
            simpleTokenAuthenticationAttributes: SimpleTokenAuthenticationAttributes,
            environment: Environment): String {

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

        if(environment.containsProperty("nsl.sam.tokens-file"))
            return environment.getProperty("nsl.sam.tokens-file", "")

        return ""
    }
}