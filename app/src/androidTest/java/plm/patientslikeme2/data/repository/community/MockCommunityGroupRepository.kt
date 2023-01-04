package plm.patientslikeme2.data.repository.community

import androidx.lifecycle.LiveData
import plm.patientslikeme2.data.api.MockApiService
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.model.community.groups.about.GroupAboutModel
import plm.patientslikeme2.data.model.community.groups.groupOverview.GroupOverviewResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockCommunityGroupRepository @Inject constructor(private val apiServices: MockApiService) {

    fun getGroupOverview(groupID: String): LiveData<Resource<GroupOverviewResponse>> {
        return apiServices.getGroupOverview(groupID)
    }

    fun getAboutGroup(groupID: String): LiveData<Resource<GroupAboutModel>> {
        return apiServices.getAboutGroup(groupID)
    }
}