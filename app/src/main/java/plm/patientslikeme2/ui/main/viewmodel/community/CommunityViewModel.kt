package plm.patientslikeme2.ui.main.viewmodel.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.model.community.events.EventResponse
import plm.patientslikeme2.data.model.community.groups.mygroups.MyGroupsResponse
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.SuggestedGroupResponse
import plm.patientslikeme2.data.model.community.members.MemberResponse
import plm.patientslikeme2.data.repository.community.CommunityGroupRepository
import plm.patientslikeme2.data.repository.community.events.EventsRepository
import plm.patientslikeme2.data.repository.community.members.CommunityMemberRepository
import plm.patientslikeme2.utils.Constants
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityGroupRepository,
    private val memberRepository: CommunityMemberRepository,
    private val eventsRepository: EventsRepository
) :
    ViewModel() {

    var myGroupsResponse = MutableLiveData<Resource<MyGroupsResponse>>()
    var suggestedGroupResponse = MutableLiveData<Resource<SuggestedGroupResponse>>()

    var membersLikeMeResponse = MutableLiveData<Resource<MemberResponse>>()
    var allMembersResponse = MutableLiveData<Resource<MemberResponse>>()

    var eventResponse = MutableLiveData<Resource<EventResponse>>()

    var allMemberTotalPage = 0
    var allMemberCurrentPage = "1"
    var allMemberSortBy = Constants.NEWEST
    var allMemberFilterMyFollowings = false
    var allMemberFilterMyConditions = false

    fun getMyGroupList() = repository.getMyGroupList()

    fun getSuggestedGroupList() = repository.getSuggestedGroupList()

    fun joinGroup(id: Int) = repository.joinGroup(id)

    fun leaveGroup(id: Int) = repository.leaveGroup(id)

    fun queryGroup(query: String) = repository.queryGroup(query)

    fun getAllMembers() = getAllUsers()

    fun getMembersLikeMe() = memberRepository.getMembersLikeMe()

    fun followMember(id: String) = memberRepository.followMember(id)

    fun unFollowMember(id: String) = memberRepository.unFollowMember(id)

    fun excludedUser(id: Int) = memberRepository.excludedUser(id)

    private fun getAllUsers() = memberRepository.getAllMembers(
        allMemberSortBy,
        allMemberFilterMyFollowings,
        allMemberFilterMyConditions,
        allMemberCurrentPage
    )

    fun fetchFilteredUsers() = getAllUsers()

    fun fetchPaginatedUsers() = getAllUsers()

    fun searchUsers(query: String) = memberRepository.searchUsers(query)

    fun getCommunityEvents() = eventsRepository.getCommunityEvents()
}
