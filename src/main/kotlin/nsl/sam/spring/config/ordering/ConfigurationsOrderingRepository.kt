package nsl.sam.spring.config.ordering

class ConfigurationsOrderingRepository {
    companion object {
        fun get(name: String):OrderingHelper {
            return NamedConfigurationsOrderingHelper(name).getObj()
        }
    }
}