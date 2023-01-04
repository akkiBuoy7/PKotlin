package plm.patientslikeme2.ui.main.viewmodel.community.groups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import plm.patientslikeme2.data.model.Discussions
import plm.patientslikeme2.data.repository.community.discussions.GroupDiscussionRepository
import plm.patientslikeme2.utils.Constants
import javax.inject.Inject

@HiltViewModel
class DiscussionThreadViewModel @Inject constructor(private val repository: GroupDiscussionRepository) : ViewModel() {

    var discussionId: String = ""
    var isMember: Boolean = false
    var discussion: MutableLiveData<Discussions> = MutableLiveData()
    var totalPage = 0
    var currentPage = "1"
    var groupDiscussionsSortBy = Constants.EMPTY

    fun getComments(discussionID: String) = repository.getCommentsOnDiscussion(discussionID, currentPage,groupDiscussionsSortBy)

    fun getDiscussion(discussionID: String) = repository.getDiscussion(discussionID)

    fun discussionLikeUnlike(id: Int, markedHelpful: Boolean) = repository.discussionLikeUnlike(id, markedHelpful)

    fun replyLikeUnlike(discussionId: String, commentID: String, id: Int, markedHelpful: Boolean) = repository.replyLikeUnlike(discussionId, commentID, id, markedHelpful)

    fun commentLikeUnlike(discussionId: String, commentID: String, markedHelpful: Boolean) = repository.commentLikeUnlike(discussionId, commentID, markedHelpful)

    fun addCommentsOnDiscussion(id: String, body: String) = repository.addCommentsOnDiscussion(id, body)

    fun addReplyOnComments(id: String, commentId: String, body: String) = repository.addReplyOnComments(id, commentId, body)

    fun uploadImage(filePath: MultipartBody.Part) = repository.uploadImage(filePath)

    fun followDiscussions(id: String) = repository.followDiscussions(id)

    fun unfollowDiscussions(id: String) = repository.unfollowDiscussions(id)

}