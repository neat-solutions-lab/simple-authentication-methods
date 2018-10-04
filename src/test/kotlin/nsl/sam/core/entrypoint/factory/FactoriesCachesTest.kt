package nsl.sam.core.entrypoint.factory

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class FactoriesCachesTest {

    @Test
    fun itShouldBeOnlyOneInstanceOfFactoriesRegistryOfGivenType() {
        val factoriesCache1 = FactoriesCaches.getFactoriesRegistry(ImaginaryCommunicatorFactory::class)
        val factoriesCache2 = FactoriesCaches.getFactoriesRegistry(ImaginaryCommunicatorFactory::class)

        Assertions.assertThat(factoriesCache1 == factoriesCache2)
        Assertions.assertThat(factoriesCache1 === factoriesCache2)
    }

    @Test
    fun factoriesRegistersOfDifferentTypesCanBeRetrieved() {
        val factoriesCache1 = FactoriesCaches.getFactoriesRegistry(ImaginaryCommunicatorFactory::class)
        val factoriesCache2 = FactoriesCaches.getFactoriesRegistry(ImaginaryJokesDispenserFactory::class)

        Assertions.assertThat(factoriesCache1 != factoriesCache2)
        Assertions.assertThat(factoriesCache1 !== factoriesCache2)
    }

}