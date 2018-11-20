package nsl.sam.method.token.tokensresolver.impl

import nsl.sam.changes.ChangeEvent
import nsl.sam.changes.ChangeListener
import nsl.sam.concurrent.ConditionalReadWriteSynchronizer
import nsl.sam.logger.logger
import nsl.sam.method.token.domain.token.ResolvedToken
import nsl.sam.method.token.tokensimporter.TokensCredentialsImporter
import nsl.sam.method.token.tokensresolver.TokensResolver
import nsl.sam.scheduler.ScheduledExecutor
import nsl.sam.utils.prune
import org.springframework.security.authentication.BadCredentialsException
import java.util.*

class InMemoryTokensResolver private constructor(
        private val tokensImporter: TokensCredentialsImporter,
        private val configProperties: Properties
): TokensResolver, ChangeListener<String> {

    companion object {

        val log by logger()

        fun createInstance(
                tokensImporter: TokensCredentialsImporter,
                configProperties: Properties
        ): InMemoryTokensResolver {
            val instance = InMemoryTokensResolver(tokensImporter, configProperties)
            instance.initialize()
            return instance
        }
    }

    override fun hasItems(): Boolean {
        return tokensImporter.hasItems()
    }


    private val tokensMap: MutableMap<String, ResolvedToken> = mutableMapOf()

    private lateinit var readWriteSynchronizer: ConditionalReadWriteSynchronizer
    private fun initialize() {
        if(tokensImporter.hasChangeDetector()) {
            this.readWriteSynchronizer = ConditionalReadWriteSynchronizer(true)
            val detector = tokensImporter.getChangeDetector()
            detector?.addChangeListener(this)
            ScheduledExecutor.addTask(
                    detector!!,
                    getFileChangeDetectionPeriod()
            )
        } else {
            this.readWriteSynchronizer = ConditionalReadWriteSynchronizer(false)
        }
        importTokensFromImporter()
    }

    override fun onChangeDetected(changeEvent: ChangeEvent<String>) {
        log.info("Underlying tokens file has changed. Reimporting tokens list.")
        importTokensFromImporter()
    }

    override fun getResolvedToken(tokenAsString: String): ResolvedToken = readWriteSynchronizer.readLock {
        tokensMap[tokenAsString] ?: throw BadCredentialsException("Filed to find token ${tokenAsString.prune()}")
    }

    private fun importTokensFromImporter() = readWriteSynchronizer.writeLock {
        tokensImporter.reset()
        var tokensCounter = 0
        tokensImporter.use { tokensImporter ->
            for (token in tokensImporter) {
                log.debug("Adding token to in-memory tokens map: $token")
                tokensMap[token.tokenValue] = token
                tokensCounter++
            }
        }
        log.info("Number of imported tokens: $tokensCounter")
    }

    private fun getFileChangeDetectionPeriod(): Long {
        return configProperties.getProperty("nsl.sam.tokens-file-change-detection-period", "1000").toLong()
    }
}