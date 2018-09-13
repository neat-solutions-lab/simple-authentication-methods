package nsl.test.interop

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("exploratory")
class JavaFromKotlinTest {

    @Test
    fun classTest() {
        println("Kotlin code calls java code")
        ClassUtility.printClass(this.javaClass)
    }

    @Test
    fun classesArrayTest() {
        ClassUtility.printClasses(arrayOf(this::class.java, this.javaClass))
    }

}