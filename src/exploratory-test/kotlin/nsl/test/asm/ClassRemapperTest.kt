package nsl.test.asm

import nsl.sam.instrumentation.InstrumentedClassLoader
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.SimpleRemapper
import org.objectweb.asm.ClassReader

@Tag("exploratory")
class ClassRemapperTest {

    @Test
    fun remapperTest() {
        val classWriter = ClassWriter(0)
        val classRemapper = ClassRemapper(
                classWriter,
                SimpleRemapper(
                        DummyObject::class.java.canonicalName.replace('.', '/'),
                        "changed/ObjName")
        )
        val classReader = ClassReader(DummyObject::class.java.canonicalName)
        classReader.accept(classRemapper, 0)

        val resultClass = InstrumentedClassLoader().defineClass("changed.ObjName", classWriter.toByteArray())
        println("resultClass: $resultClass")


        val constructor = resultClass.getConstructor(String::class.java, String::class.java)
        val changedObj = constructor.newInstance("one", "two")
        println("changed obj: $changedObj")

        val castedObj = changedObj as DummyInterface
        castedObj.dummyFunction()
    }

}