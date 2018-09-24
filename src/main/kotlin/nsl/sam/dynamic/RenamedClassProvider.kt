package nsl.sam.dynamic

object RenamedClassProvider {

    private val dynamicClassLoader = DynamicClassLoader()

    fun getRenamedClass(originalClass: Class<*>, newName: String): Class<*> {
        val renamedClassBytesSource = RenamedClassBytesSource(newName, originalClass)
        return dynamicClassLoader.defineClass(newName, renamedClassBytesSource.getBytes())
    }

}