package nsl.sam.spring.config.ordering

import org.junit.jupiter.api.Test

internal class NamedConfigurationsOrderingHelperTest {

    @Test
    fun wrappedInstancesTheSameWhenGetFromTheSameWrapper() {
        val wrappedOrderingManager = NamedConfigurationsOrderingHelper("unique-key")
        val orderingManager1 = wrappedOrderingManager.getObj()
        val orderingManager2 = wrappedOrderingManager.getObj()
        assert(orderingManager1 === orderingManager2)
    }

    @Test
    fun wrappedInstancesTheSameWhenGetFromTheWrappersOfTheSameKeys() {
        val wrappedOrderingManager1 = NamedConfigurationsOrderingHelper("unique-key-1")
        val orderingManager1 = wrappedOrderingManager1.getObj()

        val wrappedOrderingManager2 = NamedConfigurationsOrderingHelper("unique-key-1")
        val orderingManager2 = wrappedOrderingManager2.getObj()
        assert(orderingManager1 === orderingManager2)
    }

    @Test
    fun wrappedInstancesDifferentWhenGetFromTheWrappersOfDifferentKeys() {
        val wrappedOrderingManager1 = NamedConfigurationsOrderingHelper("unique-key-1")
        val orderingManager1 = wrappedOrderingManager1.getObj()

        val wrappedOrderingManager2 = NamedConfigurationsOrderingHelper("unique-key-2")
        val orderingManager2 = wrappedOrderingManager2.getObj()
        assert(orderingManager1 !== orderingManager2)
    }

}