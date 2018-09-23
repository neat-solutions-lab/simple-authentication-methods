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

        //https://www.programcreek.com/java-api-examples/?class=net.sf.cglib.proxy.Enhancer&method=createClass
        //http://www.javased.com/?api=net.sf.cglib.proxy.Enhancer
        val enhancer = Enhancer()
        enhancer.setSuperclass(ControlledClass::class.java)
        enhancer.setCallbackType(Interceptor::class.java)
        val enhancedClass = enhancer.createClass()
        println("enhanced class: $enhancedClass")

    }

}

class Interceptor: MethodInterceptor {
    override fun intercept(obj: Any, method: Method, args: Array<out Any>, proxy: MethodProxy): Any {
        return proxy.invokeSuper(obj, args)
    }

}

open class ControlledClass(val name: String)