package plm.patientslikeme2.data.model.following

data class FollowingDiscussionModel(
    var id: Int,
    var body_html: String,
    var subject: String,
    var group_name: String,
    var reply_count: String,
    var helpful_count: String,
    var featured: Boolean = false,
    var notify_updates: Boolean = false,
    var showDelete: Boolean = false
)

data class FollowingDiscussionResponse(
    var success: Boolean,
    var message: String,
    var data: FollowingDiscussionData
)

data class FollowingDiscussionData(
    var discussions: ArrayList<FollowingDiscussionModel>
)