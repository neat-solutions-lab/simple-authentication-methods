package nsl.sam.method.basicauth

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthenticationAttributes
import nsl.sam.method.basicauth.userdetails.LocalFileUsersSource
import nsl.sam.method.basicauth.userdetails.LocalUserDetailsService
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactories
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.core.entrypoint.factory.DefaultAuthenticationEntryPointFactory
import nsl.sam.core.entrypoint.helper.AnnotationAttributeToObjectMapper
import nsl.sam.core.entrypoint.helper.AuthenticationEntryPointHelper
import nsl.sam.method.basicauth.userdetails.SourceAwareUserDetailsService
import nsl.sam.method.basicauth.userdetails.UsersSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.security.web.AuthenticationEntryPoint

class BasicAuthMethodInternalConfigurerFactory(override val name: String) : AuthMethodInternalConfigurerFactory {

    companion object {
        val log by logger()
    }

    @Autowired
    private lateinit var environment: Environment

    override fun getSupportedMethod(): AuthenticationMethod {
        return AuthenticationMethod.SIMPLE_BASIC_AUTH
    }

    override fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer {
        return BasicAuthMethodInternalConfigurer(
                getUsersDetailsService(attributes),
                getAuthenticationEntryPoint(attributes)
        )
    }

    private fun getUsersDetailsService(attributes: EnableAnnotationAttributes): SourceAwareUserDetailsService {
        val usersSource = getUsersSource(attributes)
        return LocalUserDetailsService(usersSource)
    }

    private fun getUsersSource(attributes: EnableAnnotationAttributes): UsersSource {
        val passwordsFilePath = getPasswordsFilePath(attributes)
        return LocalFileUsersSource(passwordsFilePath)
    }

    private fun getPasswordsFilePath(attributes: EnableAnnotationAttributes): String {
        val simpleBasicAuthenticationAttributes = getSimpleAuthenticationAttributes(attributes.enableAnnotationMetadata)
        return decideOnPasswordFilePath(simpleBasicAuthenticationAttributes)
    }

    private fun getAuthenticationEntryPoint(attributes: EnableAnnotationAttributes): AuthenticationEntryPoint {

        return AnnotationAttributeToObjectMapper.getObject(
                "authenticationEntryPointFactory",
                "nsl.sam.authentication-entry-point.factory",
                listOf(EnableSimpleAuthenticationMethods::class, SimpleBasicAuthentication::class),
                attributes.enableAnnotationMetadata,
                environment,
                AuthenticationEntryPointFactory::class,
                DefaultAuthenticationEntryPointFactory::class
        )

        //return AuthenticationEntryPointHelper.getAuthenticationEntryPoint(
        //        environment,
        //        attributes.enableAnnotationMetadata,
        //        arrayOf(EnableSimpleAuthenticationMethods::class, SimpleBasicAuthentication::class))
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

        val annotationMetadataResolver = AnnotationMetadataResolver(
                annotationMetadata, SimpleBasicAuthentication::class
        )

        if (!annotationMetadataResolver.isAnnotationPresent()) {
            return SimpleBasicAuthenticationAttributes.default()
        }

        return SimpleBasicAuthenticationAttributes.Builder()
                .passwordFilePathProperty(annotationMetadataResolver.getRequiredAttributeValue(
                        "passwordsFilePropertyName", String::class
                ))
                .passwordFilePath(annotationMetadataResolver.getRequiredAttributeValue(
                        "passwordsFilePath", String::class
                ))
                .authenticationEntryPointFactory(annotationMetadataResolver.getRequiredAttributeAsKClassArray (
                        "authenticationEntryPointFactory",
                        AuthenticationEntryPointFactory::class
                ))
                .build()
    }
}
