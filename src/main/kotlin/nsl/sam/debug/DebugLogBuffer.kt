package nsl.sam.debug

object DebugLogBuffer {

    private var logsBuilder: StringBuilder? = StringBuilder()

    fun reset() {
        logsBuilder = StringBuilder()
    }

    fun log(msg: String) {
        logsBuilder?.append(msg + "\n")
    }


    fun getLogs(): String {
        return logsBuilder.toString()
    }

}