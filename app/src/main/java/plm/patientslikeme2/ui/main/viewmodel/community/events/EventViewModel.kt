package plm.patientslikeme2.ui.main.viewmodel.community.events

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.repository.community.events.EventsRepository
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val eventsRepository: EventsRepository) :
    ViewModel() {

    fun getEventOverview(id: String?) = eventsRepository.getEventOverview(id)
}
