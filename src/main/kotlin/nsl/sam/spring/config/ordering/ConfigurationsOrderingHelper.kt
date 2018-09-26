package nsl.sam.spring.config.ordering

import nsl.sam.util.AbstractSharingBox

class ConfigurationsOrderingHelper(key: String): AbstractSharingBox<OrderingManager>(key) {

    override fun supply(): OrderingManager {
        return OrderingManager()
    }
}
