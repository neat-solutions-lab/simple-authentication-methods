package nsl.sam.envvar

interface EnvironmentVariablesAccessor {
    fun getVarsMap(): Map<String, String>
}