package plm.patientslikeme2.data.repository.messages

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.ImageUpload
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.community.members.MemberResponse
import plm.patientslikeme2.data.model.messages.*
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagingRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun getViewAllMessages(query: String, messagesPage: Int): LiveData<Resource<MessagesResponse>> {
        return object : NetworkResource<MessagesResponse>() {
            override fun createCall(): LiveData<Resource<MessagesResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.VIEW_ALL_MESSAGES)
                } else {
                    if (query == "") {
                        apiServices.getViewAllMessages(messagesPage)
                    } else {
                        apiServices.getSearchMessages(query)
                    }
                }
            }
        }.asLiveData()
    }

    fun getMessageThreadConversation(
        otherParticipantId: String,
        messageConversationPage: Int,
    ): LiveData<Resource<MessagesThreadResponse>> {
        return object : NetworkResource<MessagesThreadResponse>() {
            override fun createCall(): LiveData<Resource<MessagesThreadResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.VIEW_CONVERSATION)
                } else {
                    apiServices.getMessageThreadConversation(otherParticipantId,
                        messageConversationPage)
                }
            }
        }.asLiveData()
    }

    fun putBlockMember(otherParticipantId: String): LiveData<Resource<MessagesResponse>> {
        return object : NetworkResource<MessagesResponse>() {
            override fun createCall(): LiveData<Resource<MessagesResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.BLOCK_MEMBER)
                } else {
                    apiServices.putBlockMember(otherParticipantId)
                }
            }
        }.asLiveData()
    }

    fun putReportAsSpam(otherParticipantId: String): LiveData<Resource<MessagesResponse>> {
        return object : NetworkResource<MessagesResponse>() {
            override fun createCall(): LiveData<Resource<MessagesResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.REPORT_AS_MEMBER)
                } else {
                    apiServices.putReportAsSpam(otherParticipantId)
                }
            }
        }.asLiveData()
    }

    fun getSearchMembers(query: String): LiveData<Resource<MemberResponse>> {
        return object : NetworkResource<MemberResponse>() {
            override fun createCall(): LiveData<Resource<MemberResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.SEARCH_MEMBERS)
                } else {
                    apiServices.getSearchMembers(query)
                }
            }
        }.asLiveData()
    }

    fun sendNewMessage(request: NewMessageRequest): LiveData<Resource<NewMessageResponse>> {
        return object : NetworkResource<NewMessageResponse>() {
            override fun createCall(): LiveData<Resource<NewMessageResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.SEND_NEW_MESSAGE)
                } else {
                    apiServices.sendNewMessage(request)
                }
            }
        }.asLiveData()
    }

    fun replyMessage(request: ReplyMessageRequest): LiveData<Resource<NewMessageResponse>> {
        return object : NetworkResource<NewMessageResponse>() {
            override fun createCall(): LiveData<Resource<NewMessageResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.REPLY_MESSAGE)
                } else {
                    apiServices.replyMessage(request)
                }
            }
        }.asLiveData()
    }

    fun markAsRead(messageId: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.REPLY_MESSAGE)
                } else {
                    apiServices.markAsRead(messageId)
                }
            }
        }.asLiveData()
    }

    fun uploadImage(file: MultipartBody.Part): LiveData<Resource<ImageUpload>> {
        return object : NetworkResource<ImageUpload>() {
            override fun createCall(): LiveData<Resource<ImageUpload>> {
                return apiServices.uploadImage(file)
            }
        }.asLiveData()
    }
}