package nsl.test.asm

import org.springframework.asm.*

class RenamingClassVisitor(private val newName: String, flags: Int, visitor: ClassVisitor) : ClassVisitor(flags, visitor) {

    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        println(">>> visit()")
        println("version: $version, access: $access, name: $name, signature: $signature, superName: $superName, interfaces: $interfaces")
        println("interfaces size: ${interfaces?.size}")
        interfaces?.forEach { println("interface: $it") }
        super.visit(version, access, newName.replace('.', '/'), signature, superName, interfaces)

    }

    override fun visitTypeAnnotation(typeRef: Int, typePath: TypePath?, desc: String?, visible: Boolean): AnnotationVisitor {

        println(">>> visitTypeAnnotations()")
        println("typeRef: $typeRef, typePath: $typePath, desc: $desc, visible: $visible")
        return super.visitTypeAnnotation(typeRef, typePath, desc, visible)
    }

    override fun visitEnd() {
        println(">>> visitEnd()")
        super.visitEnd()
    }

    override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor {
        println(">>> visitAnnotation()")
        return super.visitAnnotation(desc, visible)
    }

    override fun visitAttribute(attr: Attribute?) {
        println(">>> visitAttribute()")
        super.visitAttribute(attr)
    }

    override fun visitField(access: Int, name: String?, desc: String?, signature: String?, value: Any?): FieldVisitor {
        println(">>> visitField(access: $access, name: $name, desc: $desc, signature: $signature, value: $value)")
        return super.visitField(access, name, desc, signature, value)
    }
}