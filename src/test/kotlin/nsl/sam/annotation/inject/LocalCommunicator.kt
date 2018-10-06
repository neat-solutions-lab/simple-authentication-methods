package nsl.sam.annotation.inject

import java.time.Instant

class LocalCommunicator: ImaginaryCommunicator {
    override fun send(msg: String): Instant {
        println("Message accepted by ${this::class.qualifiedName}")
        println("Message body: $msg")
        return Instant.now()
    }
}