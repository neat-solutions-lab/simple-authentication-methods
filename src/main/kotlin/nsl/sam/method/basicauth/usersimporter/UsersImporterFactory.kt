package nsl.sam.method.basicauth.usersimporter

import nsl.sam.core.annotation.EnableAnnotationAttributes
import org.springframework.core.env.Environment

interface UsersImporterFactory {
    fun create(attributes: EnableAnnotationAttributes, environment: Environment): UsersImporter
}