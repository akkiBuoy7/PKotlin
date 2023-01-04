package plm.patientslikeme2.ui.main.viewmodel.community.resource

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.repository.community.resource.ResourceRepository
import plm.patientslikeme2.data.repository.following.FollowingRepository
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(
    private val repository: ResourceRepository,
    private val followingRepository: FollowingRepository
) :
    ViewModel() {

    fun getResourceOverview(id: String?) = repository.getResourceOverview(id)

    private fun updateUnFollowResource(id: String?) =
        followingRepository.postUnFollowResource(id)

    fun postUnFollowResource(id: String?) = updateUnFollowResource(id)

    private fun updateFollowResource(id: String?) =
        followingRepository.postFollowResource(id)

    fun postFollowResource(id: String?) = updateFollowResource(id)
}
