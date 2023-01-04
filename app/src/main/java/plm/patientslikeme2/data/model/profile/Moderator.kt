package plm.patientslikeme2.data.model.profile

data class Moderator(
    var id: Int,
    var login: String,
    var avatar: ModeratorAvatar
)

data class ModeratorAvatar(
    var type: String,
    var value: String
)