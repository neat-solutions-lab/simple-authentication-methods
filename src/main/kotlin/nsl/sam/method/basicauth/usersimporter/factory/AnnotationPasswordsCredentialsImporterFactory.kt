package nsl.sam.method.basicauth.usersimporter.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.importer.reader.impl.AnnotationCredentialsReader
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.extractor.PasswordsArrayAnnotationExtractor
import org.springframework.core.env.Environment

class AnnotationPasswordsCredentialsImporterFactory : PasswordsCredentialsImporterFactory {

    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): PasswordsCredentialsImporter {
        val extractor = PasswordsArrayAnnotationExtractor()
        return PasswordsCredentialsImporter(AnnotationCredentialsReader(attributes, extractor))
    }

}