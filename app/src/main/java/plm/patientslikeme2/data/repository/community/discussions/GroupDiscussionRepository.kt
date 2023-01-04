package plm.patientslikeme2.data.repository.community.discussions

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import org.jsoup.Jsoup
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.model.ImageUpload
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.community.groups.discussions.*
import plm.patientslikeme2.data.model.settings.BlockUnBlockUserRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupDiscussionRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {
    fun groupDiscussions(groupID: String, sort_by: String, myFollowings: Boolean = true, tagFollowing: Boolean = true, page: String): LiveData<Resource<GroupDiscussions>> {
        return object : NetworkResource<GroupDiscussions>() {
            override fun createCall(): LiveData<Resource<GroupDiscussions>> {
                return apiServices.groupDiscussions(groupID, sort_by, myFollowings, tagFollowing, page)
            }
        }.asLiveData()
    }

    fun getCommentsOnDiscussion(discussionsID: String, currentPage: String, sort_by: String): LiveData<Resource<GroupDiscussions>> {
        return object : NetworkResource<GroupDiscussions>() {
            override fun createCall(): LiveData<Resource<GroupDiscussions>> {
                return apiServices.getCommentsOnDiscussion(discussionsID, sort_by, currentPage)
            }
        }.asLiveData()
    }

    fun searchGroupDiscussions(groupID: String, query: String): LiveData<Resource<GroupDiscussions>> {
        return object : NetworkResource<GroupDiscussions>() {
            override fun createCall(): LiveData<Resource<GroupDiscussions>> {
                return apiServices.searchGroupDiscussions(groupID, query)
            }
        }.asLiveData()
    }

    fun getRecommendedTags(groupID: String): LiveData<Resource<RecommendedTagsResponse>> {
        return object : NetworkResource<RecommendedTagsResponse>() {
            override fun createCall(): LiveData<Resource<RecommendedTagsResponse>> {
                return apiServices.getRecommendedTags(groupID)
            }
        }.asLiveData()
    }

    fun searchTags(query: String): LiveData<Resource<RecommendedTagsResponse>> {
        return object : NetworkResource<RecommendedTagsResponse>() {
            override fun createCall(): LiveData<Resource<RecommendedTagsResponse>> {
                return apiServices.searchTags(query)
            }
        }.asLiveData()
    }

    fun createDiscussion(groupID: String, model: NewDiscussionRequest): LiveData<Resource<CreateDiscussionResponse>> {
        return object : NetworkResource<CreateDiscussionResponse>() {
            override fun createCall(): LiveData<Resource<CreateDiscussionResponse>> {
                return apiServices.createDiscussion(groupID, model)
            }
        }.asLiveData()
    }

    fun discussionLikeUnlike(id: Int, markedHelpful: Boolean): LiveData<Resource<CommonModel>> {
        return if (markedHelpful) {
            object : NetworkResource<CommonModel>() {
                override fun createCall(): LiveData<Resource<CommonModel>> {
                    return apiServices.addThumbsUpDiscussion(id.toString())
                }
            }.asLiveData()
        } else {
            object : NetworkResource<CommonModel>() {
                override fun createCall(): LiveData<Resource<CommonModel>> {
                    return apiServices.removeThumbsUpDiscussion(id.toString())
                }
            }.asLiveData()
        }
    }

    fun uploadImage(file: MultipartBody.Part): LiveData<Resource<ImageUpload>> {
        return object : NetworkResource<ImageUpload>() {
            override fun createCall(): LiveData<Resource<ImageUpload>> {
                return apiServices.uploadImage(file)
            }
        }.asLiveData()
    }

    fun getDiscussion(discussionsID: String): LiveData<Resource<GroupDiscussions>> {
        return object : NetworkResource<GroupDiscussions>() {
            override fun createCall(): LiveData<Resource<GroupDiscussions>> {
                return apiServices.getDiscussion(discussionsID)
            }
        }.asLiveData()
    }

    fun addCommentsOnDiscussion(discussionsID: String, htmlBody: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                val request = AddComment().apply {
                    comment = Comment().apply {
                        body = htmlBody
                        markdown = Jsoup.parse(htmlBody).text()
                    }
                }
                return apiServices.addCommentsOnDiscussion(discussionsID, request)
            }
        }.asLiveData()
    }

    fun addReplyOnComments(discussionsID: String, commentID: String, htmlBody: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                val request = AddComment().apply {
                    comment = Comment().apply {
                        body = htmlBody
                        markdown = Jsoup.parse(htmlBody).text()
                    }
                }
                return apiServices.addReplyOnComments(discussionsID, commentID, request)
            }
        }.asLiveData()
    }

    fun getReplyOnComments(discussionsID: String, commentID: String, page: String): LiveData<Resource<GroupDiscussions>> {
        return object : NetworkResource<GroupDiscussions>() {
            override fun createCall(): LiveData<Resource<GroupDiscussions>> {
                return apiServices.getReplyOnComments(discussionsID, commentID, page)
            }
        }.asLiveData()
    }

    fun getCommentDetails(discussionsID: String, commentID: String): LiveData<Resource<GroupDiscussions>> {
        return object : NetworkResource<GroupDiscussions>() {
            override fun createCall(): LiveData<Resource<GroupDiscussions>> {
                return apiServices.getCommentDetails(discussionsID, commentID)
            }
        }.asLiveData()
    }

    fun replyLikeUnlike(discussionsID: String, commentID: String, id: Int, markedHelpful: Boolean): LiveData<Resource<CommonModel>> {
        return if (markedHelpful) {
            object : NetworkResource<CommonModel>() {
                override fun createCall(): LiveData<Resource<CommonModel>> {
                    return apiServices.addThumbsUpReply(discussionsID, commentID, id.toString())
                }
            }.asLiveData()
        } else {
            object : NetworkResource<CommonModel>() {
                override fun createCall(): LiveData<Resource<CommonModel>> {
                    return apiServices.removeThumbsUpReply(discussionsID, commentID, id.toString())
                }
            }.asLiveData()
        }
    }

    fun commentLikeUnlike(discussionsID: String, commentID: String, markedHelpful: Boolean): LiveData<Resource<CommonModel>> {
        return if (markedHelpful) {
            object : NetworkResource<CommonModel>() {
                override fun createCall(): LiveData<Resource<CommonModel>> {
                    return apiServices.addThumbsUpComment(discussionsID, commentID)
                }
            }.asLiveData()
        } else {
            object : NetworkResource<CommonModel>() {
                override fun createCall(): LiveData<Resource<CommonModel>> {
                    return apiServices.removeThumbsUpComment(discussionsID, commentID)
                }
            }.asLiveData()
        }
    }

    fun blockUser(userId: Int): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return apiServices.postBlockedPrivateMessages(BlockUnBlockUserRequest(userId))
            }
        }.asLiveData()
    }

    fun flagPost(id: Int): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                val request = FlagPost().apply {
                    bucket = plm.patientslikeme2.data.model.community.groups.discussions.Bucket(ArrayList<Int>().apply {
                        add(id)
                    })
                }
                return apiServices.flagPost(request)
            }
        }.asLiveData()
    }

    fun flagCommentReply(id: Int): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                val request = FlagCommentReply().apply {
                    bucket = BucketFlagCommentReply(ArrayList<Int>().apply {
                        add(id)
                    })
                }
                return apiServices.flagCommentReply(request)
            }
        }.asLiveData()
    }

    fun followDiscussions(id: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return apiServices.followDiscussions(id)
            }
        }.asLiveData()
    }

    fun unfollowDiscussions(id: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return apiServices.unfollowDiscussions(id)
            }
        }.asLiveData()
    }
}