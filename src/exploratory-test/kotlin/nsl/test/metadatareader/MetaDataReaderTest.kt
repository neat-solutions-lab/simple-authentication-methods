package nsl.test.metadatareader

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@Tag("exploratory")
@SpringJUnitConfig(classes =
[TestConfigurationOne::class, TestConfigurationTwo::class, TestConfigurationThree::class])
class MetaDataReaderTest {

    @Test
    fun metaDataReaderTest() {

        println("Let's load the context and read attributes of the @EnableNothing annotations.")
        println("The attributes are read and analyzed within ${DynamicBeanDefinitionImporter::class.qualifiedName} class")

    }

}

@Configuration
@EnableNothing
class TestConfigurationOne

@Configuration
@EnableNothing
class TestConfigurationTwo

@Configuration
@EnableNothing
class TestConfigurationThree
