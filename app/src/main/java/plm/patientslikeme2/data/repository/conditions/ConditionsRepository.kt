package plm.patientslikeme2.data.repository.conditions

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
import plm.patientslikeme2.data.model.conditions.*
import plm.patientslikeme2.data.model.signup.ConditionsResponse
import plm.patientslikeme2.data.model.signup.ConditionsStageResponse
import plm.patientslikeme2.data.model.signup.DeleteConditionsResponse
import plm.patientslikeme2.data.model.treatments.AddTreatmentRequest
import plm.patientslikeme2.data.model.treatments.AddTreatmentRequestResponse
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConditionsRepository @Inject constructor(
    private val apiServices: ApiServices, @ApplicationContext val context: Context
) {
    fun getMyConditions(): LiveData<Resource<MyConditionsResponse>> {
        return object : NetworkResource<MyConditionsResponse>() {
            override fun createCall(): LiveData<Resource<MyConditionsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MY_CONDITION_LISTING)
                } else {
                    apiServices.getMyConditions()
                }
            }
        }.asLiveData()
    }

    fun getSearchCondition(query: String): LiveData<Resource<ConditionsResponse>> {
        return object : NetworkResource<ConditionsResponse>() {
            override fun createCall(): LiveData<Resource<ConditionsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.SEARCH_CONDITIONS)
                } else {
                    apiServices.getSearchCondition(query)
                }
            }
        }.asLiveData()
    }

    fun getSuggestedSymptoms(query: String): LiveData<Resource<SuggestedSymptomsResponse>> {
        return object : NetworkResource<SuggestedSymptomsResponse>() {
            override fun createCall(): LiveData<Resource<SuggestedSymptomsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.SUGGESTED_SYMPTOMS)
                } else {
                    apiServices.getSuggestedSymptoms(query)
                }
            }
        }.asLiveData()
    }

    fun getRelatedStages(query: String): LiveData<Resource<ConditionsStageResponse>> {
        return object : NetworkResource<ConditionsStageResponse>() {
            override fun createCall(): LiveData<Resource<ConditionsStageResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.SUGGESTED_SYMPTOMS)
                } else {
                    apiServices.getRelatedStages(query)
                }
            }
        }.asLiveData()
    }

    fun getSearchSymptomsCondition(query: String): LiveData<Resource<SearchSymptomResponse>> {
        return object : NetworkResource<SearchSymptomResponse>() {
            override fun createCall(): LiveData<Resource<SearchSymptomResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.SEARCH_SYMPTOMS)
                } else {
                    apiServices.getSearchSymptomsCOndition(query)
                }
            }
        }.asLiveData()
    }

    fun postAddConditionRequest(request: AddConditionRequest): LiveData<Resource<AddConditionRequestResponse>> {
        return object : NetworkResource<AddConditionRequestResponse>() {
            override fun createCall(): LiveData<Resource<AddConditionRequestResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.ADD_CONDITIONS)
                } else {
                    apiServices.postAddConditionRequest(request)
                }
            }
        }.asLiveData()
    }

    fun putEditCondition(request: AddConditionRequest): LiveData<Resource<AddConditionRequestResponse>> {
        return object : NetworkResource<AddConditionRequestResponse>() {
            override fun createCall(): LiveData<Resource<AddConditionRequestResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.UPDATE_CONDITIONS)
                } else {
                    apiServices.putEditCondition(request)
                }
            }
        }.asLiveData()
    }

    fun postAddSymptomRequest(request: AddSymptomRequest): LiveData<Resource<AddSymptomRequestResponse>> {
        return object : NetworkResource<AddSymptomRequestResponse>() {
            override fun createCall(): LiveData<Resource<AddSymptomRequestResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.ADD_SYMPTOM)
                } else {
                    apiServices.postAddSymptomRequest(request)
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

    fun getConditionDetails(conditionId: String): LiveData<Resource<ConditionsDetailsResponse>> {
        return object : NetworkResource<ConditionsDetailsResponse>() {
            override fun createCall(): LiveData<Resource<ConditionsDetailsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.MY_CONDITION_DETAILS)
                } else {
                    apiServices.getConditionDetails(conditionId.toInt())
                }
            }
        }.asLiveData()
    }

    fun deleteSymptoms(id: String): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.COMMON)
                } else {
                    apiServices.deleteSymptom(id.toInt())
                }
            }
        }.asLiveData()
    }

    fun deleteCondition(id: String): LiveData<Resource<DeleteConditionsResponse>> {
        return object : NetworkResource<DeleteConditionsResponse>() {
            override fun createCall(): LiveData<Resource<DeleteConditionsResponse>> {
                return if (MyApplication.APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.DELETE_CONDITIONS)
                } else {
                    apiServices.deleteCondition(id.toInt())
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
}