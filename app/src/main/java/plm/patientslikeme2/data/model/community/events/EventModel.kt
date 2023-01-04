package plm.patientslikeme2.data.model.community.events

data class EventModel(
    var id: Int,
    var event_type: String,
    var short_description: String,
    var destination_url: String,
    var cover_photo: String,
    var long_description: String,
    var start_time: String,
    var end_time: String,
    var followed: Boolean,
)

data class EventResponse(
    var success: Boolean,
    var message: String,
    var data: EventData
)

data class EventData(
    var events: ArrayList<EventModel>
)