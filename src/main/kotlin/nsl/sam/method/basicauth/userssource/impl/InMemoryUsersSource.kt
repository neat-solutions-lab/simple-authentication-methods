package nsl.sam.method.basicauth.userssource.impl

import nsl.sam.changes.ChangeEvent
import nsl.sam.changes.ChangeListener
import nsl.sam.concurrent.ConditionalReadWriteSynchronizer
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.userssource.UsersSource
import nsl.sam.scheduler.ScheduledExecutor
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*

class InMemoryUsersSource private constructor(
        private val passwordsImporter: PasswordsCredentialsImporter,
        private val configProperties: Properties
) : UsersSource, ChangeListener<String> {

    companion object {
        private val log by logger()

        fun createInstance(
                passwordsImporter: PasswordsCredentialsImporter,
                configProperties: Properties
        ): InMemoryUsersSource {
            val instance = InMemoryUsersSource(passwordsImporter, configProperties)
            instance.initialize()
            return instance
        }
    }

    private val usersMap: MutableMap<String, UserTraits> = mutableMapOf()
    private lateinit var readWriteSynchronizer: ConditionalReadWriteSynchronizer

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
            this.readWriteSynchronizer = ConditionalReadWriteSynchronizer(true)
            val detector = passwordsImporter.getChangeDetector()
            detector?.addChangeListener(this)
            ScheduledExecutor.addTask(
                    detector!!,
                    getFileChangeDetectionPeriod())
        } else {
            this.readWriteSynchronizer = ConditionalReadWriteSynchronizer(false)
        }
        importUsersFromImporter()
    }

    override fun getUserTraits(username: String): UserTraits = readWriteSynchronizer.readLock {
        usersMap[username] ?: throw UsernameNotFoundException("Failed to find $username username")
    }

    private fun importUsersFromImporter() = readWriteSynchronizer.writeLock {
        usersMap.clear()
        passwordsImporter.reset()
        //var usersCounter = 0
        passwordsImporter.use { importer ->
            for (userTraits in importer) {
                log.debug("Adding user to in-memory users map: ${userTraits.name}")
                usersMap[userTraits.name] = userTraits
                //usersCounter++
            }
        }
        //log.info("Number of imported users: $usersCounter")
    }

    private fun getFileChangeDetectionPeriod(): Long {
        return configProperties.getProperty(
                "nsl.sam.passwords-file-change-detection-period", "1000"
        ).toLong()
    }
}
