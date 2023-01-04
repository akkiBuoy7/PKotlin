package plm.patientslikeme2.ui.main.viewmodel.community.groups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.model.Discussions
import plm.patientslikeme2.data.model.RecentComments
import plm.patientslikeme2.data.repository.community.discussions.GroupDiscussionRepository
import javax.inject.Inject

@HiltViewModel
class DiscussionReplyViewModel @Inject constructor(private val repository: GroupDiscussionRepository) : ViewModel() {

    var discussionId: String = ""
    var commentId: String = ""
    var isMember: Boolean = false
    var recentComment: MutableLiveData<RecentComments> = MutableLiveData()
    var totalPage = 0
    var currentPage = "1"

    fun getCommentDetails() = repository.getCommentDetails(discussionId, commentId)

    fun getReplyOnComments() = repository.getReplyOnComments(discussionId, commentId, currentPage)

    fun replyLikeUnlike( id: Int, markedHelpful: Boolean) = repository.replyLikeUnlike(discussionId, commentId, id, markedHelpful)

    fun commentLikeUnlike(markedHelpful: Boolean) = repository.commentLikeUnlike(discussionId, commentId, markedHelpful)
}