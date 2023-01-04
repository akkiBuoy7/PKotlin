package plm.patientslikeme2.data.repository.community.resource

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson.GROUP_EVENTS
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.community.resource.ResourceDetailsResponse
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {

    fun getResourceOverview(id: String?): LiveData<Resource<ResourceDetailsResponse>> {
        return object : NetworkResource<ResourceDetailsResponse>() {
            override fun createCall(): LiveData<Resource<ResourceDetailsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(GROUP_EVENTS)
                } else {
                    apiServices.getResourceOverview(id)
                }
            }
        }.asLiveData()
    }
}