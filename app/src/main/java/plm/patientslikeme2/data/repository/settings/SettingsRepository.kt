package plm.patientslikeme2.data.repository.settings

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.settings.*
import plm.patientslikeme2.data.model.signup.UserDetailsResponse
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun updateEmail(request: EmailUpdateRequest): LiveData<Resource<EmailUpdateResponse>> {
        return object : NetworkResource<EmailUpdateResponse>() {
            override fun createCall(): LiveData<Resource<EmailUpdateResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.UPDATE_EMAIL)
                } else {
                    return apiServices.updateEmail(request)
                }
            }
        }.asLiveData()
    }

    fun verifyEmail(): LiveData<Resource<EmailVerifyResponse>> {
        return object : NetworkResource<EmailVerifyResponse>() {
            override fun createCall(): LiveData<Resource<EmailVerifyResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.VERIFY_EMAIL)
                } else {
                    return apiServices.verifyEmail()
                }
            }
        }.asLiveData()
    }

    fun updatePassword(request: PasswordUpdateRequest): LiveData<Resource<PasswordUpdateResponse>> {
        return object : NetworkResource<PasswordUpdateResponse>() {
            override fun createCall(): LiveData<Resource<PasswordUpdateResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.UPDATE_PASSWORD)
                } else {
                    return apiServices.updatePassword(request)
                }
            }
        }.asLiveData()
    }

    fun getUserPreferences(): LiveData<Resource<PrivacySettingsResponse>> {
        return object : NetworkResource<PrivacySettingsResponse>() {
            override fun createCall(): LiveData<Resource<PrivacySettingsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.USER_REFERENCES)
                } else {
                    return apiServices.getUserPreferences()
                }
            }
        }.asLiveData()
    }

    fun updateUserPreferences(request: PrivacySettingsRequest): LiveData<Resource<PrivacySettingsResponse>> {
        return object : NetworkResource<PrivacySettingsResponse>() {
            override fun createCall(): LiveData<Resource<PrivacySettingsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.UPDATE_USER_PREFERENCES)
                } else {
                    return apiServices.updateUserPreferences(request)
                }
            }
        }.asLiveData()
    }

    fun getUserDetails(): LiveData<Resource<UserDetailsResponse>> {
        return object : NetworkResource<UserDetailsResponse>() {
            override fun createCall(): LiveData<Resource<UserDetailsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.USER_DETAILS)
                } else {
                    apiServices.getUserDetails()
                }
            }
        }.asLiveData()
    }

    fun getBlockedPrivateMessages(page: Int?, size: Int?): LiveData<Resource<BlockerUserResponse>> {
        return object : NetworkResource<BlockerUserResponse>() {
            override fun createCall(): LiveData<Resource<BlockerUserResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.BLOCKED_PRIVATE_MESSAGES_LIST)
                } else {
                    apiServices.getBlockedPrivateMessages(page, size)
                }
            }
        }.asLiveData()
    }

    fun postUnBlockedPrivateMessages(request: BlockUnBlockUserRequest): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.UNBLOCKED_PRIVATE_MESSAGES)
                } else {
                    apiServices.postUnBlockedPrivateMessages(request)
                }
            }
        }.asLiveData()
    }

    fun postBlockedPrivateMessages(request: BlockUnBlockUserRequest): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.BLOCKED_PRIVATE_MESSAGES)
                } else {
                    apiServices.postBlockedPrivateMessages(request)
                }
            }
        }.asLiveData()
    }

    fun getHiddenPosts(page: Int?, size: Int?): LiveData<Resource<HiddenPostResponse>> {
        return object : NetworkResource<HiddenPostResponse>() {
            override fun createCall(): LiveData<Resource<HiddenPostResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.HIDDEN_POSTS_LIST)
                } else {
                    apiServices.getHiddenPosts(page, size)
                }
            }
        }.asLiveData()
    }

    fun postUnHiddenPosts(request: HiddenUnHiddenPostRequest): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.UNHIDDEN_POSTS)
                } else {
                    apiServices.postUnHiddenPosts(request)
                }
            }
        }.asLiveData()
    }

    fun postHiddenPosts(request: HiddenUnHiddenPostRequest): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.HIDDEN_POSTS)
                } else {
                    apiServices.postHiddenPosts(request)
                }
            }
        }.asLiveData()
    }

    fun getPushNotificationSettings(): LiveData<Resource<PushNotificationSettingsResponse>> {
        return object : NetworkResource<PushNotificationSettingsResponse>() {
            override fun createCall(): LiveData<Resource<PushNotificationSettingsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.NOTIFICATION_SETTINGS)
                } else {
                    apiServices.getPushNotificationSettings()
                }
            }
        }.asLiveData()
    }

    fun putPushNotificationSettings(key: String, status: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.UPDATE_NOTIFICATION_SETTINGS)
                } else {
                    apiServices.putPushNotificationSettings(key, status)
                }
            }
        }.asLiveData()
    }
}