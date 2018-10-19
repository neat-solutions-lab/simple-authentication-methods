package nsl.test.bufferedreader

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.FileReader

@Tag("exploratory")
class BufferedReaderExploration {

    @Test
    fun test() {
        val tmpFile = createTempFile()
        println("tmpFile: ${tmpFile.absoluteFile}")
        val bufferedReader = BufferedReader(FileReader(tmpFile.absoluteFile))
        while(true) {
            val line = bufferedReader.readLine()
            println("line: $line")
            if(line == null) break
        }
        bufferedReader.close()
        println("after close()")
        Thread.sleep(100000)
        println("hello")
    }

}