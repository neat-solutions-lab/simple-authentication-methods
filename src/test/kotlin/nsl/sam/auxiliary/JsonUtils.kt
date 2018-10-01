package nsl.sam.auxiliary

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

object JsonUtils {

    private val prettyGson = GsonBuilder().setPrettyPrinting().create()
    private val jsonParser = JsonParser()

    fun toPretty(jsonAsString: String): String {
        val jsonObject = jsonParser.parse(jsonAsString)
        return prettyGson.toJson(jsonObject)
    }

}