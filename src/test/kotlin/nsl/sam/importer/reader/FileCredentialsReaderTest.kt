package nsl.sam.importer.reader

import nsl.sam.importer.reader.impl.FileCredentialsReader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FileCredentialsReaderTest {




    @Test
    fun underlyingFileCanBeRemovedOnlyAfterFileCredentialsReaderIsClosed() {
        val tmpFile = createTempFile()
        val fileCredentialsReader = FileCredentialsReader(tmpFile.absolutePath)
        fileCredentialsReader.reset()
        fileCredentialsReader.readCredentials()
        var deleteResult = tmpFile.delete()
        println("delete result: $deleteResult")

        /*
         * as the FileCredentialsReader has not been closed, the tmpFile cannot be deleted
         */
        Assertions.assertThat(deleteResult).isEqualTo(false)

        fileCredentialsReader.close()
        deleteResult = tmpFile.delete()

        /*
         * now the file could be removed (becuae FileCredentialsReader has been closed)
         */
        Assertions.assertThat(deleteResult).isEqualTo(true)

    }

}