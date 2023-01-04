package plm.patientslikeme2.ui.main.viewmodel.treatments

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.model.treatments.AddTreatmentRequest
import plm.patientslikeme2.data.repository.treatments.TreatmentsRepository
import javax.inject.Inject

@HiltViewModel
class TreatmentsViewModel @Inject constructor(private val repository: TreatmentsRepository) :
    ViewModel() {

    var treatmentId: String = ""

    fun getMyTreatments() = repository.getMyTreatments()

    fun getSearchTreatments(search: String) = repository.getSearchTreatments(search)

    fun getSuggestedPurpose(search: String) = repository.getSearchPurpose(search)

    fun deleteTreatments() = repository.deleteMyTreatments(treatmentId)

    fun getTreatmentCategory(treatmentId1: String, categoryId: String) =
        repository.getTreatmentCategory(treatmentId1, categoryId)

    fun postAddTreatment(request: AddTreatmentRequest) = repository.postTreatmentRequest(request)

    fun getEditTreatments(treatmentId: String) = repository.getEditTreatments(treatmentId)

    fun putEditTreatments(request: AddTreatmentRequest, treatmentId: Int) =
        repository.putEditTreatmentRequest(request, treatmentId)

}