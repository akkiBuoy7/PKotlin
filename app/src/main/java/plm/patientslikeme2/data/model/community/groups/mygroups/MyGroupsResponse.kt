package plm.patientslikeme2.data.model.community.groups.mygroups


import com.google.gson.annotations.SerializedName

data class MyGroupsResponse(
    @SerializedName("data") var data: Data? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("success") var success: Boolean? = null
)

data class Data(
    @SerializedName("my_groups") var myGroups: List<MyGroup>? = null
)

data class MyGroup(
    @SerializedName("description") var description: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("key") var key: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("new_stats") var newStats: NewStats = NewStats(),
    @SerializedName("restricted") var restricted: Boolean = false,
)

data class NewStats(
    @SerializedName("comments") var comments: Int = 0,
    @SerializedName("discussions") var discussions: Int = 0,
    @SerializedName("members") var members: Int = 0
)