package nsl.test.cglib

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.cglib.proxy.Enhancer
import org.springframework.cglib.proxy.MethodInterceptor
import org.springframework.cglib.proxy.MethodProxy
import java.lang.reflect.Method

@Tag("exploratory")
class EnhancerTest {

    @Test
    fun test() {
        val enhancer = Enhancer()
        enhancer.setSuperclass(ControlledClass::class.java)
        enhancer.setCallbackType(Interceptor::class.java)
        val enhancedClass = enhancer.createClass()
        println("enhanced class: $enhancedClass")
    }
}

class Interceptor : MethodInterceptor {
    override fun intercept(obj: Any, method: Method, args: Array<out Any>, proxy: MethodProxy): Any {
        return proxy.invokeSuper(obj, args)
    }

}

open class ControlledClass(val name: String)