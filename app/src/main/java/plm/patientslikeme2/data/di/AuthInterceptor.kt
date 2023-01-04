package plm.patientslikeme2.data.di

import okhttp3.Interceptor
import okhttp3.Response
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.ANDROID
import plm.patientslikeme2.utils.Constants.APP_TYPE
import plm.patientslikeme2.utils.Constants.AUTHORIZATION
import plm.patientslikeme2.utils.Preferences
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = Preferences.getValue(Constants.ACCESS_TOKEN, "")
        request.addHeader(APP_TYPE, ANDROID)
        request.addHeader(AUTHORIZATION, "Bearer $token")
        return chain.proceed(request.build())
    }
}