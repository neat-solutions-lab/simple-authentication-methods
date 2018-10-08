package nsl.sam.method.basicauth.userdetails

import nsl.sam.logger.logger
import nsl.sam.method.basicauth.userdetails.importer.LocalFileUsersImporter
import nsl.sam.method.basicauth.userdetails.importer.UsersImporter
import org.springframework.security.core.userdetails.UsernameNotFoundException

//class InMemoryUsersSource(@Autowired private val passwordsFilePath: String): UsersSource {
class InMemoryUsersSource(private val usersImporter: UsersImporter): UsersSource {

    companion object { private val log by logger() }

    private val usersPasswordsAndRolesMap: MutableMap<String, Pair<String, Array<String>>> = mutableMapOf()

    override fun usersNumber(): Int {
        if(!isAvailable()) return 0
        return usersNumberInternal()
    }

    private fun usersNumberInternal(): Int {
        //val localUsersImporter = LocalFileUsersImporter(passwordsFilePath)
        usersImporter.reset()
        var usersCounter = 0
        while (usersImporter.hasNext()) {
            usersCounter++
            usersImporter.next()
        }
        return usersCounter
    }

    init {
        // TODO: add UsersImporter.is...
        importUsersFromImporter()
    }

    override fun getUserPasswordAndRoles(username: String): Pair<String, Array<String>> {
        return usersPasswordsAndRolesMap[username] ?: throw UsernameNotFoundException("Failed to find ${username} username")
    }

    override fun isAvailable(): Boolean {
        //if(passwordsFilePath.isBlank()) return false
        return (usersNumberInternal() > 0)
    }

    private fun importUsersFromImporter() {
        usersImporter.reset()
        usersImporter.use { importer ->
            for((user, pass, roles) in importer) {
                log.debug("Adding to local map user: $user")
                usersPasswordsAndRolesMap[user] = Pair(pass, roles)
            }
        }
    } // importUsersFromImporter()
}
