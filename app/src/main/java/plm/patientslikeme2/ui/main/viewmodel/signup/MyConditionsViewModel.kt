package plm.patientslikeme2.ui.main.viewmodel.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.repository.settings.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class MyConditionsViewModel @Inject constructor(private val repository: SettingsRepository) :
    ViewModel() {

    fun updateData() {
        repository
    }
}