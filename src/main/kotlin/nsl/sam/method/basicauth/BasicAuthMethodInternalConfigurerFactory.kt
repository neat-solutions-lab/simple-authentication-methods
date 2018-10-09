package nsl.sam.method.basicauth

import nsl.sam.annotation.inject.InjectedObjectsProvider
import nsl.sam.configurer.AuthMethodInternalConfigurer
import nsl.sam.configurer.AuthMethodInternalConfigurerFactory
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.entrypoint.factory.AuthenticationEntryPointFactory
import nsl.sam.core.entrypoint.factory.DefaultAuthenticationEntryPointFactory
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.userdetails.AvailabilityAwareUserDetailsService
import nsl.sam.method.basicauth.userdetails.impl.DefaultUserDetailsService
import nsl.sam.method.basicauth.userssource.UsersSource
import nsl.sam.method.basicauth.userssource.UsersSourceFactory
import nsl.sam.method.basicauth.userssource.factory.InMemoryUsersSourceFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
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

    private fun getUsersDetailsService(attributes: EnableAnnotationAttributes): AvailabilityAwareUserDetailsService {
        val usersSource = getUsersSource(attributes)
        return DefaultUserDetailsService(usersSource)
    }

    private fun getUsersSource(attributes: EnableAnnotationAttributes): UsersSource {
        val usersSourceFactory = getUsersSourceFactory()
        return usersSourceFactory.create(attributes, environment)
    }

    private fun getUsersSourceFactory(): UsersSourceFactory {
        return InMemoryUsersSourceFactory()
    }

    private fun getAuthenticationEntryPoint(attributes: EnableAnnotationAttributes): AuthenticationEntryPoint {

        return InjectedObjectsProvider.Builder(AuthenticationEntryPointFactory::class)
                .attributeName("authenticationEntryPointFactory")
                .defaultFactoryPropertyName("nsl.sam.authentication-entry-point.factory")
                .involvedAnnotationTypes(listOf(EnableSimpleAuthenticationMethods::class, SimpleBasicAuthentication::class))
                .annotationMetadata(attributes.enableAnnotationMetadata)
                .environment(environment)
                .defaultFactory(DefaultAuthenticationEntryPointFactory::class)
                .build().getObject()
    }
}
