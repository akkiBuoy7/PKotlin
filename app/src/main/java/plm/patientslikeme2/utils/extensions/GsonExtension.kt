package plm.patientslikeme2.utils.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> stringToObject(value: String?): T {
    val dataType = object : TypeToken<T>() {}.type
    return Gson().fromJson(value, dataType)
}

inline fun <reified T> objectToString(any: T): String {
    return Gson().toJson(any, T::class.java)
}