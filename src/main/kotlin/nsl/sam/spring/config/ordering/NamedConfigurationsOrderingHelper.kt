package nsl.sam.spring.config.ordering

import nsl.sam.util.AbstractNamedSingleton

class NamedConfigurationsOrderingHelper(name: String): AbstractNamedSingleton<OrderingHelper>(name) {

    override fun supply(): OrderingHelper {
        return OrderingHelper()
    }
}
