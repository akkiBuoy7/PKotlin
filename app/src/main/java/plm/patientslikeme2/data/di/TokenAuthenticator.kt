package plm.patientslikeme2.data.di

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import javax.inject.Inject

class TokenAuthenticator @Inject constructor() : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        return runBlocking {
            val token = Preferences.getValue(Constants.ACCESS_TOKEN, "")
            response.request.newBuilder()
                .header(Constants.APP_TYPE, Constants.ANDROID)
                .header(Constants.AUTHORIZATION, "Bearer $token")
                .build()
        }
    }
}