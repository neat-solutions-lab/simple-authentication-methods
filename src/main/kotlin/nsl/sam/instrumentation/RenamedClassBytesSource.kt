package nsl.sam.instrumentation

import nsl.sam.asm.commons.ClassRemapper
import nsl.sam.asm.commons.SimpleRemapper
import org.springframework.asm.ClassReader
import org.springframework.asm.ClassWriter
//import org.objectweb.asm.commons.ClassRemapper
//import org.objectweb.asm.commons.SimpleRemapper
//import org.objectweb.asm.ClassReader
//import org.objectweb.asm.ClassWriter

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