package plm.patientslikeme2.ui.main.viewmodel.messages

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import plm.patientslikeme2.data.model.messages.NewMessageRequest
import plm.patientslikeme2.data.model.messages.ReplyMessageRequest
import plm.patientslikeme2.data.repository.messages.MessagingRepository
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(private val repository: MessagingRepository) :
    ViewModel() {
    var otherParticipantId: String = ""
    var otherParticipantName: String = ""
    var messageId: String = ""
    var replyMessageId: String = ""
    var replyMessageText: String = ""
    var messageConversationPage: Int = 1
    var messagesPage: Int = 1
    var blockedByOtherParticipant: Boolean = false
    var optionCallFrom: String = ""

    private fun fetchViewAllMessages(query: String) =
        repository.getViewAllMessages(query, messagesPage)

    fun getViewAllMessages(query: String) = fetchViewAllMessages(query)

    private fun fetchMessageThreadConversation() =
        repository.getMessageThreadConversation(otherParticipantId, messageConversationPage)

    fun getMessageThreadConversation() = fetchMessageThreadConversation()

    private fun putBlockMember() = repository.putBlockMember(otherParticipantId)

    fun blockMember() = putBlockMember()

    private fun putReportAsSpam() = repository.putReportAsSpam(messageId)

    fun reportAsSpam() = putReportAsSpam()

    private fun fetchSearchMembers(query: String) = repository.getSearchMembers(query)

    fun getSearchMembers(query: String) = fetchSearchMembers(query)

    private fun postNewMessage(request: NewMessageRequest) = repository.sendNewMessage(request)

    fun sendNewMessage(request: NewMessageRequest) = postNewMessage(request)

    private fun postReplyMessage(request: ReplyMessageRequest) = repository.replyMessage(request)

    fun replyMessage(request: ReplyMessageRequest) = postReplyMessage(request)

    private fun putMarkAsRead() = repository.markAsRead(messageId)

    fun markAsRead() = putMarkAsRead()

    fun uploadImage(filePath: MultipartBody.Part) = repository.uploadImage(filePath)
}