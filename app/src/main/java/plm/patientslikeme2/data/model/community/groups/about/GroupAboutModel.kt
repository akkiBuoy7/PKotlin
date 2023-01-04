package plm.patientslikeme2.data.model.community.groups.about

import com.google.gson.annotations.SerializedName
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.SuggestedGroup

data class GroupAboutModel(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()
)

data class Moderator(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("profile_pic") var profilePic: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("user_badges") var userBadges: ArrayList<String> = arrayListOf()
)

data class Stats(
    @SerializedName("members") var members: Int? = null
)


data class Data(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("media_url") var mediaUrl: String? = null,
    @SerializedName("moderator") var moderator: Moderator? = Moderator(),
    @SerializedName("name") var name: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("suggested_groups") var suggestedGroups: ArrayList<SuggestedGroup> = arrayListOf()
)