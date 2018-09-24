package nsl.sam.dynamic

import org.springframework.asm.ClassVisitor

class ClassRenamingVisitor(api: Int, visitor: ClassVisitor, val newName: String): ClassVisitor(api, visitor) {

    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, newName, signature, superName, interfaces)
    }

}