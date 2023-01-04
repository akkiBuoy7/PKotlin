package plm.patientslikeme2.data.model.community.groups.discussions

import com.google.gson.annotations.SerializedName
import plm.patientslikeme2.data.model.Discussions
import plm.patientslikeme2.data.model.Meta
import plm.patientslikeme2.data.model.RecentComments

data class GroupDiscussions(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("meta") var meta: Meta? = Meta()
)

data class Avatar(
    @SerializedName("type") var type: String? = null,
    @SerializedName("value") var value: String? = null
)


data class Badges(
    @SerializedName("badge") var badge: String? = null,
    @SerializedName("name") var name: String? = null
)


data class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("login") var login: String? = null,
    @SerializedName("avatar") var avatar: Avatar? = Avatar(),
    @SerializedName("badges") var badges: ArrayList<Badges> = arrayListOf(),
    @SerializedName("followed") var followed: Boolean? = null
)

data class Data(
    @SerializedName("discussions") var discussions: ArrayList<Discussions> = arrayListOf(),
    @SerializedName("discussion") var discussion: Discussions? = null,
    @SerializedName("comments") var comments: ArrayList<Discussions> = arrayListOf(),
    @SerializedName("comment") var comment: RecentComments? = null
)
