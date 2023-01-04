package plm.patientslikeme2.data.model.community.groups.discussions

data class FlagCommentReply(
    var bucket: BucketFlagCommentReply? = null,
)

data class BucketFlagCommentReply(
    var post_ids: ArrayList<Int>
)