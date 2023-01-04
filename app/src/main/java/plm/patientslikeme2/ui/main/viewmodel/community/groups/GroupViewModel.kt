package plm.patientslikeme2.ui.main.viewmodel.community.groups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.model.community.events.EventResponse
import plm.patientslikeme2.data.model.community.groups.about.GroupAboutModel
import plm.patientslikeme2.data.model.community.groups.discussions.GroupDiscussions
import plm.patientslikeme2.data.model.community.groups.discussions.NewDiscussionRequest
import plm.patientslikeme2.data.model.community.groups.discussions.RecommendedTagsResponse
import plm.patientslikeme2.data.model.community.groups.groupOverview.GroupOverviewResponse
import plm.patientslikeme2.data.model.community.groups.groupOverview.Moderator
import plm.patientslikeme2.data.model.community.groups.members.MemberResponse
import plm.patientslikeme2.data.model.community.resource.ResourceResponse
import plm.patientslikeme2.data.repository.community.CommunityGroupRepository
import plm.patientslikeme2.data.repository.community.discussions.GroupDiscussionRepository
import plm.patientslikeme2.data.repository.community.events.EventsRepository
import plm.patientslikeme2.utils.Constants
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repository: CommunityGroupRepository,
    private val eventsRepository: EventsRepository,
    private val discussionRepository: GroupDiscussionRepository
) : ViewModel() {

    var groupId: String = ""
    var isPrivateGroup: Boolean = false
    var groupOverviewResponse = MutableLiveData<Resource<GroupOverviewResponse>>()
    var moderatorResponse = MutableLiveData<Moderator>()
    var aboutGroupResponse = MutableLiveData<Resource<GroupAboutModel>>()

    var groupDiscussions = MutableLiveData<Resource<GroupDiscussions>>()
    var groupDiscussionsTotalPage = 0
    var groupDiscussionsCurrentPage = "1"
    var groupDiscussionsSortBy = Constants.RECENT_ACTIVITY
    var groupDiscussionsFilterMyFollowings = false
    var groupDiscussionsFilterTagsConditions = false
    var recommendedTags = MutableLiveData<Resource<RecommendedTagsResponse>>()


    var newMemberResponse = MutableLiveData<Resource<MemberResponse>>()
    var membersResponse = MutableLiveData<Resource<MemberResponse>>()

    var eventResponse = MutableLiveData<Resource<EventResponse>>()
    var resourceResponse = MutableLiveData<Resource<ResourceResponse>>()

    var allMemberTotalPage = 0
    var allMemberCurrentPage = "1"
    var allMemberSortBy = Constants.NEWEST
    var allMemberFilterMyFollowings = false
    var allMemberFilterMyConditions = false
    var discussionRequest = NewDiscussionRequest()

    var filterAllResources = true
    var filterResourcesImFollowing = false

    fun groupOverview() = repository.getGroupOverview(groupId)

    fun getAboutGroup() = repository.getAboutGroup(groupId)

    fun joinGroup(id: Int) = repository.joinGroup(id)

    fun unJoinGroup(id: Int) = repository.leaveGroup(id)

    fun getNewMembers() = repository.getNewMembers(groupId)

    fun getMembers() = getAllUsers()

    fun fetchFilteredUsers() = getAllUsers()

    fun fetchPaginatedUsers() = getAllUsers()

    private fun getAllUsers() = repository.getMembers(
        groupId,
        allMemberSortBy,
        allMemberFilterMyFollowings,
        allMemberFilterMyConditions,
        allMemberCurrentPage
    )

    fun searchUsers(query: String) = repository.searchUsers(groupId, query)

    fun getGroupEvents() = eventsRepository.getGroupEvents(groupId)

    fun getGroupResource() = eventsRepository.getGroupResource(groupId)

    fun getGroupFilterResource() = eventsRepository.getGroupFilterResource(groupId)

    fun groupDiscussions() = discussions()

    fun getRecommendedTags() = discussionRepository.getRecommendedTags(groupId)

    fun searchTags(query: String) = discussionRepository.searchTags(query)

    fun createDiscussion() = discussionRepository.createDiscussion(groupId, discussionRequest)

    fun searchGroupDiscussions(query: String) =
        discussionRepository.searchGroupDiscussions(groupId, query)

    private fun discussions() =
        discussionRepository.groupDiscussions(
            groupId,
            groupDiscussionsSortBy,
            groupDiscussionsFilterMyFollowings,
            groupDiscussionsFilterTagsConditions,
            groupDiscussionsCurrentPage
        )

    fun discussionLikeUnlike(id: Int, markedHelpful: Boolean) =
        discussionRepository.discussionLikeUnlike(id, markedHelpful)

    fun uploadImage(filePath: MultipartBody.Part) = discussionRepository.uploadImage(filePath)

    fun blockUser(userId: Int) = discussionRepository.blockUser(userId)

    fun flagPost(postId: Int) = discussionRepository.flagPost(postId)

    fun flagCommentReply(postId: Int) = discussionRepository.flagCommentReply(postId)

    fun resetInitialValues() {
        groupDiscussionsTotalPage = 0
        groupDiscussionsCurrentPage = "1"
        groupDiscussionsSortBy = Constants.RECENT_ACTIVITY
        groupDiscussionsFilterMyFollowings = false
        groupDiscussionsFilterTagsConditions = false

        allMemberTotalPage = 0
        allMemberCurrentPage = "1"
        allMemberSortBy = Constants.NEWEST
        allMemberFilterMyFollowings = false
        allMemberFilterMyConditions = false
    }
}
