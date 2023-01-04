package plm.patientslikeme2.data.repository.login

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson.FORGET_PASSWORD
import plm.patientslikeme2.data.di.LocalJson.LOGIN
import plm.patientslikeme2.data.di.MyApplication.Companion.APP_MODE
import plm.patientslikeme2.data.model.login.*
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun postLogin(request: LoginRequest): LiveData<Resource<LoginModel>> {
        return object : NetworkResource<LoginModel>() {
            override fun createCall(): LiveData<Resource<LoginModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LOGIN)
                } else {
                    apiServices.postLogin(request)
                }
            }
        }.asLiveData()
    }

    fun postForgetPassword(request: ForgetPassword): LiveData<Resource<ForgetPasswordResponse>> {
        return object : NetworkResource<ForgetPasswordResponse>() {
            override fun createCall(): LiveData<Resource<ForgetPasswordResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FORGET_PASSWORD)
                } else {
                    apiServices.postForgetPassword(request)
                }
            }
        }.asLiveData()
    }

    fun putResetPassword(request: ResetPasswordRequest): LiveData<Resource<ResetPasswordResponse>> {
        return object : NetworkResource<ResetPasswordResponse>() {
            override fun createCall(): LiveData<Resource<ResetPasswordResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FORGET_PASSWORD)
                } else {
                    apiServices.putForgetPassword(request)
                }
            }
        }.asLiveData()
    }
}