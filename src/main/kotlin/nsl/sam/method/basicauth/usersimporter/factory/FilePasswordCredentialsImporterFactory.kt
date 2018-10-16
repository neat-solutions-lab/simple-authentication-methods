package nsl.sam.method.basicauth.usersimporter.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributes
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributesExtractor
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporterFactory
import org.springframework.core.env.Environment

class FilePasswordCredentialsImporterFactory : PasswordsCredentialsImporterFactory {

    override fun create(
            attributes: EnableAnnotationAttributes,
            environment: Environment
    ): PasswordsCredentialsImporter {

        val passwordsFilePath = getPasswordsFilePath(attributes, environment)
        return PasswordsCredentialsImporter(FileCredentialsReader(passwordsFilePath))
    }

    private fun getPasswordsFilePath(
            attributes: EnableAnnotationAttributes,
            environment: Environment
    ): String {

        val simpleBasicAuthenticationAttributes =
                SimpleBasicAuthenticationAttributesExtractor.extractAttributes(
                        attributes.enableAnnotationMetadata
                )
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

        if (environment.containsProperty("sam.passwords-file"))
            return environment.getProperty("sam.passwords-file", "")

        return ""
    }
}