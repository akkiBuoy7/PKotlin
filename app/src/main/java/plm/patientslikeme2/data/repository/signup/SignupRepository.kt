package plm.patientslikeme2.data.repository.signup

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson.ADD_CONDITIONS
import plm.patientslikeme2.data.di.LocalJson.CHECK_MAIL
import plm.patientslikeme2.data.di.LocalJson.CONDITIONS_LIST
import plm.patientslikeme2.data.di.LocalJson.CONDITIONS_STAGES
import plm.patientslikeme2.data.di.LocalJson.DELETE_CONDITIONS
import plm.patientslikeme2.data.di.LocalJson.SEARCH_CONDITIONS
import plm.patientslikeme2.data.di.LocalJson.SEARCH_ZIPCODE
import plm.patientslikeme2.data.di.LocalJson.UPDATE_CONDITIONS
import plm.patientslikeme2.data.di.LocalJson.UPDATE_USER_INFO
import plm.patientslikeme2.data.di.LocalJson.USER_AVATARS
import plm.patientslikeme2.data.di.LocalJson.USER_REGISTRATIONS
import plm.patientslikeme2.data.di.MyApplication.Companion.APP_MODE
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.signup.*
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignupRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun getCheckEmail(email: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(CHECK_MAIL)
                } else {
                    apiServices.getCheckEmail(email)
                }
            }
        }.asLiveData()
    }

    fun postUserRegistrations(request: SignupRequest): LiveData<Resource<SignupResponse>> {
        return object : NetworkResource<SignupResponse>() {
            override fun createCall(): LiveData<Resource<SignupResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(USER_REGISTRATIONS)
                } else {
                    apiServices.postUserRegistrations(request)
                }
            }
        }.asLiveData()
    }

    fun getConditionList(): LiveData<Resource<ConditionsResponse>> {
        return object : NetworkResource<ConditionsResponse>() {
            override fun createCall(): LiveData<Resource<ConditionsResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(CONDITIONS_LIST)
                } else {
                    apiServices.getConditionList()
                }
            }
        }.asLiveData()
    }

    fun getSearchCondition(query: String): LiveData<Resource<ConditionsResponse>> {
        return object : NetworkResource<ConditionsResponse>() {
            override fun createCall(): LiveData<Resource<ConditionsResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(SEARCH_CONDITIONS)
                } else {
                    apiServices.getSearchCondition(query)
                }
            }
        }.asLiveData()
    }

    fun getConditionStages(id: Int?): LiveData<Resource<ConditionsStageResponse>> {
        return object : NetworkResource<ConditionsStageResponse>() {
            override fun createCall(): LiveData<Resource<ConditionsStageResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(CONDITIONS_STAGES)
                } else {
                    apiServices.getConditionStages(id)
                }
            }
        }.asLiveData()
    }

    fun postAddCondition(request: AddConditionsRequest): LiveData<Resource<AddConditionsResponse>> {
        return object : NetworkResource<AddConditionsResponse>() {
            override fun createCall(): LiveData<Resource<AddConditionsResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(ADD_CONDITIONS)
                } else {
                    apiServices.postAddCondition(request)
                }
            }
        }.asLiveData()
    }

    fun putUpdateCondition(request: UpdateConditionsRequest): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(UPDATE_CONDITIONS)
                } else {
                    apiServices.putUpdateCondition(request)
                }
            }
        }.asLiveData()
    }

    fun deleteCondition(id: Int?): LiveData<Resource<DeleteConditionsResponse>> {
        return object : NetworkResource<DeleteConditionsResponse>() {
            override fun createCall(): LiveData<Resource<DeleteConditionsResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(DELETE_CONDITIONS)
                } else {
                    apiServices.deleteCondition(id)
                }
            }
        }.asLiveData()
    }

    fun getSearchZipCode(query: String): LiveData<Resource<ZipCodeResponse>> {
        return object : NetworkResource<ZipCodeResponse>() {
            override fun createCall(): LiveData<Resource<ZipCodeResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(SEARCH_ZIPCODE)
                } else {
                    apiServices.getSearchZipCode(query)
                }
            }
        }.asLiveData()
    }

    fun postUserAvatars(avatar: MultipartBody.Part): LiveData<Resource<AvatarResponse>> {
        return object : NetworkResource<AvatarResponse>() {
            override fun createCall(): LiveData<Resource<AvatarResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(USER_AVATARS)
                } else {
                    apiServices.postUserAvatars(avatar)
                }
            }
        }.asLiveData()
    }

    fun postUpdateUserInfo(request: UpdateUserInfo): LiveData<Resource<SignupResponse>> {
        return object : NetworkResource<SignupResponse>() {
            override fun createCall(): LiveData<Resource<SignupResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(UPDATE_USER_INFO)
                } else {
                    apiServices.postUpdateUserInfo(request)
                }
            }
        }.asLiveData()
    }

    fun removeUserAvatars(): LiveData<Resource<AvatarResponse>> {
        return object : NetworkResource<AvatarResponse>() {
            override fun createCall(): LiveData<Resource<AvatarResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(USER_AVATARS)
                } else {
                    apiServices.removeUserAvatars()
                }
            }
        }.asLiveData()
    }
}