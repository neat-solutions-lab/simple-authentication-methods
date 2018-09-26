package nsl.sam.spring.config.ordering

import org.junit.jupiter.api.Test

internal class OrderingManagerWrapperTest {

    @Test
    fun wrappedInstancesTheSameWhenGetFromTheSameWrapper() {
        val wrappedOrderingManager = OrderingManagerWrapper("unique-key")
        val orderingManager1 = wrappedOrderingManager.getObj()
        val orderingManager2 = wrappedOrderingManager.getObj()
        assert(orderingManager1 === orderingManager2)
    }

    @Test
    fun wrappedInstancesTheSameWhenGetFromTheWrappersOfTheSameKeys() {
        val wrappedOrderingManager1 = OrderingManagerWrapper("unique-key-1")
        val orderingManager1 = wrappedOrderingManager1.getObj()

        val wrappedOrderingManager2 = OrderingManagerWrapper("unique-key-1")
        val orderingManager2 = wrappedOrderingManager2.getObj()
        assert(orderingManager1 === orderingManager2)
    }

    @Test
    fun wrappedInstancesDifferentWhenGetFromTheWrappersOfDifferentKeys() {
        val wrappedOrderingManager1 = OrderingManagerWrapper("unique-key-1")
        val orderingManager1 = wrappedOrderingManager1.getObj()

        val wrappedOrderingManager2 = OrderingManagerWrapper("unique-key-2")
        val orderingManager2 = wrappedOrderingManager2.getObj()
        assert(orderingManager1 !== orderingManager2)
    }

}