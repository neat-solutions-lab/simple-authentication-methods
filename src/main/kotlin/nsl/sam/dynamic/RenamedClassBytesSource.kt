package nsl.sam.dynamic

import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.SimpleRemapper
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ASM6

class RenamedClassBytesSource(val newName: String, val originalClass: Class<*>) {

    fun getBytes():ByteArray {
        val classWriter = ClassWriter(0)
        val classRemapper = ClassRemapper(
                classWriter,
                SimpleRemapper(
                        originalClass.canonicalName.replace('.','/'),
                        newName.replace('.', '/')
                )
        )
        val classReader = ClassReader(originalClass.canonicalName)
        classReader.accept(classRemapper, 0)
        return classWriter.toByteArray()
    }

}