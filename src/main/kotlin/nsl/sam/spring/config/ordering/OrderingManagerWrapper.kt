package nsl.sam.spring.config.ordering

import nsl.sam.util.GlobalObjectWrapper

class OrderingManagerWrapper(key: String): GlobalObjectWrapper<OrderingManager>(key) {
    override fun supply(): OrderingManager {
        return OrderingManager()
    }
}
