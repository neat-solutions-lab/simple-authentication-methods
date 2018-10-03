package nsl.sam.instrumentation

object InstrumentedClassProvider {

    private val dynamicClassLoader = InstrumentedClassLoader()

    fun generateRenamedClass(originalClass: Class<*>, newName: String): Class<*> {
        val renamedClassBytesSource = RenamedClassBytesSource(newName, originalClass)
        return dynamicClassLoader.defineClass(newName, renamedClassBytesSource.getBytes())
    }

}