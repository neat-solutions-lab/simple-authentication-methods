package nsl.sam.spring.annotation

import nsl.sam.spring.config.BasicAuthConfig
import nsl.sam.spring.config.DisableBasicAuthConfig
import nsl.sam.spring.config.SimpleWebSecurityConfigurer
import nsl.sam.spring.config.TokenAuthConfig
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.core.type.AnnotationMetadata

class EnabledEntrypointsSelectorJupiterTest {

    companion object {

        @JvmStatic
        @Suppress("unused")
        fun selectImportsTestParameters(): List<Arguments> {
            val argumentsRepository = ArgumentsRepository()

            argumentsRepository.add(
                    arrayOf(AuthenticationMethod.SIMPLE_BASIC_AUTH),
                    arrayOf(SimpleWebSecurityConfigurer::class.qualifiedName,
                            BasicAuthConfig::class.qualifiedName)
            )

            argumentsRepository.add(
                    arrayOf(AuthenticationMethod.SIMPLE_TOKEN),
                    arrayOf(SimpleWebSecurityConfigurer::class.qualifiedName,
                            TokenAuthConfig::class.qualifiedName,
                            DisableBasicAuthConfig::class.qualifiedName)
            )

            argumentsRepository.add(
                    arrayOf(AuthenticationMethod.SIMPLE_TOKEN,
                            AuthenticationMethod.SIMPLE_BASIC_AUTH),
                    arrayOf(SimpleWebSecurityConfigurer::class.qualifiedName,
                            TokenAuthConfig::class.qualifiedName,
                            BasicAuthConfig::class.qualifiedName)
            )

            return argumentsRepository.getArgumentList()
        } // selectImportsTestParameters()

    } // companion object

    @Mock
    lateinit var importingClassMetadata: AnnotationMetadata

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

    @ParameterizedTest(name = "selectImportsTest {0}")
    @MethodSource("selectImportsTestParameters")
    fun selectImportsTest(
            enableAnnotationAnnotations: Array<AuthenticationMethod>,
            selectedConfigurationClasses: Array<String>) {

        // MOCKITO FIXTURES
        BDDMockito.given(importingClassMetadata.getAnnotationAttributes(
                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
        ).willReturn(mapOf("methods" to enableAnnotationAnnotations))

        // OBJECT OF TESTED CLASS
        val enabledEntrypointsSelector = EnabledEntrypointsSelector()

        // ACT
        val configurationClasses = enabledEntrypointsSelector.selectImports(importingClassMetadata)

        // ASSERT
        Assertions.assertThat(configurationClasses).containsExactlyInAnyOrder(*selectedConfigurationClasses)

    }

}

class ArgumentsRepository {
    val arguments: MutableList<Arguments> = mutableListOf()

    fun add(vararg arguments: Any) {
        this.arguments.add(Arguments.arguments(*arguments))
    }

    fun getArgumentList(): List<Arguments> = this.arguments
}

