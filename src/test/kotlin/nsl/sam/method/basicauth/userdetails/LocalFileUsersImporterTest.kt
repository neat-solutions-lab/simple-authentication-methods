package nsl.sam.method.basicauth.userdetails

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class LocalFileUsersImporterTest {

    @Test
    fun oneUserWithNoAnyAdditionalLines() {
        val importer = LocalFileUsersImporter("src/test/config/passwords-one-user.conf")
        val resultAccumulator = mutableListOf<Triple<String, String, Array<String>>>()
        for((user, password, roles) in importer) {
            resultAccumulator.add(Triple(user, password, roles))
            println("User details read from file: user: $user, password: $password, roles: $roles")
        }
        assertEquals(1, resultAccumulator.size)
    }

    @Test
    fun oneUserWithOneEmptyLineAtTop() {
        val importer = LocalFileUsersImporter("src/test/config/passwords-one-user-with-empty-line-at-top.conf")
        val resultAccumulator = mutableListOf<Triple<String, String, Array<String>>>()
        for((user, password, roles) in importer) {
            resultAccumulator.add(Triple(user, password, roles))
            println("User details read from file: user: $user, password: $password, roles: $roles")
        }
        assertEquals(1, resultAccumulator.size)
    }

    @Test
    fun oneUserWithOneEmptyLineAtBottom() {
        val importer = LocalFileUsersImporter("src/test/config/passwords-one-user-with-empty-line-at-bottom.conf")
        val resultAccumulator = mutableListOf<Triple<String, String, Array<String>>>()
        for((user, password, roles) in importer) {
            resultAccumulator.add(Triple(user, password, roles))
            println("User details read from file: user: $user, password: $password, roles: $roles")
        }
        assertEquals(1, resultAccumulator.size)
    }

    @Test
    fun emptyFileTest() {
        val importer = LocalFileUsersImporter("src/test/config/passwords-empty.conf")
        val resultAccumulator = mutableListOf<Triple<String, String, Array<String>>>()
        for((user, password, roles) in importer) {
            resultAccumulator.add(Triple(user, password, roles))
            println("User details read from file: user: $user, password: $password, roles: $roles")
        }
        assertEquals(0, resultAccumulator.size)
    }

    @Test
    fun onlyCommentsAndBlankLinesFileTest() {
        val importer = LocalFileUsersImporter("src/test/config/passwords-comments-and-blank-lines.conf")
        val resultAccumulator = mutableListOf<Triple<String, String, Array<String>>>()
        for((user, password, roles) in importer) {
            resultAccumulator.add(Triple(user, password, roles))
            println("User details read from file: user: $user, password: $password, roles: $roles")
        }
        assertEquals(0, resultAccumulator.size)
    }

    @Test
    fun mixedContentTest() {
        val importer = LocalFileUsersImporter("src/test/config/passwords-mixed.conf")
        val resultAccumulator = mutableListOf<Triple<String, String, Array<String>>>()
        for((user, password, roles) in importer) {
            resultAccumulator.add(Triple(user, password, roles))
            println("User details read from file: user: $user, password: $password, roles: $roles")
        }
        assertEquals(8, resultAccumulator.size)
    }

}