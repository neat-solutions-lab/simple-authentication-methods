package nsl.sam.method.basicauth

import nsl.sam.annotation.AnnotationProcessor
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributes
import nsl.sam.method.basicauth.userdetails.LocalFileUsersSource
import nsl.sam.method.basicauth.userdetails.LocalUserDetailsService
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableAnnotationAttributes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.security.web.AuthenticationEntryPoint

class BasicAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {

    @Autowired
    private lateinit var environment: Environment

    @Autowired
    private lateinit var simpleAuthenticationEntryPoint: AuthenticationEntryPoint

    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_BASIC_AUTH
    }

    override fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer {

        val annotationMetadata = attributes.annotationMetadata

        val simpleBasicAuthenticationAttributes = getSimpleAuthenticationAttributes(annotationMetadata)

        val passwordsFile = decideOnPasswordFilePath(simpleBasicAuthenticationAttributes)

        println("------> $simpleBasicAuthenticationAttributes")

        /*
         * To determine:
         * - instance of UsersDetailsService (from Env)
         * - instance UsersSource
         * - configure UsersSource and pass it to UsersDetailsService
         */

        val usersSource = LocalFileUsersSource(passwordsFile)
        val usersDetailsService = LocalUserDetailsService(usersSource)

        return BasicAuthMethodInternalConfigurer(
                usersDetailsService,
                simpleAuthenticationEntryPoint)
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

        return SimpleBasicAuthenticationAttributes.create {

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
        }
    }
}