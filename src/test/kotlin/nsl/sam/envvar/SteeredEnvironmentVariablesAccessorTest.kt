package nsl.sam.envvar

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class SteeredEnvironmentVariablesAccessorTest {

    @Test
    fun variablesProvidedBySteeredAccessorAreSameAsReturnedByProvider() {

        System.setProperty(
                SteeredEnvironmentVariablesAccessor.SUPPLIER_PROPERTY_NAME,
                TestingEnvVarsSupplier::class.qualifiedName
        )

        val envVarsAccessor = SteeredEnvironmentVariablesAccessor()
        val retrievedMap = envVarsAccessor.getVarsMap()
        for( (key, value) in retrievedMap) {
            println("key: $key, value: $value")
        }

        val expectedMap = TestingEnvVarsSupplier().get()

        Assertions.assertThat(retrievedMap).isEqualTo(expectedMap)
    }

}