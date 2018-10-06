package nsl.sam.instrumentation

import nsl.sam.core.config.InstrumentedWebSecurityConfigurerTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class InstrumentedClassProviderTest {

    @Test
    fun renamedClassCanonicalNameTest() {

        val changedClass = InstrumentedClassProvider.generateRenamedClass(
                InstrumentedWebSecurityConfigurerTemplate::class.java,
                "changed.name"
        )
        Assertions.assertThat(changedClass.canonicalName).isEqualTo("changed.name")
    }

    @Test
    fun renamedDummyObjectTest() {

        val dummyObj = DummyObject("one", "two")

        val renamedDummyObjClass = InstrumentedClassProvider.generateRenamedClass(
                DummyObject::class.java,
                "some.changed.Obj"
        )

        val constructor = renamedDummyObjClass.getConstructor(String::class.java, String::class.java)
        val changedObj = constructor.newInstance("one", "two") as DummyInterface

        Assertions.assertThat(renamedDummyObjClass.canonicalName).isEqualTo("some.changed.Obj")
        Assertions.assertThat(dummyObj.fieldOne).isEqualTo(changedObj.fieldOne)
        Assertions.assertThat(dummyObj.fieldTwo).isEqualTo(changedObj.fieldTwo)
    }
}
