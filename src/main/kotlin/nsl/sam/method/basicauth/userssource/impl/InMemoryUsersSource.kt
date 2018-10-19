package nsl.sam.method.basicauth.userssource.impl

import nsl.sam.changes.ChangeEvent
import nsl.sam.changes.ChangeListener
import nsl.sam.logger.logger
import nsl.sam.method.basicauth.domain.user.UserTraits
import nsl.sam.method.basicauth.usersimporter.PasswordsCredentialsImporter
import nsl.sam.method.basicauth.userssource.UsersSource
import nsl.sam.scheduler.ScheduledExecutor
import nsl.sam.utils.Conditionally
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock

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
    private val readWriteLock = ReentrantReadWriteLock()


    private lateinit var ifSynchronized: Conditionally

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
            this.ifSynchronized = Conditionally(true)
            val detector = passwordsImporter.getChangeDetector()
            detector?.addChangeListener(this)
            ScheduledExecutor.addTask(
                    detector!!,
                    getFileChangeDetectionPeriod())
        } else {
            this.ifSynchronized = Conditionally(false)
        }
        importUsersFromImporter()
    }

    override fun getUserTraits(username: String): UserTraits = try {

        ifSynchronized { readWriteLock.readLock().lock() }

        usersMap[username]
                ?: throw UsernameNotFoundException("Failed to find $username username")
    } finally {
        ifSynchronized { readWriteLock.readLock().unlock() }
    }

    private fun importUsersFromImporter() = try {
        ifSynchronized { readWriteLock.writeLock().lock() }
        usersMap.clear()
        passwordsImporter.reset()
        passwordsImporter.use { importer ->
            for (userTraits in importer) {
                log.debug("Adding user to in-memory users map: ${userTraits.name}")
                usersMap[userTraits.name] = userTraits
            }
        }
    } finally {
        ifSynchronized { readWriteLock.writeLock().unlock() }
    }

    private fun getFileChangeDetectionPeriod(): Long {
        return configProperties.getProperty(
                "sam.passwords-file-change-detection-period", "1000"
        ).toLong()
    }
}