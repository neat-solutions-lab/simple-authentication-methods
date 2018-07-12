package nsl.sam.userdetails

import nsl.sam.logger.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

//@Component
class LocalFileUsersSource: UsersSource {

    companion object { val log by logger() }

    @Value("\${sms.passwords-file:}")
    lateinit var passwordsFile: String

    val usersPasswordsAndRolesMap: MutableMap<String, Pair<String, Array<String>>> = mutableMapOf()

    override fun getUserPasswordAndRoles(username: String): Pair<String, Array<String>> {
        return usersPasswordsAndRolesMap[username] ?: throw UsernameNotFoundException("Failed to find ${username} username")
    }

    @PostConstruct
    fun init() {
        if(passwordsFile.isNotBlank()) importUsersFromFile()
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