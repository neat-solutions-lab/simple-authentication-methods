package nsl.sam.spring.annotation

import nsl.sam.spring.config.BasicAuthConfig
import nsl.sam.spring.config.DisableBasicAuthConfig
import nsl.sam.spring.config.SimpleWebSecurityConfigurer
import nsl.sam.spring.config.TokenAuthConfig
import org.assertj.core.api.Assertions.*
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.core.type.AnnotationMetadata
import org.mockito.BDDMockito.*

@RunWith(MockitoJUnitRunner::class)
class EnabledEntrypointsSelectorTest {

    @Mock
    lateinit var importingClassMetadata: AnnotationMetadata

    @Test
    fun `selectImports(), SIMPLE_BASIC_AUTH enabled`() {

        // DATA ARRANGEMENTS
        val enabledMethodsArray = arrayOf(AuthenticationMethod.SIMPLE_BASIC_AUTH)

        val expectedResult = arrayOf(
                SimpleWebSecurityConfigurer::class.qualifiedName,
                BasicAuthConfig::class.qualifiedName
        )

        // MOCKITO FIXTURES
        given(importingClassMetadata.getAnnotationAttributes(
                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
        ).willReturn(mapOf("methods" to enabledMethodsArray))

        // OBJECT OF TESTED CLASS
        val enabledEntrypointsSelector = EnabledEntrypointsSelector()

        // ACT
        val configurationClasses = enabledEntrypointsSelector.selectImports(importingClassMetadata)

        // ASSERT
        assertThat(configurationClasses).containsExactlyInAnyOrder(*expectedResult)
    }

    @Test
    fun `selectImports(), SIMPLE_TOKEN enabled`() {

        // DATA ARRANGEMENTS
        val enabledMethodsArray = arrayOf(AuthenticationMethod.SIMPLE_TOKEN)

        val expectedResult = arrayOf(SimpleWebSecurityConfigurer::class.qualifiedName,
                TokenAuthConfig::class.qualifiedName,
                DisableBasicAuthConfig::class.qualifiedName
        )

        // MOCKITO FIXTURES
        given(importingClassMetadata.getAnnotationAttributes(
                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
        ).willReturn(mapOf("methods" to enabledMethodsArray))

        // OBJECT OF TESTED CLASS
        val enabledEntrypointsSelector = EnabledEntrypointsSelector()

        // ACT
        val configurationClasses = enabledEntrypointsSelector.selectImports(importingClassMetadata)

        // ASSERT
        assertThat(configurationClasses).containsExactlyInAnyOrder(*expectedResult)
    }

    @Test
    fun `selectImports(), SIMPLE_BASIC_AUTH and SIMPLE_TOKEN enabled`() {

        // DATA ARRANGEMENTS
        val enabledMethodsArray = arrayOf(
                AuthenticationMethod.SIMPLE_TOKEN,
                AuthenticationMethod.SIMPLE_BASIC_AUTH
        )

        val expectedResult = arrayOf(
                SimpleWebSecurityConfigurer::class.qualifiedName,
                TokenAuthConfig::class.qualifiedName,
                BasicAuthConfig::class.qualifiedName
        )

        // MOCKITO FIXTURES
        given(importingClassMetadata.getAnnotationAttributes(
                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
        ).willReturn(mapOf("methods" to enabledMethodsArray))

        // OBJECT OF TESTED CLASS
        val enabledEntrypointsSelector = EnabledEntrypointsSelector()

        // ACT
        val configurationClasses = enabledEntrypointsSelector.selectImports(importingClassMetadata)

        // ASSERT
        assertThat(configurationClasses).containsExactlyInAnyOrder(*expectedResult)
    }

    @Test
    fun `selectImports(), SIMPLE_NO_METHOD enabled`() {

        // DATA ARRANGEMENTS
        val enabledMethodsArray = arrayOf(AuthenticationMethod.SIMPLE_NO_METHOD)

        val expectedResult = arrayOf(
                SimpleWebSecurityConfigurer::class.qualifiedName,
                DisableBasicAuthConfig::class.qualifiedName
        )


        // MOCKITO FIXTURES
        given(importingClassMetadata.getAnnotationAttributes(
                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
        ).willReturn(mapOf("methods" to enabledMethodsArray))

        // OBJECT OF TESTED CLASS
        val enabledEntrypointsSelector = EnabledEntrypointsSelector()

        // ACT
        val configurationClasses = enabledEntrypointsSelector.selectImports(importingClassMetadata)

        // ASSERT
        assertThat(configurationClasses).containsExactlyInAnyOrder(*expectedResult)
    }

    @Test
    fun `selectImports(), SIMPLE_NO_METHOD and SIMPLE_TOKEN and SIMPLE_BASIC_AUTH enabled`() {

        // DATA ARRANGEMENTS
        val enabledMethodsArray = arrayOf(
                AuthenticationMethod.SIMPLE_NO_METHOD,
                AuthenticationMethod.SIMPLE_TOKEN,
                AuthenticationMethod.SIMPLE_BASIC_AUTH
        )

        val expectedResult = arrayOf(
                SimpleWebSecurityConfigurer::class.qualifiedName,
                DisableBasicAuthConfig::class.qualifiedName
        )

        // MOCKITO FIXTURES
        given(importingClassMetadata.getAnnotationAttributes(
                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
        ).willReturn(mapOf("methods" to enabledMethodsArray))

        // OBJECT OF TESTED CLASS
        val enabledEntrypointsSelector = EnabledEntrypointsSelector()

        // ACT
        val configurationClasses = enabledEntrypointsSelector.selectImports(importingClassMetadata)

        // ASSERT
        assertThat(configurationClasses).containsExactlyInAnyOrder(*expectedResult)
    }

    @Test
    fun `selectImports(), SIMPLE_BASIC_AUTH and SIMPLE_METHOD and SIMPLE_NO_METHOD enabled`() {

        // DATA ARRANGEMENTS
        val enabledMethodsArray = arrayOf(
                AuthenticationMethod.SIMPLE_BASIC_AUTH,
                AuthenticationMethod.SIMPLE_TOKEN,
                AuthenticationMethod.SIMPLE_NO_METHOD
        )

        val expectedResult = arrayOf(
                SimpleWebSecurityConfigurer::class.qualifiedName,
                DisableBasicAuthConfig::class.qualifiedName
        )

        // MOCKITO FIXTURES
        given(importingClassMetadata.getAnnotationAttributes(
                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
        ).willReturn(mapOf("methods" to enabledMethodsArray))

        // OBJECT OF TESTED CLASS
        val enabledEntrypointsSelector = EnabledEntrypointsSelector()

        // ACT
        val configurationClasses = enabledEntrypointsSelector.selectImports(importingClassMetadata)

        // ASSERT
        assertThat(configurationClasses).containsExactlyInAnyOrder(*expectedResult)
    }
}