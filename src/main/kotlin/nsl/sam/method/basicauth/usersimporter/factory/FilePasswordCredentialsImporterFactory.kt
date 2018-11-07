package nsl.sam.method.basicauth.usersimporter.factory

import nsl.sam.changes.resource.FileChangeDetector
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributes
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributesExtractor
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporterFactory
import org.springframework.core.env.Environment

class FilePasswordCredentialsImporterFactory : PasswordsCredentialsImporterFactory {

    companion object {
        val log by logger()
    }

    override fun create(
            attributes: EnableAnnotationAttributes,
            environment: Environment
    ): PasswordsCredentialsImporter {

        val simpleBasicAuthenticationAttributes =
                extractSimpleBasicAuthenticationAttributes(attributes)

        val passwordsFilePath = getPasswordsFilePath(
                simpleBasicAuthenticationAttributes, environment
        )
        log.info("Passwords file to be used by " +
                 "${PasswordsCredentialsImporter::class.simpleName}: $passwordsFilePath")

        val importer = PasswordsCredentialsImporter(FileCredentialsReader(passwordsFilePath))
        injectChangeDetectorIfConfigured(
                importer, passwordsFilePath, simpleBasicAuthenticationAttributes, environment
        )
        return importer
    }

    private fun injectChangeDetectorIfConfigured(
            importer: PasswordsCredentialsImporter,
            observableFilePath: String,
            simpleBasicAuthenticationAttributes: SimpleBasicAuthenticationAttributes,
            environment: Environment
    ) {
        if (!isPasswordsFileChangeDetectionEnabled(
                        simpleBasicAuthenticationAttributes, environment)
        ) return

        val fileChangeDetector = FileChangeDetector(observableFilePath)
        log.info("Passwords file changes detector created for path: $observableFilePath")

        importer.setChangeDetector(fileChangeDetector)
        log.info("Passwords file changes detector injected to the importer: " +
                 "${importer::class.qualifiedName}")
    }

    private fun isPasswordsFileChangeDetectionEnabled(
            simpleBasicAuthenticationAttributes: SimpleBasicAuthenticationAttributes,
            environment: Environment
    ): Boolean {

        val propertyValue = environment.getProperty("sam.detect-passwords-file-changes")
        if(propertyValue != null && propertyValue.toBoolean()) {
            log.info("Passwords file change detection is enabled by external configuration.")
            return true
        }

        val hardcodedValue = simpleBasicAuthenticationAttributes.detectPasswordsFileChanges
        if(hardcodedValue) {
            log.info("Passwords file change detection is enabled by " +
                     "${SimpleBasicAuthentication::class.simpleName} annotation attribute")
        } else {
            log.info("Passwords file change detection is NOT enabled.")
        }
        return hardcodedValue
    }

    private fun extractSimpleBasicAuthenticationAttributes(
            attributes: EnableAnnotationAttributes) =
            SimpleBasicAuthenticationAttributesExtractor.extractAttributes(
                    attributes.enableAnnotationMetadata
            )

    private fun getPasswordsFilePath(
            simpleBasicAuthenticationAttributes: SimpleBasicAuthenticationAttributes,
            environment: Environment
    ): String {
        return decideOnPasswordFilePath(simpleBasicAuthenticationAttributes, environment)
    }

    private fun decideOnPasswordFilePath(
            simpleBasicAuthenticationAttributes: SimpleBasicAuthenticationAttributes,
            environment: Environment
    ): String {

        if (simpleBasicAuthenticationAttributes.passwordsFilePath.isNotBlank())
            return simpleBasicAuthenticationAttributes.passwordsFilePath

        if (simpleBasicAuthenticationAttributes.passwordsFilePathProperty.isNotBlank())
            return environment.getProperty(
                    simpleBasicAuthenticationAttributes.passwordsFilePathProperty, ""
            )

        if (environment.containsProperty("nsl.sam.passwords-file"))
            return environment.getProperty("nsl.sam.passwords-file", "")

        return ""
    }
}