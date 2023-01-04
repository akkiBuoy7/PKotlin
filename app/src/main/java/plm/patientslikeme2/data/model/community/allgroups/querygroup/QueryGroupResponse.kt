package plm.patientslikeme2.data.model.community.allgroups.querygroup


import com.google.gson.annotations.SerializedName

data class QueryGroupResponse(
    @SerializedName("data") val `data`: Data,
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean
)

data class Group(
    @SerializedName("id") val id: Int,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String
){
    override fun toString(): String {
        return name
    }
}

data class Data(
    @SerializedName("groups") val groups: List<Group>
)