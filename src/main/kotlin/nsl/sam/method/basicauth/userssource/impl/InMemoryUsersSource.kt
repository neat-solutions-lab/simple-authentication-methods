package nsl.sam.method.basicauth.userssource.impl

import nsl.sam.changes.ChangeEvent
import nsl.sam.changes.ChangeListener
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.userssource.UsersSource
import nsl.sam.scheduler.ScheduledExecutor
import org.springframework.security.core.userdetails.UsernameNotFoundException

class InMemoryUsersSource private constructor(private val passwordsImporter: PasswordsCredentialsImporter) :
        UsersSource, ChangeListener<String> {

    companion object {
        private val log by logger()

        fun createInstance(passwordsImporter: PasswordsCredentialsImporter): InMemoryUsersSource {
            val instance = InMemoryUsersSource(passwordsImporter)
            instance.initialize()
            return instance
        }
    }

    private val usersMap: MutableMap<String, UserTraits> = mutableMapOf()

    override fun onChangeDetected(changeEvent: ChangeEvent<String>) {
        log.info("Underlying passwords file has changed. Reimporting users list.")
        importUsersFromImporter()
    }

    override fun hasItems(): Boolean {
        return passwordsImporter.hasItems()
    }

    /**
     * To be called by factory.
     * (below code passes `this` out of the object, so it should not be executed in constructor)
     */
    private fun initialize() {
        if (passwordsImporter.hasChangeDetector()) {
            val detector = passwordsImporter.getChangeDetector()
            detector?.addChangeListener(this)
            ScheduledExecutor.addTask(detector!!, 1000)
        }
        importUsersFromImporter()
    }

    @Synchronized
    override fun getUserTraits(username: String): UserTraits {
        return usersMap[username]
                ?: throw UsernameNotFoundException("Failed to find $username username")
    }

    @Synchronized
    private fun importUsersFromImporter() {
        usersMap.clear()
        passwordsImporter.reset()
        passwordsImporter.use { importer ->
            for (userTraits in importer) {
                log.debug("Adding user to in-memory users map: ${userTraits.name}")
                usersMap[userTraits.name] = userTraits
            }
        }
    }
}