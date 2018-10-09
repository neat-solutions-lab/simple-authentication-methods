package nsl.sam.envvar

import java.util.function.Supplier
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class SteeredEnvironmentVariablesAccessor : EnvironmentVariablesAccessor {

    companion object {
        const val SUPPLIER_PROPERTY_NAME = "nsl.sam.env-vars-supplier"
    }

    override fun getVarsMap(): Map<String, String> {
        val supplierFqName = System.getProperty(SUPPLIER_PROPERTY_NAME)
        return if (null == supplierFqName) {
            getVarsMapFromSystem()
        } else {
            getVarsMapFromDynamicSupplier(supplierFqName)
        }
    }

    private fun getVarsMapFromSystem(): Map<String, String> {
        return System.getenv()
    }

    private fun getVarsMapFromDynamicSupplier(supplierFqName: String): Map<String, String> {

        @Suppress("UNCHECKED_CAST")
        val supplierClass: KClass<Supplier<Map<String, String>>> =
                Class.forName(supplierFqName).kotlin as KClass<Supplier<Map<String, String>>>

        val supplier = supplierClass.createInstance()

        return supplier.get()
    }
}