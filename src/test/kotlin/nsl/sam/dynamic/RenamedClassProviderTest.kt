package nsl.sam.dynamic

import nsl.sam.spring.config.DynamicWebSecurityConfigurer
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.platform.engine.support.filter.ClasspathScanningSupport

internal class RenamedClassProviderTest {

    @Test
    fun getRenamedClass() {

        val changedClass = RenamedClassProvider.getRenamedClass(DynamicWebSecurityConfigurer::class.java, "changed.name")

        println("changedClass: $changedClass")

        println("compontent type: ${changedClass.componentType}")

        //val classObj = Class<DynamicWebSecurityConfigurer>
    }


    @Test
    fun dummyObjectTest() {

        val dummyObj = DummyObject("one", "two")
        println("dummyObj: $dummyObj")

        val renamedDummyObjClass = RenamedClassProvider.getRenamedClass(DummyObject::class.java, "some.changed.Obj")
        println("renamedDummyObjClass: ${renamedDummyObjClass}")

        //val cons = renamedDummyObjClass.getConstructor(String::class.java, String::class.java)
        //println("constructor $cons")

    }

}