package nsl.test.spel

import kotlin.math.atan

class EvaluationRoot {

    fun methodOnEvaluationRoot(arg: String) {
        println("methodOnEvaluationRoot(): $arg")
    }

    fun echo(arg: String): EvaluationRoot {
        println(arg)
        return this
    }

    fun delegateVarargsMethod(vararg arguments: String) {
        println("delegateVarargsMethod(): $arguments")
    }

    fun varargsMethod(vararg arguments: String) {
        print("args: ")
        arguments.asSequence().map { "$it " }.forEach(::print)
        println()
        delegateVarargsMethod(*arguments)
    }

}