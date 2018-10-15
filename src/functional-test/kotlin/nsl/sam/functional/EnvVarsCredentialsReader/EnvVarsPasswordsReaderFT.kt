package nsl.sam.functional.EnvVarsCredentialsReader

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.envvar.SteeredEnvironmentVariablesAccessor
import nsl.sam.importer.reader.impl.EnvironmentCredentialsReader
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.usersimporter.interim.extractor.PasswordsArrayEnvVarExtractor
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [EnvVarsPasswordsReaderFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class EnvVarsPasswordsReaderFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null

        @BeforeAll
        @JvmStatic
        fun setSystemProperties() {
            System.setProperty(
                    SteeredEnvironmentVariablesAccessor.SUPPLIER_PROPERTY_NAME,
                    EnvironmentVariablesSupplier::class.qualifiedName
            )
        }

        @AfterAll
        @JvmStatic
        fun clearSystemProperties() {
            System.clearProperty(SteeredEnvironmentVariablesAccessor.SUPPLIER_PROPERTY_NAME)
        }
    }

    /**
     * In this test one credential is to be omitted (not returned by reader) because this credential
     * begins with '#' and therefore is treated as comment and not valid credential
     */
    @Test
    fun readFiveCredentialsFromEnvironmentVariablesAndOmitOneWhichStartsWithHashSign() {
        val enableAnnotationAttributes = EnableAnnotationAttributesExtractor.extractAttributes(
                importingClassMetadata!!
        )
        println("enableAnnotationAttributes: $enableAnnotationAttributes")

        val extractor = PasswordsArrayEnvVarExtractor(SteeredEnvironmentVariablesAccessor())

        val reader = EnvironmentCredentialsReader(enableAnnotationAttributes, extractor)

        var credentialsCounter = 0

        reader.reset()
        while(true) {
            val credentials = reader.readCredentials()
            println("credentials: $credentials")
            if (credentials == null) break
            credentialsCounter++
        }
        Assertions.assertThat(credentialsCounter).isEqualTo(5)
    }
}

class EnvVarsPasswordsReaderFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        EnvVarsPasswordsReaderFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(usersEnvPrefix = "TestAppUsers")
@Import(EnvVarsPasswordsReaderFTImportBeanDefinitionRegistrar::class)
class EnvVarsPasswordsReaderFTConfiguration