package plm.patientslikeme2.data.repository.treatments

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.treatments.*
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TreatmentsRepository @Inject constructor(
    private val apiServices: ApiServices, @ApplicationContext val context: Context
) {
    fun getMyTreatments(): LiveData<Resource<MyTreatmentsResponse>> {
        return object : NetworkResource<MyTreatmentsResponse>() {
            override fun createCall(): LiveData<Resource<MyTreatmentsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MY_TREATMENTS)
                } else {
                    apiServices.getMyTreatments()
                }
            }
        }.asLiveData()
    }

    fun getSearchTreatments(query: String): LiveData<Resource<SearchTreatmentResponse>> {
        return object : NetworkResource<SearchTreatmentResponse>() {
            override fun createCall(): LiveData<Resource<SearchTreatmentResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.SEARCH_TREATMENTS)
                } else {
                    apiServices.getSearchTreatments(query)
                }
            }
        }.asLiveData()
    }

    fun getSearchPurpose(query: String): LiveData<Resource<PurposeSearchResponse>> {
        return object : NetworkResource<PurposeSearchResponse>() {
            override fun createCall(): LiveData<Resource<PurposeSearchResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.PURPOSE_SEARCH)
                } else {
                    apiServices.getSearchPurpose(query)
                }
            }
        }.asLiveData()
    }

    fun getTreatmentCategory(
        treatmentId: String, categoryId: String
    ): LiveData<Resource<MyFrequencyDosageResponse>> {
        return object : NetworkResource<MyFrequencyDosageResponse>() {
            override fun createCall(): LiveData<Resource<MyFrequencyDosageResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.CATEGORY_SEARCH)
                } else {
                    apiServices.getTreatmentCategory(categoryId.toInt(), treatmentId.toInt())
                }
            }
        }.asLiveData()
    }

    fun deleteMyTreatments(id: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.COMMON)
                } else {
                    apiServices.deleteTreatment(id.toInt())
                }
            }
        }.asLiveData()
    }

    fun postTreatmentRequest(request: AddTreatmentRequest): LiveData<Resource<AddTreatmentRequestResponse>> {
        return object : NetworkResource<AddTreatmentRequestResponse>() {
            override fun createCall(): LiveData<Resource<AddTreatmentRequestResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.ADD_CONDITIONS)
                } else {
                    apiServices.postAddConditionRequest(request)
                }
            }
        }.asLiveData()
    }

    fun putEditTreatmentRequest(
        request: AddTreatmentRequest, id: Int
    ): LiveData<Resource<AddTreatmentRequestResponse>> {
        return object : NetworkResource<AddTreatmentRequestResponse>() {
            override fun createCall(): LiveData<Resource<AddTreatmentRequestResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.ADD_CONDITIONS)
                } else {
                    apiServices.putEditTreatment(request, id)
                }
            }
        }.asLiveData()
    }

    fun getEditTreatments(treatmentId: String): LiveData<Resource<GetTreatmentResponse>> {
        return object : NetworkResource<GetTreatmentResponse>() {
            override fun createCall(): LiveData<Resource<GetTreatmentResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.GET_EDIT_TREATMENT)
                } else {
                    apiServices.getEditTreatments(treatmentId.toInt())
                }
            }
        }.asLiveData()
    }
}