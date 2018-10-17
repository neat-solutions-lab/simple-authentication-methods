package nsl.sam.changes.resource

import nsl.sam.changes.ChangeEvent
import nsl.sam.changes.ChangeListener
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class FileChangeDetectorTest {

    @Test
    fun fileChangesDetectedWhenFileChangeDetectorWithChangeListenerUsed() {
        val tmpFile = createTempFile()
        val fileChangeDetector = FileChangeDetector(tmpFile.absolutePath)

        var changesCounter = 0

        fileChangeDetector.addChangeListener(object : ChangeListener<String> {
            override fun onChangeDetected(changeEvent: ChangeEvent<String>) {
                changesCounter++
                println("change detected: ${changeEvent.changeTimestamp}, ${changeEvent.resource}")
            }
        })

        tmpFile.writeText("First change")
        fileChangeDetector.run()
        fileChangeDetector.run()
        fileChangeDetector.run()

        tmpFile.writeText("Second change")

        fileChangeDetector.run()

        println("tmp file path: ${tmpFile.absolutePath}")

        // third change
        tmpFile.delete()
        fileChangeDetector.run()
        fileChangeDetector.run()
        fileChangeDetector.run()

        // fourth change
        tmpFile.createNewFile()
        fileChangeDetector.run()
        fileChangeDetector.run()

        tmpFile.writeText("fifth change")
        fileChangeDetector.run()

        Assertions.assertThat(changesCounter).isEqualTo(5)

        println("changes counter: $changesCounter")
    }
}