package plm.patientslikeme2.data.model.community.groups.discussions

data class AddComment(
    var comment: Comment? = null
)

data class Comment(
    var body: String? = null,
    var markdown: String? = null,
)