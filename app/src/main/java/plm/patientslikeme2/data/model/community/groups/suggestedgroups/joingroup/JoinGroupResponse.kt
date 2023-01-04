package plm.patientslikeme2.data.model.community.groups.suggestedgroups.joingroup


import com.google.gson.annotations.SerializedName

data class JoinGroupResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)