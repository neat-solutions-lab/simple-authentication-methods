package nsl.sam.method.basicauth.userssource.impl

import nsl.sam.logger.logger
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.userssource.UsersSource
import org.springframework.security.core.userdetails.UsernameNotFoundException

class InMemoryUsersSource(private val passwordsImporter: PasswordsCredentialsImporter): UsersSource {

    companion object {
        private val log by logger()
    }

    override fun hasItems(): Boolean {
        return passwordsImporter.hasItems()
    }

    private val usersMap: MutableMap<String, UserTraits> = mutableMapOf()

    init {
        importUsersFromImporter()
    }

    override fun getUserTraits(username: String): UserTraits {
        return usersMap[username]
                ?: throw UsernameNotFoundException("Failed to find $username username")
    }

    private fun importUsersFromImporter() {
        passwordsImporter.reset()
        passwordsImporter.use { importer ->
            for ((name, pass, roles) in importer) {
                log.debug("Adding user to in-memory users map: $name")
                usersMap[name] = UserTraits(name, pass, roles)
            }
        }
    }
}