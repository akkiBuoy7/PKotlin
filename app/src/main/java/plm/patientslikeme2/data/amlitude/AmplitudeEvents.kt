package plm.patientslikeme2.data.amlitude

import plm.patientslikeme2.data.di.MyApplication.Companion.amplitude

object AmplitudeEvents {

    fun trackEvent(type: String, map: MutableMap<String, Any?>) {
        amplitude?.track(type, map)
    }
}