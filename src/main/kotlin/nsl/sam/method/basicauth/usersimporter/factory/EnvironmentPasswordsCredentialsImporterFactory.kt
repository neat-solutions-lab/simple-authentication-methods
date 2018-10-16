package nsl.sam.method.basicauth.usersimporter.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.envvar.SteeredEnvironmentVariablesAccessor
import nsl.sam.importer.reader.impl.EnvironmentCredentialsReader
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.extractor.PasswordsArrayEnvVarExtractor
import org.springframework.core.env.Environment

class EnvironmentPasswordsCredentialsImporterFactory : PasswordsCredentialsImporterFactory {
    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): PasswordsCredentialsImporter {
        val extractor = PasswordsArrayEnvVarExtractor(SteeredEnvironmentVariablesAccessor())
        return PasswordsCredentialsImporter(EnvironmentCredentialsReader(attributes, extractor))
    }
}