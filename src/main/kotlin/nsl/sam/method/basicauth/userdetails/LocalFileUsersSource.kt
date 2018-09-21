package nsl.sam.method.basicauth.userdetails

import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UsernameNotFoundException
import javax.annotation.PostConstruct

class LocalFileUsersSource(@Autowired val passwordsFile: String): UsersSource {

    companion object { val log by logger() }

    val usersPasswordsAndRolesMap: MutableMap<String, Pair<String, Array<String>>> = mutableMapOf()

    init {
        if(passwordsFile.isNotBlank()) importUsersFromFile()
    }

    override fun getUserPasswordAndRoles(username: String): Pair<String, Array<String>> {
        return usersPasswordsAndRolesMap[username] ?: throw UsernameNotFoundException("Failed to find ${username} username")
    }

    private fun importUsersFromFile() {
        LocalFileUsersImporter(passwordsFile).use {importer ->
            for((user, pass, roles) in importer) {
                log.debug("Adding to local map user: ${user}")
                usersPasswordsAndRolesMap[user] = Pair(pass, roles)
            }
        }
    } // importUsersFromFile()
}
