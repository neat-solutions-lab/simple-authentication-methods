package nsl.test.asm

import nsl.sam.dynamic.DynamicClassLoader
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.asm.ClassReader
import org.springframework.asm.ClassWriter
import org.springframework.asm.Opcodes

@Tag("exploratory")
class AsmTest {

    @Test
    fun test() {

        val cr = ClassReader("nsl.sam.spring.config.DynamicWebSecurityConfigurerTemplate")
        println("bytes size: ${cr.b.size}")

        val cw = ClassWriter(0)

        val renamingClassVisitor = RenamingClassVisitor(
                "nsl.sam.spring.config.DynamicWebSecurityConfigurer001", Opcodes.ASM6, cw
        )

        cr.accept(renamingClassVisitor, 0)

        val testClass = DynamicClassLoader().defineClass(
                "nsl.sam.spring.config.DynamicWebSecurityConfigurer001", cw.toByteArray()
        )
        println("testClass: ${testClass}")
        println("class name: ${testClass.canonicalName}")
        println("superclass: ${testClass.superclass}")
    }


    @Test
    fun exampleBasedOnChapterTransformingClassesFromOfficialGuide() {

        val classWriter = ClassWriter(0)
        val classVisitor = RenamingClassVisitor(
                "nsl.sam.spring.config.DynamicWebSecurityConfigurer001",
                Opcodes.ASM6,
                classWriter
        )
        val classReader = ClassReader("nsl.sam.spring.config.DynamicWebSecurityConfigurerTemplate")
        classReader.accept(classVisitor, 0)

        val testClass = DynamicClassLoader().defineClass(
                "nsl.sam.spring.config.DynamicWebSecurityConfigurer001", classWriter.toByteArray()
        )

        println("testClass: $testClass")

    }

}