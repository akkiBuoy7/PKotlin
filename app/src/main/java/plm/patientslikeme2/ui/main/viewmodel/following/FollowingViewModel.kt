package plm.patientslikeme2.ui.main.viewmodel.following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.model.following.FollowingDiscussionResponse
import plm.patientslikeme2.data.model.following.FollowingMemberResponse
import plm.patientslikeme2.data.model.following.FollowingResourceResponse
import plm.patientslikeme2.data.model.following.FollowingTagResponse
import plm.patientslikeme2.data.repository.following.FollowingRepository
import plm.patientslikeme2.data.repository.profile.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val repository: FollowingRepository,
    private val profileRepository: ProfileRepository
) :
    ViewModel() {

    private val pageSize = 10
    var discussionPageNo = 1
    var memberPageNo = 1
    var tagPageNo = 1
    var resourcePageNo = 1

    var discussionFragment = MutableLiveData<Int>()
    var memberFragment = MutableLiveData<Int>()
    var tagFragment = MutableLiveData<Int>()
    var resourceFragment = MutableLiveData<Int>()

    var discussionResponse = MutableLiveData<Resource<FollowingDiscussionResponse>>()
    var memberResponse = MutableLiveData<Resource<FollowingMemberResponse>>()
    var tagResponse = MutableLiveData<Resource<FollowingTagResponse>>()
    var resourceResponse = MutableLiveData<Resource<FollowingResourceResponse>>()

    private fun fetchFollowingDiscussion(page: Int?) =
        repository.getFollowingDiscussion(page, pageSize)

    fun getFollowingDiscussion(page: Int?) = fetchFollowingDiscussion(page)

    private fun fetchMemberFollowings(page: Int?) = repository.getFollowingMember(page, pageSize)

    fun getFollowingMembers(page: Int?) = fetchMemberFollowings(page)

    private fun fetchFollowingTags(page: Int?) = repository.getFollowingTags(page, pageSize)

    fun getFollowingTags(page: Int?) = fetchFollowingTags(page)

    private fun fetchFollowingResources(page: Int?) =
        repository.getFollowingResources(page, pageSize)

    fun getFollowingResources(page: Int?) = fetchFollowingResources(page)

    private fun fetchTagDetailsList(tag: String?, page: Int?) =
        repository.getTagDetailsList(tag, page, pageSize)

    fun getTagDetailsList(tag: String?, page: Int?) = fetchTagDetailsList(tag, page)

    private fun updateFollowTagDetailsDiscussion(id: String?) =
        repository.postFollowTagDetailsDiscussion(id)

    fun postFollowTagDetailsDiscussion(id: String?) = updateFollowTagDetailsDiscussion(id)

    private fun updateUnFollowTag(id: String?) =
        repository.postUnFollowTag(id)

    fun postUnFollowTag(id: String?) = updateUnFollowTag(id)

    private fun updateFollowTag(id: String?) =
        repository.postFollowTag(id)

    fun postFollowTag(id: String?) = updateFollowTag(id)

    private fun setMemberUnFollow(id: String?) = profileRepository.memberUnFollow(id.toString())

    fun memberUnFollow(id: String?) = setMemberUnFollow(id)

    private fun updateUnFollowDiscussion(id: String?) =
        repository.postUnFollowDiscussion(id)

    fun postUnFollowDiscussion(id: String?) = updateUnFollowDiscussion(id)

    private fun updateUnFollowResource(id: String?) =
        repository.postUnFollowResource(id)

    fun postUnFollowResource(id: String?) = updateUnFollowResource(id)

    private fun updateFollowResource(id: String?) =
        repository.postFollowResource(id)

    fun postFollowResource(id: String?) = updateFollowResource(id)

}