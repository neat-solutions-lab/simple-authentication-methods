package nsl.sam.method.basicauth.usersimporter.interim.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.envvar.SteeredEnvironmentVariablesAccessor
import nsl.sam.importer.reader.impl.EnvironmentCredentialsReader
import nsl.sam.method.basicauth.usersimporter.interim.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.interim.PasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.interim.extractor.PasswordsArrayEnvVarExtractor
import org.springframework.core.env.Environment

class EnvironmentPasswordsCredentialsImporterFactory : PasswordsCredentialsImporterFactory {
    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): PasswordsCredentialsImporter {
        val extractor = PasswordsArrayEnvVarExtractor(SteeredEnvironmentVariablesAccessor())
        return PasswordsCredentialsImporter(EnvironmentCredentialsReader(attributes, extractor))
    }
}