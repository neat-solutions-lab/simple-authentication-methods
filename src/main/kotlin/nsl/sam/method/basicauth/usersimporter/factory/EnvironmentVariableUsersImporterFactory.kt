package nsl.sam.method.basicauth.usersimporter.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.envvar.SteeredEnvironmentVariablesAccessor
import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.usersimporter.UsersImporterFactory
import nsl.sam.method.basicauth.usersimporter.impl.EnvironmentVariableUsersImporter
import org.springframework.core.env.Environment

class EnvironmentVariableUsersImporterFactory : UsersImporterFactory {
    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): UsersImporter {
        return EnvironmentVariableUsersImporter(
                attributes,
                SteeredEnvironmentVariablesAccessor()
        )
    }

}