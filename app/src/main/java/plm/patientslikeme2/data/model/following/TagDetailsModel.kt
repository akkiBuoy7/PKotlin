package plm.patientslikeme2.data.model.following

data class TagDetailsModel(
    var id: Int,
    var title: String,
    var group_name: String,
    var comment_count: String,
    var helpful_count: String,
    var featured: Boolean = false,
    var followed: Boolean = false,
    var notify_me: Boolean = false,
    var marked_helpful: Boolean = false
)

data class TagDetailsResponse(
    var success: Boolean,
    var message: String,
    var data: TagDetailsData
)

data class TagDetailsData(
    var followed: Boolean,
    var discussions: ArrayList<TagDetailsModel>
)