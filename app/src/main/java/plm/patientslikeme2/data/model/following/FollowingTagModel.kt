package plm.patientslikeme2.data.model.following

data class FollowingTagModel(
    var id: Int,
    var text: String,
    var showDelete: Boolean,
)

data class FollowingTagResponse(
    var success: Boolean,
    var message: String,
    var data: FollowingTagData
)

data class FollowingTagData(
    var tags: ArrayList<FollowingTagModel>
)