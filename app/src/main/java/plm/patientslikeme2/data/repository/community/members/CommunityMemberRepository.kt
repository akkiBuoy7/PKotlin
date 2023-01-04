package plm.patientslikeme2.data.repository.community.members

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
import plm.patientslikeme2.data.model.community.members.Bucket
import plm.patientslikeme2.data.model.community.members.ExcludedUserRequest
import plm.patientslikeme2.data.model.community.members.MemberResponse
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityMemberRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun getAllMembers(sort_by: String = "newest", myFollowings: Boolean = true, myConditions: Boolean = true, page: String): LiveData<Resource<MemberResponse>> {
        return object : NetworkResource<MemberResponse>() {
            override fun createCall(): LiveData<Resource<MemberResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.ALL_MEMBERS)
                } else {
                    apiServices.getMemberList(sort_by, myFollowings, myConditions, page)
                }
            }
        }.asLiveData()
    }

    fun getMembersLikeMe(): LiveData<Resource<MemberResponse>> {
        return object : NetworkResource<MemberResponse>() {
            override fun createCall(): LiveData<Resource<MemberResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MEMBERS_LIKE_ME)
                } else {
                    apiServices.getMembersLikeMe()
                }
            }
        }.asLiveData()
    }


    fun followMember(id: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return apiServices.followMember(id)
            }
        }.asLiveData()
    }

    fun unFollowMember(id: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return apiServices.unFollowMember(id)
            }
        }.asLiveData()
    }

    fun excludedUser(id: Int): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                val request = ExcludedUserRequest().apply {
                    bucket = Bucket(ArrayList<Int>().apply {
                        add(id)
                    })
                }

                return apiServices.excludedUser(request)
            }
        }.asLiveData()
    }

    fun searchUsers(query: String): LiveData<Resource<MemberResponse>> {
        return object : NetworkResource<MemberResponse>() {
            override fun createCall(): LiveData<Resource<MemberResponse>> {
                return apiServices.searchUsers(query)
            }
        }.asLiveData()
    }
}