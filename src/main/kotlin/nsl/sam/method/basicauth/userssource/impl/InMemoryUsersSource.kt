package nsl.sam.method.basicauth.userssource.impl

import nsl.sam.logger.logger
import nsl.sam.method.basicauth.usersimporter.UsersImporter
import nsl.sam.method.basicauth.userssource.UsersSource
import org.springframework.security.core.userdetails.UsernameNotFoundException

class InMemoryUsersSource(private val usersImporter: UsersImporter) : UsersSource {

    override fun hasItems(): Boolean {
        return usersImporter.hasItems()
    }

    companion object {
        private val log by logger()
    }

    private val usersPasswordsAndRolesMap: MutableMap<String, Pair<String, Array<String>>> = mutableMapOf()

    init {
        importUsersFromImporter()
    }

    override fun getUserPasswordAndRoles(username: String): Pair<String, Array<String>> {
        return usersPasswordsAndRolesMap[username]
                ?: throw UsernameNotFoundException("Failed to find ${username} username")
    }

    private fun importUsersFromImporter() {
        usersImporter.reset()
        usersImporter.use { importer ->
            for ((user, pass, roles) in importer) {
                log.debug("Adding to local map user: $user")
                usersPasswordsAndRolesMap[user] = Pair(pass, roles)
            }
        }
    }
}
