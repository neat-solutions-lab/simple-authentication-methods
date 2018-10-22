package nsl.sam.importer.reader

import nsl.sam.importer.reader.impl.FileCredentialsReader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FileCredentialsReaderTest {

    @Test
    fun underlyingFileCanBeRemovedAfterFileCredentialsReaderIsClosed() {

        val tmpFile = createTempFile()
        val fileCredentialsReader = FileCredentialsReader(tmpFile.absolutePath)
        fileCredentialsReader.reset()
        fileCredentialsReader.readCredentials()


        fileCredentialsReader.close()
        val deleteResult = tmpFile.delete()

        Assertions.assertThat(deleteResult).isEqualTo(true)
    }
}
