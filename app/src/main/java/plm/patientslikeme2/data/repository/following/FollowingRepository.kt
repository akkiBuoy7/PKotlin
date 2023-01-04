package plm.patientslikeme2.data.repository.following

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson.FOLLOWING_DISCUSSIONS
import plm.patientslikeme2.data.di.LocalJson.FOLLOWING_MEMBERS
import plm.patientslikeme2.data.di.LocalJson.FOLLOWING_RESOURCES
import plm.patientslikeme2.data.di.LocalJson.FOLLOWING_TAGS
import plm.patientslikeme2.data.di.MyApplication.Companion.APP_MODE
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.following.*
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FollowingRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun getFollowingDiscussion(
        page: Int?,
        size: Int?
    ): LiveData<Resource<FollowingDiscussionResponse>> {
        return object : NetworkResource<FollowingDiscussionResponse>() {
            override fun createCall(): LiveData<Resource<FollowingDiscussionResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_DISCUSSIONS)
                } else {
                    apiServices.getFollowingDiscussion(page, size)
                }
            }
        }.asLiveData()
    }

    fun getFollowingMember(page: Int?, size: Int?): LiveData<Resource<FollowingMemberResponse>> {
        return object : NetworkResource<FollowingMemberResponse>() {
            override fun createCall(): LiveData<Resource<FollowingMemberResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_MEMBERS)
                } else {
                    apiServices.getFollowingMembers(page, size)
                }
            }
        }.asLiveData()
    }

    fun getFollowingTags(page: Int?, size: Int?): LiveData<Resource<FollowingTagResponse>> {
        return object : NetworkResource<FollowingTagResponse>() {
            override fun createCall(): LiveData<Resource<FollowingTagResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_TAGS)
                } else {
                    apiServices.getFollowingTags(page, size)
                }
            }
        }.asLiveData()
    }

    fun getFollowingResources(
        page: Int?,
        size: Int?
    ): LiveData<Resource<FollowingResourceResponse>> {
        return object : NetworkResource<FollowingResourceResponse>() {
            override fun createCall(): LiveData<Resource<FollowingResourceResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_RESOURCES)
                } else {
                    apiServices.getFollowingResources(page, size)
                }
            }
        }.asLiveData()
    }

    fun getTagDetailsList(
        tag: String?,
        page: Int?,
        size: Int?
    ): LiveData<Resource<TagDetailsResponse>> {
        return object : NetworkResource<TagDetailsResponse>() {
            override fun createCall(): LiveData<Resource<TagDetailsResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_RESOURCES)
                } else {
                    apiServices.getTagDetailsList(tag, page, size)
                }
            }
        }.asLiveData()
    }

    fun postFollowTagDetailsDiscussion(id: String?): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_RESOURCES)
                } else {
                    apiServices.getFollowTagDetailsDiscussion(id)
                }
            }
        }.asLiveData()
    }

    fun postUnFollowTag(id: String?): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_RESOURCES)
                } else {
                    apiServices.postUnFollowTag(id)
                }
            }
        }.asLiveData()
    }

    fun postFollowTag(id: String?): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_RESOURCES)
                } else {
                    apiServices.postFollowTag(id)
                }
            }
        }.asLiveData()
    }
    fun postUnFollowDiscussion(id: String?): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_RESOURCES)
                } else {
                    apiServices.postUnFollowDiscussion(id)
                }
            }
        }.asLiveData()
    }

    fun postUnFollowResource(id: String?): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_RESOURCES)
                } else {
                    apiServices.postUnFollowResource(id)
                }
            }
        }.asLiveData()
    }

    fun postFollowResource(id: String?): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(FOLLOWING_RESOURCES)
                } else {
                    apiServices.postFollowResource(id)
                }
            }
        }.asLiveData()
    }
}