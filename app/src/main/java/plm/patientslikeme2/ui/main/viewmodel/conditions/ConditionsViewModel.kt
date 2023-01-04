package plm.patientslikeme2.ui.main.viewmodel.conditions

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.model.conditions.AddConditionRequest
import plm.patientslikeme2.data.model.conditions.AddSymptomRequest
import plm.patientslikeme2.data.model.treatments.AddTreatmentRequest
import plm.patientslikeme2.data.repository.conditions.ConditionsRepository
import javax.inject.Inject

@HiltViewModel
class ConditionsViewModel @Inject constructor(private val repository: ConditionsRepository) :
    ViewModel() {

    var conditionId: String = ""

    private fun fetchMyConditions() = repository.getMyConditions()

    fun getMyConditions() = fetchMyConditions()

    fun getSearchCondition(search: String) = repository.getSearchCondition(search)

    fun getSuggestedSymptoms(search: String) = repository.getSuggestedSymptoms(search)

    fun getRelatedStages(search: String) = repository.getRelatedStages(search)

    fun postAddCondition(request: AddConditionRequest) = repository.postAddConditionRequest(request)

    fun putEditCondition(request: AddConditionRequest) = repository.putEditCondition(request)

    fun getConditionDetails() = repository.getConditionDetails(conditionId)

    fun postAddSymptom(request: AddSymptomRequest) = repository.postAddSymptomRequest(request)

    fun postAddTreatment(request: AddTreatmentRequest) = repository.postTreatmentRequest(request)

    fun getSearchSymptomsCondition(search: String) = repository.getSearchSymptomsCondition(search)

    fun deleteSymptoms(symptom_info_id: String) = repository.deleteSymptoms(symptom_info_id)

    fun deleteCondition() = repository.deleteCondition(conditionId)

    fun deleteTreatments(treatmentId: String) = repository.deleteMyTreatments(treatmentId)

    fun putEditTreatments(request: AddTreatmentRequest, treatmentId: Int) =
        repository.putEditTreatmentRequest(request, treatmentId)

}