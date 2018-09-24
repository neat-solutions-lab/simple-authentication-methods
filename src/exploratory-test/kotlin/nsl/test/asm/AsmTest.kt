package nsl.test.asm

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.asm.ClassReader
import org.springframework.asm.ClassWriter
import org.springframework.asm.Opcodes
import org.springframework.asm.Opcodes.ACC_PUBLIC
import org.springframework.asm.Opcodes.V1_7

@Tag("exploratory")
class AsmTest {

    @Test
    fun test() {

        val cr = ClassReader("nsl.sam.spring.config.DynamicWebSecurityConfigurer")
        println("bytes size: ${cr.b.size}")

        val cw = ClassWriter(cr, ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)

        val renamingClassVisitor = RenamingClassVisitor(Opcodes.ASM6, cw)

        cr.accept(renamingClassVisitor, 0)

        val testClass = DynamicClassLoader().defineClass(
                "nsl.sam.spring.config.DynamicWebSecurityConfigurer001", cw.toByteArray()
        )
        println("testClass: ${testClass}")
        println("class name: ${testClass.canonicalName}")
        println("superclass: ${testClass.superclass}")
    }

}