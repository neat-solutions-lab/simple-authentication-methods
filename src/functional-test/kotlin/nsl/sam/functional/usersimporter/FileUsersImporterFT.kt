package nsl.sam.functional.usersimporter

import nsl.sam.importer.reader.impl.FileCredentialsReader
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FileUsersImporterFT {

    @Test
    fun hasItemsMethodShouldNotKeepTheUnderlyingFileOpenForEver() {

        val tmpFile = createTempFile()
        val usersFileCredentialsImporter = PasswordsCredentialsImporter(FileCredentialsReader(tmpFile.absolutePath))
        usersFileCredentialsImporter.hasItems()
        val deleteResult = tmpFile.delete()

        /*
         * the temporary file can be deleted successfully only if it is not open by any process
         */
        Assertions.assertThat(deleteResult).isEqualTo(true)
        println("delete result: $deleteResult")
    }
}