package nsl.sam.functional.AnnotationCredentialsReader

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.importer.reader.impl.AnnotationCredentialsReader
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.basicauth.usersimporter.interim.extractor.PasswordsArrayAnnotationExtractor
import org.assertj.core.api.Assertions
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
        classes = [PasswordsCredentialsReaderFTConfiguration::class])
@AutoConfigureMockMvc(secure = false)
class EnvVarsPasswordsReaderFT {

    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    /**
     * In this test one credential has to be omitted because the assumption
     * is that credentials which begin with '#' sign are skipped
     */
    @Test
    fun threeCredentialsGetAndOneOmittedFromAnnotationAttribute() {
        val enableAnnotationAttributes = EnableAnnotationAttributesExtractor.extractAttributes(
                importingClassMetadata!!
        )
        println("enableAnnotationAttributes: $enableAnnotationAttributes")

        val extractor = PasswordsArrayAnnotationExtractor()

        val reader = AnnotationCredentialsReader(enableAnnotationAttributes, extractor)

        var credentialsCounter = 0

        reader.reset()
        while (true) {
            val credentials = reader.readCredentials()
            println("credentials: $credentials")
            if(null == credentials) break
            credentialsCounter++
        }

        Assertions.assertThat(credentialsCounter).isEqualTo(3)
    }

}

class PasswordsCredentialsReaderFTImportBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        EnvVarsPasswordsReaderFT.importingClassMetadata = importingClassMetadata
    }

}

@Configuration
@EnableSimpleAuthenticationMethods
@SimpleBasicAuthentication(users =
[
    "hardcoded-user1:{noop}test USER ADMIN",
    "hardcoded-user2:{noop}test USER ADMIN",
    "#hardcoded-user3:{noop}test USER ADMIN",
    "hardcoded-user4:{noop}test USER ADMIN"
])
@Import(PasswordsCredentialsReaderFTImportBeanDefinitionRegistrar::class)
class PasswordsCredentialsReaderFTConfiguration
