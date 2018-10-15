package nsl.sam.method.basicauth.userssource.interim.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.usersimporter.interim.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.interim.PasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.interim.factory.AnnotationPasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.interim.factory.EnvironmentPasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.interim.factory.FilePasswordCredentialsImporterFactory
import nsl.sam.method.basicauth.userssource.UsersSource
import nsl.sam.method.basicauth.userssource.UsersSourceFactory
import nsl.sam.method.basicauth.userssource.interim.impl.InMemoryUsersSource
import org.springframework.core.env.Environment
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class InMemoryUsersSourceFactory : UsersSourceFactory {

    companion object {
        val log by logger()
    }

    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): UsersSource {

        val passwordsImporter = getPasswordsImporter(attributes, environment)
        return InMemoryUsersSource(passwordsImporter)

    }

    private fun getPasswordsImporter(
            attributes: EnableAnnotationAttributes, environment: Environment
    ): PasswordsCredentialsImporter {

        val factoriesArray: Array<KClass<out PasswordsCredentialsImporterFactory>>
                = getPasswordsImportersFactories()

        factoriesArray.forEach {
            val factory = it.createInstance()
            val usersImporter = factory.create(attributes, environment)
            if (usersImporter.hasItems()) {
                log.info("Selected UsersImporter is $usersImporter")
                return usersImporter
            }
        }

        /*
         * if up to now the UsersImporter could not be selected then return the default one
         */
        val defaultUsersImporter = FilePasswordCredentialsImporterFactory().create(attributes, environment)
        log.info("Selected UsersImporter is the default one: $defaultUsersImporter")
        return defaultUsersImporter
    }

    private fun getPasswordsImportersFactories(): Array<KClass<out PasswordsCredentialsImporterFactory>> {
        return arrayOf(
                FilePasswordCredentialsImporterFactory::class,
                AnnotationPasswordsCredentialsImporterFactory::class,
                EnvironmentPasswordsCredentialsImporterFactory::class
        )
    }

}