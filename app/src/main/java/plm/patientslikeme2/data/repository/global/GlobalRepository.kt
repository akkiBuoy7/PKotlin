package plm.patientslikeme2.data.repository.global

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson.FEATURE_FLAG
import plm.patientslikeme2.data.di.LocalJson.GLOBAL
import plm.patientslikeme2.data.di.MyApplication.Companion.APP_MODE
import plm.patientslikeme2.data.model.global.FeatureFlagResponse
import plm.patientslikeme2.data.model.global.GlobalModel
import plm.patientslikeme2.data.model.login.LoginModel
import plm.patientslikeme2.data.model.login.RefreshTokenRequest
import plm.patientslikeme2.utils.Constants.REFRESH_TOKEN
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun getGlobalData(): LiveData<Resource<GlobalModel>> {
        return object : NetworkResource<GlobalModel>() {
            override fun createCall(): LiveData<Resource<GlobalModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(GLOBAL)
                } else {
                    apiServices.getGlobalState()
                }
            }
        }.asLiveData()
    }

    fun getFeatureFlag(): LiveData<Resource<FeatureFlagResponse>> {
        return object : NetworkResource<FeatureFlagResponse>() {
            override fun createCall(): LiveData<Resource<FeatureFlagResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FEATURE_FLAG)
                } else {
                    apiServices.getFeatureFlag()
                }
            }
        }.asLiveData()
    }

    fun refreshToken(): LiveData<Resource<LoginModel>> {
        return object : NetworkResource<LoginModel>() {
            override fun createCall(): LiveData<Resource<LoginModel>> {
                val request = RefreshTokenRequest()
                request.refresh_token = Preferences.getValue(REFRESH_TOKEN, "")
                return apiServices.postRefreshToken(request)
            }
        }.asLiveData()
    }
}