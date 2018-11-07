package nsl.sam.functional.userssource.factory

import nsl.sam.core.annotation.EnableAnnotationAttributesExtractor
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.basicauth.userssource.factory.InMemoryUsersSourceFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [InMemoryUsersSourceFactoryTestConfiguration::class])
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(properties = [
    "nsl.sam.passwords-file=src/functional-test/config/passwords.conf"])
class InMemoryUsersSourceFactoryFT {
    companion object {
        var importingClassMetadata: AnnotationMetadata? = null
    }

    @Autowired
    lateinit var environment: Environment

    @Test
    fun factoryCreatesSourceWithUsersFromPasswordsFile() {
        val enableAnnotationAttributes = EnableAnnotationAttributesExtractor.extractAttributes(
                importingClassMetadata!!
        )
        val factory = InMemoryUsersSourceFactory()

        val inMemoryUsersSource = factory.create(enableAnnotationAttributes, environment)

        val passwordAndRoles = inMemoryUsersSource.getUserTraits("test")

        Assertions.assertThat(passwordAndRoles.password).isEqualTo("{noop}test")
    }
}


class InMemoryUsersSourceFactoryTestImportBeanDefinitionRegistrar   : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        InMemoryUsersSourceFactoryFT.importingClassMetadata = importingClassMetadata
    }
}

@Configuration
@EnableSimpleAuthenticationMethods
@Import(InMemoryUsersSourceFactoryTestImportBeanDefinitionRegistrar::class)
class InMemoryUsersSourceFactoryTestConfiguration