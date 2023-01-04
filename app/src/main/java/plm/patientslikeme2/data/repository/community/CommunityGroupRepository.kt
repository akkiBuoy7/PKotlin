package plm.patientslikeme2.data.repository.community

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson
import plm.patientslikeme2.data.di.LocalJson.ALL_GROUPS
import plm.patientslikeme2.data.di.LocalJson.GROUP_SEARCH
import plm.patientslikeme2.data.di.LocalJson.JOIN_GROUP
import plm.patientslikeme2.data.di.LocalJson.MY_GROUPS
import plm.patientslikeme2.data.di.LocalJson.SUGGESTED_GROUPS
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.community.allgroups.AllGroupsResponse
import plm.patientslikeme2.data.model.community.allgroups.querygroup.QueryGroupResponse
import plm.patientslikeme2.data.model.community.groups.about.GroupAboutModel
import plm.patientslikeme2.data.model.community.groups.groupOverview.GroupOverviewResponse
import plm.patientslikeme2.data.model.community.groups.members.MemberResponse
import plm.patientslikeme2.data.model.community.groups.mygroups.MyGroupsResponse
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.SuggestedGroupResponse
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.joingroup.JoinGroupResponse
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityGroupRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {
    fun getMyGroupList(): LiveData<Resource<MyGroupsResponse>> {
        return object : NetworkResource<MyGroupsResponse>() {
            override fun createCall(): LiveData<Resource<MyGroupsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(MY_GROUPS)
                } else {
                    apiServices.getMyGroupsList()
                }
            }
        }.asLiveData()
    }

    fun getSuggestedGroupList(): LiveData<Resource<SuggestedGroupResponse>> {
        return object : NetworkResource<SuggestedGroupResponse>() {
            override fun createCall(): LiveData<Resource<SuggestedGroupResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(SUGGESTED_GROUPS)
                } else {
                    apiServices.getSuggestedGroupsList()
                }
            }
        }.asLiveData()
    }

    fun getAllGroupList(): LiveData<Resource<AllGroupsResponse>> {
        return object : NetworkResource<AllGroupsResponse>() {
            override fun createCall(): LiveData<Resource<AllGroupsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(ALL_GROUPS)
                } else {
                    apiServices.getAllGroupsList()
                }
            }
        }.asLiveData()
    }

    fun queryGroup(search: String): LiveData<Resource<QueryGroupResponse>> {
        return object : NetworkResource<QueryGroupResponse>() {
            override fun createCall(): LiveData<Resource<QueryGroupResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(GROUP_SEARCH)
                } else {
                    apiServices.queryGroup(search)
                }
            }
        }.asLiveData()
    }

    fun joinGroup(id: Int): LiveData<Resource<JoinGroupResponse>> {
        return object : NetworkResource<JoinGroupResponse>() {
            override fun createCall(): LiveData<Resource<JoinGroupResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(JOIN_GROUP)
                } else {
                    apiServices.joinGroup(id)
                }
            }
        }.asLiveData()
    }

    fun leaveGroup(id: Int): LiveData<Resource<JoinGroupResponse>> {
        return object : NetworkResource<JoinGroupResponse>() {
            override fun createCall(): LiveData<Resource<JoinGroupResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(JOIN_GROUP)
                } else {
                    apiServices.leaveGroup(id)
                }
            }
        }.asLiveData()
    }

    fun getGroupOverview(groupID: String): LiveData<Resource<GroupOverviewResponse>> {
        return object : NetworkResource<GroupOverviewResponse>() {
            override fun createCall(): LiveData<Resource<GroupOverviewResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.GROUP_OVERVIEW)
                } else {
                    apiServices.getGroupOverview(groupID)
                }
            }
        }.asLiveData()
    }

    fun getAboutGroup(groupID: String): LiveData<Resource<GroupAboutModel>> {
        return object : NetworkResource<GroupAboutModel>() {
            override fun createCall(): LiveData<Resource<GroupAboutModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.GROUP_ABOUT)
                } else {
                    apiServices.getAboutGroup(groupID)
                }
            }
        }.asLiveData()
    }

    fun getNewMembers(groupID: String): LiveData<Resource<MemberResponse>> {
        return object : NetworkResource<MemberResponse>() {
            override fun createCall(): LiveData<Resource<MemberResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.ALL_MEMBERS)
                } else {
                    apiServices.getGroupNewMembers(groupID)
                }
            }
        }.asLiveData()
    }

    fun getMembers(groupID: String, sort_by: String = "newest", myFollowings: Boolean = true, myConditions: Boolean = true, page: String): LiveData<Resource<MemberResponse>> {
        return object : NetworkResource<MemberResponse>() {
            override fun createCall(): LiveData<Resource<MemberResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.ALL_MEMBERS)
                } else {
                    apiServices.getGroupMembers(groupID, sort_by, myFollowings, myConditions, page)
                }
            }
        }.asLiveData()
    }

    fun searchUsers(groupID: String, query: String): LiveData<Resource<MemberResponse>> {
        return object : NetworkResource<MemberResponse>() {
            override fun createCall(): LiveData<Resource<MemberResponse>> {
                return apiServices.searchMembers(groupID, query)
            }
        }.asLiveData()
    }

}