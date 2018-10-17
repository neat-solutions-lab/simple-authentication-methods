package nsl.sam.changes.resource

import ch.qos.logback.classic.util.ContextInitializer
import nsl.sam.changes.ChangeEvent
import nsl.sam.changes.ChangeListener
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import java.io.File
import java.io.FileWriter

internal class FileChangeDetectorTest {

    var tmpFile: File? = null

    companion object {

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "src/test/logback/logback-debug.xml")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            System.clearProperty(ContextInitializer.CONFIG_FILE_PROPERTY)
        }
    }

    @BeforeEach
    fun beforeEach() {
        tmpFile = createTempFile()
    }

    @AfterEach
    fun afterEach() {
        tmpFile?.delete()
    }

    /**
     * NOTE: The resolution of the last change timestamps is 1 millisecond;
     *       ON GITLAB SHARED RUNNERS THE MODIFICATION TIME OF THE FILE USED BY THIS TEST IS NOT CHANGING
     *       (it was the case at least for the 18th October, 2018r; on those runners the test are passing
     *       only due to changes in file size, and not in modification time)
     */
    @Test
    fun fileChangesDetectedWhenFileChangeDetectorWithChangeListenerUsed() {

        val fileChangeDetector = FileChangeDetector(tmpFile!!.absolutePath)

        Thread.sleep(100)

        var fileWriter = FileWriter(tmpFile, true)

        var changesCounter = 0

        fileChangeDetector.addChangeListener(object : ChangeListener<String> {
            override fun onChangeDetected(changeEvent: ChangeEvent<String>) {
                changesCounter++
                println("change detected: ${changeEvent.changeTimestamp}, ${changeEvent.resource}")
            }
        })

        Thread.sleep(100)

        fileWriter.write("first change")
        fileWriter.flush()
        fileWriter.close()
        fileChangeDetector.run()

        Thread.sleep(100)

        // third change
        fileWriter = FileWriter(tmpFile, true)
        fileWriter.write("second change")
        fileWriter.flush()
        fileWriter.close()
        fileChangeDetector.run()

        Thread.sleep(100)

        fileWriter = FileWriter(tmpFile, true)
        fileWriter.write("third change")
        fileWriter.flush()
        fileWriter.close()

        fileChangeDetector.run()

        println("changes counter: $changesCounter")

        Assertions.assertThat(changesCounter).isEqualTo(3)
    }
}