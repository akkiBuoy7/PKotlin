package plm.patientslikeme2.data.model.community.groups.discussions

data class FlagPost(
    var bucket: Bucket? = null,
)

data class Bucket(
    var topic_ids: ArrayList<Int>
)