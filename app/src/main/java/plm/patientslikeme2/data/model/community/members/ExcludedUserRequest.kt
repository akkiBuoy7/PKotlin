package plm.patientslikeme2.data.model.community.members

data class ExcludedUserRequest(
    var bucket: Bucket? = null,
)

data class Bucket(
    var like_me_user_ids: ArrayList<Int>
)