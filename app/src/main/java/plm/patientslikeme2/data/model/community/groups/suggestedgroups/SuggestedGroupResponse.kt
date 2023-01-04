package plm.patientslikeme2.data.model.community.groups.suggestedgroups


import com.google.gson.annotations.SerializedName

data class SuggestedGroupResponse(
    @SerializedName("data") var data: Data? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("success") var success: Boolean? = null
)

data class Data(
    @SerializedName("suggested_groups") var suggestedGroups: List<SuggestedGroup>? = null
)

data class Stats(
    @SerializedName("members") var members: Int = 0
)

data class SuggestedGroup(
    @SerializedName("description") var description: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("key") var key: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("stats") var stats: Stats = Stats(),
    @SerializedName("restricted") var restricted: Boolean = false,
    var isJoined: Boolean = false
)