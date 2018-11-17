package nsl.sam.instrumentation

import org.slf4j.LoggerFactory

object InstrumentedClassProvider {

    val log = LoggerFactory.getLogger(this::class.java)

    private val dynamicClassLoader = InstrumentedClassLoader(Thread.currentThread().contextClassLoader)

    fun generateRenamedClass(originalClass: Class<*>, newName: String): Class<*> {
        val renamedClassBytesSource = RenamedClassBytesSource(newName, originalClass)
        val bytesSet = renamedClassBytesSource.getBytes()
        return dynamicClassLoader.defineClass(newName, bytesSet)
    }

}