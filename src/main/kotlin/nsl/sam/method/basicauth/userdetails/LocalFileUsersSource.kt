package nsl.sam.method.basicauth.userdetails

import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UsernameNotFoundException

class LocalFileUsersSource(@Autowired private val passwordsFilePath: String): UsersSource {

    override fun usersNumber(): Int {
        if(!isAvailable()) return 0
        return usersNumberInternal()
    }

    private fun usersNumberInternal(): Int {
        val localUsersImporter = LocalFileUsersImporter(passwordsFilePath)
        var usersCounter = 0
        while (localUsersImporter.hasNext()) {
            usersCounter++
            localUsersImporter.next()
        }
        return usersCounter
    }


    companion object { private val log by logger() }

    private val usersPasswordsAndRolesMap: MutableMap<String, Pair<String, Array<String>>> = mutableMapOf()

    init {
        if(passwordsFilePath.isNotBlank()) importUsersFromFile()
    }

    override fun getUserPasswordAndRoles(username: String): Pair<String, Array<String>> {
        return usersPasswordsAndRolesMap[username] ?: throw UsernameNotFoundException("Failed to find ${username} username")
    }

    override fun isAvailable(): Boolean {
        if(passwordsFilePath.isBlank()) return false
        return (usersNumberInternal() > 0)
    }


    private fun importUsersFromFile() {
        LocalFileUsersImporter(passwordsFilePath).use { importer ->
            for((user, pass, roles) in importer) {
                log.debug("Adding to local map user: ${user}")
                usersPasswordsAndRolesMap[user] = Pair(pass, roles)
            }
        }
    } // importUsersFromFile()
}
