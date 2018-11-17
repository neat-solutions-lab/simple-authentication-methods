package nsl.sam.instrumentation

import nsl.sam.asm.commons.ClassRemapper
import nsl.sam.asm.commons.SimpleRemapper
import nsl.sam.logger.logger
import org.springframework.asm.ClassReader
import org.springframework.asm.ClassWriter


class RenamedClassBytesSource(val newName: String, val originalClass: Class<*>) {

    companion object {
        val log by logger()
    }

    fun getBytes(): ByteArray {
        val classWriter = ClassWriter(0)
        val classRemapper = ClassRemapper(
                classWriter,
                SimpleRemapper(
                        originalClass.canonicalName.replace('.', '/'),
                        newName.replace('.', '/')
                )
        )

        val classBytesAsStream = Thread.currentThread().contextClassLoader.getResourceAsStream(originalClass.canonicalName.replace('.', '/') + ".class")
        val classReader = ClassReader(classBytesAsStream)
        classReader.accept(classRemapper, 0)
        return classWriter.toByteArray()
    }

}
