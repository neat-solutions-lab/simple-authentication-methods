package nsl.sam.method.basicauth.userssource.factory

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributes
import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.usersimporter.UsersImporterFactory
import nsl.sam.method.basicauth.usersimporter.factory.AnnotationAttributeUsersImporterFactory
import nsl.sam.method.basicauth.usersimporter.factory.LocalFileUsersImporterFactory
import nsl.sam.method.basicauth.usersimporter.impl.LocalFileUsersImporter
import nsl.sam.method.basicauth.userssource.UsersSource
import nsl.sam.method.basicauth.userssource.UsersSourceFactory
import nsl.sam.method.basicauth.userssource.impl.InMemoryUsersSource
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class InMemoryUsersSourceFactory: UsersSourceFactory {

    companion object {
        val log by logger()
    }

    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): UsersSource {
        val usersImporter = getUsersImporter(attributes, environment)
        return InMemoryUsersSource(usersImporter)
    }

    private fun getUsersImporter(attributes: EnableAnnotationAttributes, environment: Environment): UsersImporter {
        /*
         * Decide which UsersImporter should be used:
         * - LocalFileUsersImporter
         * - EnvironmentVariableUsersImporter
         * - AnnotationAttributeUsersImporter
         */

        val factoriesClassesArray: Array<KClass<out UsersImporterFactory>> = getUsersImportersFactories()

        factoriesClassesArray.forEach {
            val factory = it.createInstance()
            val usersImporter = factory.create(attributes, environment)
            if(usersImporter.hasItems()) {
                log.info("Selected UsersImporter is $usersImporter")
                return usersImporter
            }
        }

        /*
         * if up to now the UsersImporter could not be selected then return the default one
         */
        val defaultUsersImporter = LocalFileUsersImporterFactory().create(attributes, environment)
        log.info("Selected UsersImporter is the default one: $defaultUsersImporter")
        return defaultUsersImporter
    }

    private fun getUsersImportersFactories(): Array<KClass<out UsersImporterFactory>> {
        return arrayOf(
                LocalFileUsersImporterFactory::class,
                AnnotationAttributeUsersImporterFactory::class
        )
    }
}