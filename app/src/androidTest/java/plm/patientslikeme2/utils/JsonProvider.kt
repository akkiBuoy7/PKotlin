package plm.patientslikeme2.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonProvider {

    inline fun <reified U> objectFromJsonFileWithType(filePath: String): U =
        Gson().fromJson(fileAsString(filePath), object : TypeToken<U>() {}.type)

    fun fileAsString(filePath: String): String {
        return InstrumentationRegistry.getInstrumentation().targetContext.assets.open(filePath).bufferedReader()
            .use { it.readText() }
    }
}