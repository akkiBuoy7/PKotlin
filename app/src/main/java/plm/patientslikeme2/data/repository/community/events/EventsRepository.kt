package plm.patientslikeme2.data.repository.community.events

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson.COMMUNITY_EVENTS
import plm.patientslikeme2.data.di.LocalJson.GROUP_EVENTS
import plm.patientslikeme2.data.di.LocalJson.GROUP_RESOURCES
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.community.events.EventDetailsResponse
import plm.patientslikeme2.data.model.community.events.EventResponse
import plm.patientslikeme2.data.model.community.resource.ResourceResponse
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
) {
    fun getCommunityEvents(): LiveData<Resource<EventResponse>> {
        return object : NetworkResource<EventResponse>() {
            override fun createCall(): LiveData<Resource<EventResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(COMMUNITY_EVENTS)
                } else {
                    apiServices.getCommunityEvents()
                }
            }
        }.asLiveData()
    }

    fun getGroupEvents(id: String?): LiveData<Resource<EventResponse>> {
        return object : NetworkResource<EventResponse>() {
            override fun createCall(): LiveData<Resource<EventResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(GROUP_EVENTS)
                } else {
                    apiServices.getGroupEvents(id)
                }
            }
        }.asLiveData()
    }

    fun getEventOverview(id: String?): LiveData<Resource<EventDetailsResponse>> {
        return object : NetworkResource<EventDetailsResponse>() {
            override fun createCall(): LiveData<Resource<EventDetailsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(GROUP_EVENTS)
                } else {
                    apiServices.getEventOverview(id)
                }
            }
        }.asLiveData()
    }

    fun getGroupResource(id: String?): LiveData<Resource<ResourceResponse>> {
        return object : NetworkResource<ResourceResponse>() {
            override fun createCall(): LiveData<Resource<ResourceResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(GROUP_RESOURCES)
                } else {
                    apiServices.getGroupResources(id)
                }
            }
        }.asLiveData()
    }

    fun getGroupFilterResource(id: String?): LiveData<Resource<ResourceResponse>> {
        return object : NetworkResource<ResourceResponse>() {
            override fun createCall(): LiveData<Resource<ResourceResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(GROUP_RESOURCES)
                } else {
                    apiServices.getGroupFilterResource(Constants.MY_FOLLOWINGS)
                }
            }
        }.asLiveData()
    }
}