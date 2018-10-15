package nsl.sam.method.basicauth.userssource.interim.impl

import nsl.sam.logger.logger
import nsl.sam.method.basicauth.usersimporter.interim.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.userssource.UsersSource
import org.springframework.security.core.userdetails.UsernameNotFoundException

class InMemoryUsersSource(private val passwordsImporter: PasswordsCredentialsImporter): UsersSource {

    override fun hasItems(): Boolean {
        return passwordsImporter.hasItems()
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
                ?: throw UsernameNotFoundException("Failed to find $username username")
    }

    private fun importUsersFromImporter() {
        passwordsImporter.reset()
        passwordsImporter.use { importer ->
            for ((user, pass, roles) in importer) {
                log.debug("Adding user to in-memory users map: $user")
                usersPasswordsAndRolesMap[user] = Pair(pass, roles)
            }
        }
    }

}