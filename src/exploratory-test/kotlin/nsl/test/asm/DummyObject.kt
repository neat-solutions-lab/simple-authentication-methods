package nsl.test.asm

class DummyObject(val propertyOne: String, val propertyTwo: String) : DummyInterface {
    override fun dummyFunction() {
        println("Hello from dummyFunction()")
    }
}