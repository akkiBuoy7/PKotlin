package plm.patientslikeme2.data.model.community.events

import plm.patientslikeme2.data.model.profile.Moderator

data class EventDetailsModel(
    var id: Int,
    var title: String,
    var description: String,
    var date: String,
    var time: String,
    var duration: String,
    var timezone: String,
    var start_time: String,
    var end_time: String,
    var cover_photo: String,
    var destination_url: String,
    var call_to_action: String,
    var followed: Boolean,
    var moderator: Moderator
)

data class EventDetailsResponse(
    var success: Boolean, var message: String, var data: EventDetailsData
)

data class EventDetailsData(
    var event: EventDetailsModel
)