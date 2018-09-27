package nsl.sam.dynamic

import nsl.sam.spring.config.DynamicWebSecurityConfigurerTemplate
import org.junit.jupiter.api.Test

internal class RenamedClassProviderTest {

    @Test
    fun getRenamedClass() {

        val changedClass = RenamedClassProvider.getRenamedClass(DynamicWebSecurityConfigurerTemplate::class.java, "changed.name")

        println("changedClass: $changedClass")

        println("compontent type: ${changedClass.componentType}")

        //val classObj = Class<DynamicWebSecurityConfigurerTemplate>
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