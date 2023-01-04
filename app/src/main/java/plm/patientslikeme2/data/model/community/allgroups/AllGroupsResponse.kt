package plm.patientslikeme2.data.model.community.allgroups


import com.google.gson.annotations.SerializedName

data class AllGroupsResponse(
    @SerializedName("data") val data: Data,
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean
)

data class Data(
    @SerializedName("groups") val groups: List<Group>
)

data class Group(
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: Int,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("stats") val stats: Stats,
    @SerializedName("is_member") var isMember: Boolean,
    @SerializedName("restricted") var restricted: Boolean = false,
    var isJoined: Boolean
)

data class Stats(
    @SerializedName("members") val members: Int
)