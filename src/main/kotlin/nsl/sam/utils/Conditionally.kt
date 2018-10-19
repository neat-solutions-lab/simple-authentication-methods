package nsl.sam.utils

class Conditionally(private val condition: Boolean) {
    operator fun invoke(block: ()->Unit): Unit {
        if(condition) {
            block()
        }
    }
}