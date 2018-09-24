package nsl.sam.dynamic

class DynamicClassLoader: ClassLoader() {
    fun defineClass(name:String, bytesArray: ByteArray): Class<*> {
        return defineClass(name, bytesArray, 0, bytesArray.size)
    }
}