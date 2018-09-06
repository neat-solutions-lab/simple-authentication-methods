package nsl.sam.spring.annotation

import kotlin.reflect.KClass

class ConfigurationClassesBuilder {

    private val classesNames: MutableList<String> = mutableListOf()


    fun add(clazz: KClass<*>): ConfigurationClassesBuilder {
        classesNames.add(clazz.qualifiedName!!)
        return this
    }

    fun addIfNoConflict(candidateClass: KClass<*>, confilictingClass: KClass<*>,
                        onAdded: ()->Unit = {},
                        onRejected: ()->Unit = {}): ConfigurationClassesBuilder {
        if (!classesNames.contains(confilictingClass.qualifiedName) ) {
            classesNames.add(candidateClass.qualifiedName!!)
            onAdded()
        } else {
            onRejected()
        }

        return this
    }

    fun build(): Array<String> {
        return classesNames.toTypedArray()
    }
}
