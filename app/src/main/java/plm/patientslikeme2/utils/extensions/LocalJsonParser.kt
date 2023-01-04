package plm.patientslikeme2.utils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.MyApplication

inline fun <reified T : Any> getLocalResponse(path: String): LiveData<Resource<T>> {
    val jsonFile = getJsonFile(MyApplication.appContext, path)
    return fromJson(jsonFile)
}

inline fun <reified T : Any> fromJson(string: String): MediatorLiveData<Resource<T>> {
    val result = MediatorLiveData<Resource<T>>()
    val data = Gson().fromJson(string, T::class.java)
    result.value = Resource.success(data, 200)
    return result
}