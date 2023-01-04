package plm.patientslikeme2.data.repository.profile

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.global.MasterData
import plm.patientslikeme2.data.model.profile.*
import plm.patientslikeme2.data.model.signup.UserDetailsResponse
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun getMemberConditions(id: String): LiveData<Resource<MemberConditionsResponse>> {
        return object : NetworkResource<MemberConditionsResponse>() {
            override fun createCall(): LiveData<Resource<MemberConditionsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MEMBER_CONDITIONS)
                } else {
                    if (id.isEmpty())
                        apiServices.getMyConditionsList()
                    else
                        apiServices.getMemberConditions(id)
                }
            }
        }.asLiveData()
    }

    fun getMemberFollowings(id: String): LiveData<Resource<MemberFollowingsResponse>> {
        return object : NetworkResource<MemberFollowingsResponse>() {
            override fun createCall(): LiveData<Resource<MemberFollowingsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MEMBER_FOLLOWINGS)
                } else {
                    if (id.isEmpty())
                        apiServices.getMyFollowings()
                    else
                        apiServices.getMemberFollowings(id)
                }
            }
        }.asLiveData()
    }

    fun getMemberProfile(id: String): LiveData<Resource<MyProfileResponse>> {
        return object : NetworkResource<MyProfileResponse>() {
            override fun createCall(): LiveData<Resource<MyProfileResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MEMBER_ABOUT)
                } else {
                    if (id.isEmpty())
                        apiServices.getMyProfile()
                    else
                        apiServices.getMemberProfile(id)
                }
            }
        }.asLiveData()
    }

    fun memberFollow(id: String): LiveData<Resource<FollowResponse>> {
        return object : NetworkResource<FollowResponse>() {
            override fun createCall(): LiveData<Resource<FollowResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MEMBER_FOLLOW)
                } else {
                    apiServices.memberFollow(id)
                }
            }
        }.asLiveData()
    }

    fun memberUnFollow(id: String): LiveData<Resource<FollowResponse>> {
        return object : NetworkResource<FollowResponse>() {
            override fun createCall(): LiveData<Resource<FollowResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MEMBER_UNFOLLOW)
                } else {
                    apiServices.memberUnFollow(id)
                }
            }
        }.asLiveData()
    }

    fun getStaticMasterData(): LiveData<Resource<MasterData>> {
        return object : NetworkResource<MasterData>() {
            override fun createCall(): LiveData<Resource<MasterData>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.STATIC_MASTER_DATA)
                } else {
                    apiServices.getStaticMasterData()
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

    fun updatePersonalInfo(request: UpdatePersonalInfoRequest): LiveData<Resource<UserDetailsResponse>> {
        return object : NetworkResource<UserDetailsResponse>() {
            override fun createCall(): LiveData<Resource<UserDetailsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.UPDATE_PERSONAL_INFO)
                } else {
                    apiServices.updatePersonalInfo(request)
                }
            }
        }.asLiveData()
    }
}