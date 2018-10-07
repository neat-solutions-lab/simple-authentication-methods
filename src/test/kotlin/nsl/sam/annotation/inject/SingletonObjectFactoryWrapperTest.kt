package nsl.sam.annotation.inject

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class SingletonObjectFactoryWrapperTest {

    @Test
    fun differentTargetObjectsWhenNoWrapperIsUsed() {
        val itJokesDispenserFactory = ItJokesDispenserFactory()

        val jokesDispenser1 = itJokesDispenserFactory.create()
        val jokesDispenser2 = itJokesDispenserFactory.create()

        Assertions.assertThat(jokesDispenser1).isNotSameAs(jokesDispenser2)
    }

    @Test
    fun sameTargetObjectsWhenWrapperIsUsed() {

        val itJokesDispenserFactory = SingletonObjectFactoryWrapper(ItJokesDispenserFactory())
        val jokesDispenser1 = itJokesDispenserFactory.create()
        val jokesDispenser2 = itJokesDispenserFactory.create()

        Assertions.assertThat(jokesDispenser1).isSameAs(jokesDispenser2)
    }
}