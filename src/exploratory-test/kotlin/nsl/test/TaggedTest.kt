package nsl.test

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("exploratory")
class TaggedTest {

    @Test
    fun test() {
        println("Someone has called me.")
    }

}