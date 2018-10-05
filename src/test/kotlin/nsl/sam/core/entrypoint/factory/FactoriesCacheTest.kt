package nsl.sam.core.entrypoint.factory

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class FactoriesCacheTest {

//    @Test
//    fun callOnCommunicatorsTest() {
//
//        val factories = FactoriesCache(ImaginaryCommunicatorFactory::class)
//
//        factories.getOrPut(LocalCommunicatorFactory::class) {
//            LocalCommunicatorFactory()
//        }
//
//        factories.getOrPut(RemoteCommunicatorFactory::class) {
//            RemoteCommunicatorFactory()
//        }
//
//        val localCommunicator = factories.get(LocalCommunicatorFactory::class).create()
//        val remoteCommunicator = factories.get(RemoteCommunicatorFactory::class).create()
//
//        localCommunicator.send("Hello local World!")
//        remoteCommunicator.send("Hello remote World!")
//
//    }
//
//    @Test
//    fun getOrPutTest() {
//
//        val factories = FactoriesCache(ImaginaryCommunicatorFactory::class)
//
//        val localCommunicatorFactory1 = factories.getOrPut(LocalCommunicatorFactory::class) {
//            LocalCommunicatorFactory()
//        }
//
//        val remoteCommunicatorFactory1 = factories.getOrPut(RemoteCommunicatorFactory::class) {
//            RemoteCommunicatorFactory()
//        }
//
//
//        val localCommunicatorFactory2 = factories.getOrPut(LocalCommunicatorFactory::class) {
//            LocalCommunicatorFactory()
//        }
//
//        val remoteCommunicatorFactory2 = factories.getOrPut(RemoteCommunicatorFactory::class) {
//            RemoteCommunicatorFactory()
//        }
//
//        val localCommunicatorFactory3 = factories.get(LocalCommunicatorFactory::class)
//        val remoteCommunicatorFactory3 = factories.get(RemoteCommunicatorFactory::class)
//
//        Assertions.assertThat(localCommunicatorFactory1 === localCommunicatorFactory2)
//        Assertions.assertThat(localCommunicatorFactory1 === localCommunicatorFactory3)
//
//        Assertions.assertThat(remoteCommunicatorFactory1 === remoteCommunicatorFactory2)
//        Assertions.assertThat(remoteCommunicatorFactory1 === remoteCommunicatorFactory3)
//
//        Assertions.assertThat(localCommunicatorFactory1 == localCommunicatorFactory2)
//        Assertions.assertThat(localCommunicatorFactory1 == localCommunicatorFactory3)
//
//        Assertions.assertThat(remoteCommunicatorFactory1 == remoteCommunicatorFactory2)
//        Assertions.assertThat(remoteCommunicatorFactory1 == remoteCommunicatorFactory3)
//    }
}