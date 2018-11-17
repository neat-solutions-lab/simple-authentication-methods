package nsl.sam.instrumentation

import nsl.sam.logger.logger

class InstrumentedClassLoader(parent: ClassLoader) : ClassLoader(parent) {

    companion object {
        val log by logger()
    }

    fun defineClass(name: String, bytesArray: ByteArray): Class<*> {
        try {
            return defineClass(name, bytesArray, 0, bytesArray.size)
        } catch (e: Exception) {
            log.error("Failed to define new class; " +
                      "name: $name, bytesArray: $bytesArray, bytes number: ${bytesArray.size}", e)
            throw e
        } catch (e: Error) {
            log.error("Failed to define new class; " +
                    "name: $name, bytesArray: $bytesArray, bytes number: ${bytesArray.size}", e)
            throw e
        }
    }
}