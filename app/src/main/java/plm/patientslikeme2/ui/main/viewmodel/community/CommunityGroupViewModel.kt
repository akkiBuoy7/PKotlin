package plm.patientslikeme2.ui.main.viewmodel.community

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.repository.community.CommunityGroupRepository
import javax.inject.Inject

@HiltViewModel
class CommunityGroupViewModel @Inject constructor(private val repository: CommunityGroupRepository) :
    ViewModel() {

    private fun fetchMyGroups() = repository.getMyGroupList()

    private fun fetchSuggestedGroups() = repository.getSuggestedGroupList()

    private fun postJoinGroups(id: Int) = repository.joinGroup(id)

    fun getMyGroupList() = fetchMyGroups()

    fun getSuggestedGroupList() = fetchSuggestedGroups()

    fun joinGroup(id: Int) = postJoinGroups(id)

    private fun postUnjoinGroups(id: Int) = repository.leaveGroup(id)

    fun unjoinGroup(id: Int) = postUnjoinGroups(id)

    fun queryGroup(query:String) = repository.queryGroup(query)

    fun getAllGroupsList() = repository.getAllGroupList()
}