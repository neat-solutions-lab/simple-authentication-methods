package nsl.sam.method.token.tokensresolver.factory

import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.factory.AnnotationPasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.factory.EnvironmentPasswordsCredentialsImporterFactory
import nsl.sam.method.basicauth.usersimporter.factory.FilePasswordCredentialsImporterFactory
import nsl.sam.method.token.tokensimporter.TokenCredentialsImporterFactory
import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.method.token.tokensimporter.factory.AnnotationTokensCredentialsImporterFactory
import nsl.sam.method.token.tokensimporter.factory.EnvironmentTokensCredentialsImportFactory
import nsl.sam.method.token.tokensimporter.factory.FileTokenCredentialsImporterFactory
import nsl.sam.method.token.tokensresolver.TokensResolver
import nsl.sam.method.token.tokensresolver.TokensResolverFactory
import nsl.sam.method.token.tokensresolver.impl.InMemoryTokensResolver
import org.springframework.core.env.Environment
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class InMemoryTokensResolverFactory : TokensResolverFactory {

    companion object {
        val log by logger()
    }

    override fun create(attributes: EnableAnnotationAttributes, environment: Environment): TokensResolver {
        val tokensImporter = getTokensImporter(attributes, environment)
        val confProperties = getConfigurationProperties(environment)
        return InMemoryTokensResolver.createInstance(tokensImporter, confProperties)
    }

    private fun getConfigurationProperties(environment: Environment): Properties {
        val confProperties = Properties()

        confProperties.setProperty(
                "nsl.sam.tokens-file-change-detection-period",
                environment.getProperty(
                        "nsl.sam.tokens-file-change-detection-period",
                        "1000"
                )
        )
        return confProperties
    }

    private fun getTokensImporter(
            attributes: EnableAnnotationAttributes, environment: Environment
    ): TokensCredentialsImporter {

        val factoriesArray: Array<KClass<out TokenCredentialsImporterFactory>> = getTokensImportersFactories()

        factoriesArray.forEach {
            val factory = it.createInstance()
            val tokensImporter = factory.create(attributes, environment)
            if(tokensImporter.hasItems()) {
                log.info("Selected ${TokensCredentialsImporter::class.simpleName} is ${tokensImporter::class.qualifiedName}")
                return tokensImporter
            }
        }

        /*
         * if up to now the TokensImporter could not be selected then return the default one
         */
        val defaultTokensImporter = FileTokenCredentialsImporterFactory().create(attributes, environment)
        log.info("Selected ${TokensCredentialsImporter::class.simpleName} is the default one: " +
                 "${defaultTokensImporter::class.qualifiedName}")
        return defaultTokensImporter
    }

    private fun getTokensImportersFactories(): Array<KClass<out TokenCredentialsImporterFactory>> {
        return arrayOf(
                FileTokenCredentialsImporterFactory::class,
                AnnotationTokensCredentialsImporterFactory::class,
                EnvironmentTokensCredentialsImportFactory::class
        )
    }
}