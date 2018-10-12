package nsl.sam.method.basicauth.usersimporter.interim

import nsl.sam.core.annotation.EnableAnnotationAttributes
import org.springframework.core.env.Environment

interface PasswordsCredentialsImporterFactory {
    fun create(attributes: EnableAnnotationAttributes, environment: Environment): PasswordsCredentialsImporter
}