package nsl.sam.method.basicauth

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.annotation.AnnotationProcessor
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributes
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributes.Companion.create
import nsl.sam.method.basicauth.userdetails.LocalFileUsersSource
import nsl.sam.method.basicauth.userdetails.LocalUserDetailsService
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.AuthenticationEntryPointFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata

class BasicAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {

    companion object {
        val log by logger()
    }

    @Autowired
    private lateinit var environment: Environment

//    @Autowired
//    private lateinit var authenticationEntryPointFactory: AuthenticationEntryPointFactory

    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_BASIC_AUTH
    }

    override fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer {

        val annotationMetadata = attributes.enableAnnotationMetadata

        val simpleBasicAuthenticationAttributes = getSimpleAuthenticationAttributes(annotationMetadata)
        log.debug("Configuration of ${AuthenticationMethod.SIMPLE_BASIC_AUTH.name} picked up by " +
                "${this::class.qualifiedName}: $simpleBasicAuthenticationAttributes")

        val passwordsFile = decideOnPasswordFilePath(simpleBasicAuthenticationAttributes)

        /*
         * To determine:
         * - instance of UsersDetailsService (from Env)
         * - instance UsersSource
         * - configure UsersSource and pass it to UsersDetailsService
         */

        val usersSource = LocalFileUsersSource(passwordsFile)
        val usersDetailsService = LocalUserDetailsService(usersSource)

        val parentAnnotationMetadataResolver = AnnotationMetadataResolver(
                attributes.enableAnnotationMetadata, EnableSimpleAuthenticationMethods::class
        )

        val annotationMetadataResolver = AnnotationMetadataResolver(
                attributes.enableAnnotationMetadata,
                SimpleBasicAuthentication::class,
                parentAnnotationMetadataResolver
        )

        val authenticationEntryPointFactory = AuthenticationEntryPointFactory.getFactory(
                annotationMetadataResolver, environment
        )

        return BasicAuthMethodInternalConfigurer(
                usersDetailsService,
                authenticationEntryPointFactory.create()
        )

    }

    private fun decideOnPasswordFilePath(simpleBasicAuthenticationAttributes: SimpleBasicAuthenticationAttributes): String {

        if(simpleBasicAuthenticationAttributes.passwordsFilePath.isNotBlank())
            return simpleBasicAuthenticationAttributes.passwordsFilePath

        if(simpleBasicAuthenticationAttributes.passwordsFilePathProperty.isNotBlank())
            return environment.getProperty(simpleBasicAuthenticationAttributes.passwordsFilePathProperty, "")

        if(environment.containsProperty("sam.passwords-file"))
            return environment.getProperty("sam.passwords-file", "")

        return ""
    }

    private fun getSimpleAuthenticationAttributes(annotationMetadata: AnnotationMetadata): SimpleBasicAuthenticationAttributes {

        AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        SimpleBasicAuthentication::class.qualifiedName!!, true
                )
        ) ?: return SimpleBasicAuthenticationAttributes.default()

        return create {

            passwordsFilePathProperty {
                AnnotationProcessor.getAnnotationAttributeValue(
                        annotationMetadata,
                        SimpleBasicAuthentication::class,
                        "passwordsFilePropertyName",
                        String::class
                )
            }
            passwordsFilePath {
                AnnotationProcessor.getAnnotationAttributeValue(
                        annotationMetadata,
                        SimpleBasicAuthentication::class,
                        "passwordsFilePath",
                        String::class
                )
            }
            authenticationEntryPointFactory {
//                AnnotationProcessor.getAnnotationAttributeAsKClass(
//                        enableAnnotationMetadata,
//                        SimpleBasicAuthentication::class,
//                        "authenticationEntryPointFactory",
//                        AuthenticationEntryPointFactory::class
//                )
                AnnotationProcessor.getAnnotationAttributeAsArray(
                        annotationMetadata,
                        SimpleBasicAuthentication::class,
                        "authenticationEntryPointFactory",
                        AuthenticationEntryPointFactory::class
                )

            }
        }
    }
}
