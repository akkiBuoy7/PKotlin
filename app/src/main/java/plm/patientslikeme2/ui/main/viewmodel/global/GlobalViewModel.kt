package plm.patientslikeme2.ui.main.viewmodel.global

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.repository.global.GlobalRepository
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(private val repository: GlobalRepository) :
    ViewModel() {

    fun getGlobalDataRequest() = repository.getGlobalData()

    fun getFeatureFlagData() = repository.getFeatureFlag()

    fun refreshToken() = repository.refreshToken()
}