package nsl.sam.method.basicauth.userssource.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.factory.AnnotationPasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.factory.EnvironmentPasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.factory.FilePasswordCredentialsImporterFactory
import nsl.sam.method.basicauth.userssource.UsersSource
import nsl.sam.method.basicauth.userssource.UsersSourceFactory
import nsl.sam.method.basicauth.userssource.impl.InMemoryUsersSource
import org.springframework.core.env.Environment
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class InMemoryUsersSourceFactory : UsersSourceFactory {

    companion object {
        val log by logger()
    }

    override fun create(
            attributes: EnableAnnotationAttributes, environment: Environment
    ): UsersSource {

        val passwordsImporter = getPasswordsImporter(attributes, environment)
        val confProperties = getConfigurationProperties(environment)
        return InMemoryUsersSource.createInstance(passwordsImporter, confProperties)
    }

    private fun getConfigurationProperties(environment: Environment): Properties {

        val confProperties = Properties()

        confProperties.setProperty(
                "nsl.sam.passwords-file-change-detection-period",
                environment.getProperty(
                        "nsl.sam.passwords-file-change-detection-period",
                        "1000"
                )
        )

        return confProperties
    }

    private fun getPasswordsImporter(
            attributes: EnableAnnotationAttributes, environment: Environment
    ): PasswordsCredentialsImporter {

        val factoriesArray: Array<KClass<out PasswordsCredentialsImporterFactory>>
                = getPasswordsImportersFactories()

        factoriesArray.forEach {
            val factory = it.createInstance()
            val passwordsImporter = factory.create(attributes, environment)
            log.debug("Checking if $passwordsImporter has items to be imported.")
            if (passwordsImporter.hasItems()) {
                log.info("Selected ${PasswordsCredentialsImporterFactory::class.simpleName} is " +
                         "${factory::class.qualifiedName}")
                return passwordsImporter
            } else {
                log.debug("$passwordsImporter provides no items.")
            }
        }

        /*
         * if up to now the PasswordsImporter could not be selected then return the default one
         */
        val defaultPasswordsImporter =
                FilePasswordCredentialsImporterFactory().create(attributes, environment)
        log.info("Selected ${PasswordsCredentialsImporterFactory::class.simpleName} is the default one: " +
                 "${FilePasswordCredentialsImporterFactory::class.qualifiedName}")
        return defaultPasswordsImporter
    }

    private fun getPasswordsImportersFactories(): Array<KClass<out PasswordsCredentialsImporterFactory>> {
        return arrayOf(
                FilePasswordCredentialsImporterFactory::class,
                AnnotationPasswordsCredentialsImporterFactory::class,
                EnvironmentPasswordsCredentialsImporterFactory::class
        )
    }
}