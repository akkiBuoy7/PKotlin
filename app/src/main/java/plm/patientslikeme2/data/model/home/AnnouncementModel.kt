package plm.patientslikeme2.data.model.home

data class AnnouncementModel(
    var id: Int,
    var message: String,
    var lifetime_desc: String
)

data class AnnouncementResponse(
    var success: Boolean,
    var message: String,
    var data: AnnouncementData
)

data class AnnouncementData(
    var announcements: ArrayList<AnnouncementModel>
)