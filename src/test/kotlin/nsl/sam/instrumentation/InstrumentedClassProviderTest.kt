package nsl.sam.instrumentation

import nsl.sam.spring.config.InstrumentedWebSecurityConfigurerTemplate
import org.junit.jupiter.api.Test

internal class InstrumentedClassProviderTest {

    @Test
    fun getRenamedClass() {

        val changedClass = InstrumentedClassProvider.getRenamedClass(InstrumentedWebSecurityConfigurerTemplate::class.java, "changed.name")

        println("changedClass: $changedClass")

        println("compontent type: ${changedClass.componentType}")

        //val classObj = Class<InstrumentedWebSecurityConfigurerTemplate>
    }


    @Test
    fun dummyObjectTest() {

        val dummyObj = DummyObject("one", "two")
        println("dummyObj: $dummyObj")

        val renamedDummyObjClass = InstrumentedClassProvider.getRenamedClass(DummyObject::class.java, "some.changed.Obj")
        println("renamedDummyObjClass: ${renamedDummyObjClass}")

        //val cons = renamedDummyObjClass.getConstructor(String::class.java, String::class.java)
        //println("constructor $cons")

    }

}