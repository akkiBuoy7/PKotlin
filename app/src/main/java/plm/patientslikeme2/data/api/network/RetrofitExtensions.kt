package plm.patientslikeme2.data.api.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import plm.patientslikeme2.data.model.action.ErrorResponse
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Synthetic sugaring to create Retrofit Service.
 */
inline fun <reified T> Retrofit.create(): T = create(T::class.java)

/**
 * Converts Retrofit [Response] to [Resource] which provides state
 * and data to the UI.
 */
fun <ResultType> Response<ResultType>.toResource(): Resource<ResultType> {
    var error = errorBody()?.toString() ?: message()
    return when {
        isSuccessful -> {
            val body = body()
            when {
                body != null -> Resource.success(body, this.code())
                else -> {
                    Resource.error(error, this.code())
                }
            }
        }
        else -> {
            if (code() == 422) {
                val type = object : TypeToken<ErrorResponse>() {}.type
                val errorResponse: ErrorResponse? = Gson().fromJson(errorBody()?.charStream(), type)
                if (errorResponse?.data != null) {
                    error = Gson().toJson(errorResponse)
                }
            }
            Resource.error(error, this.code())
        }
    }
}