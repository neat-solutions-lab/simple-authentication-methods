package nsl.sam.changes

import java.time.Instant

class ChangeEvent<T>(
        val changeTimestamp: Instant,
        val resource: T
)