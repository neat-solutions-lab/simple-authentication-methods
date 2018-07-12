package nsl.sam

import nsl.sam.logger.logger
import org.junit.Test

class ExampleTest {

    companion object { val log by logger() }

    @Test
    fun checkIt() {
        log.info("OK")
        println("AAAAAAAAA")
    }

}