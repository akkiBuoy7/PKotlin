package plm.patientslikeme2.ui.main.viewmodel.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.model.profile.UpdatePersonalInfoRequest
import plm.patientslikeme2.data.repository.profile.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository) :
    ViewModel() {

    var userId: String = ""

    private fun fetchMemberConditions() = repository.getMemberConditions(userId)

    fun getMemberConditions() = fetchMemberConditions()

    private fun fetchMemberFollowings() = repository.getMemberFollowings(userId)

    fun getMemberFollowings() = fetchMemberFollowings()

    private fun fetchMemberProfile() = repository.getMemberProfile(userId)

    fun getMemberProfile() = fetchMemberProfile()

    private fun setMemberFollow() = repository.memberFollow(userId)

    fun memberFollow() = setMemberFollow()

    private fun setMemberUnFollow() = repository.memberUnFollow(userId)

    fun memberUnFollow() = setMemberUnFollow()

    private fun fetchStaticMasterData() = repository.getStaticMasterData()

    fun getStaticMasterData() = fetchStaticMasterData()

    private fun fetchUserDetails() = repository.getUserDetails()

    fun getUserDetails() = fetchUserDetails()

    private fun postUserDetails(request: UpdatePersonalInfoRequest) =
        repository.updatePersonalInfo(request)

    fun updatePersonalInfo(request: UpdatePersonalInfoRequest) = postUserDetails(request)
}