package nsl.sam.functional.usersimporter

import nsl.sam.method.basicauth.usersimporter.impl.FileUsersImporter
import org.junit.jupiter.api.Test

class CorruptedPasswordsFileFileUsersImporterFT {

    @Test
    fun test() {

        //TODO: finish it after changes to UsersImporter(s)
        val fileUsersImporter = FileUsersImporter("src/functional-test/config/corrupted-passwords.conf")
        fileUsersImporter.reset()
        //fileUsersImporter.use {
        //    for(user in it) {
        //        println("user: $user")
        //    }
        //}

    }

}