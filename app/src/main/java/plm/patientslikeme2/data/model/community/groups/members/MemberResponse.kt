package plm.patientslikeme2.data.model.community.groups.members

import com.google.gson.annotations.SerializedName
import plm.patientslikeme2.data.model.Meta

data class MemberResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("meta") var meta: Meta? = Meta()
)

data class Conditions(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
)

data class Badges(
    @SerializedName("name") var name: String? = null,
    @SerializedName("badge") var badge: String = ""
)

data class NewMembers(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("login") var login: String = "",
    @SerializedName("avatar_url") var profilePic: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("living_with") var livingWith: ArrayList<String> = arrayListOf(),
    @SerializedName("conditions") var conditions: ArrayList<Conditions> = arrayListOf(),
    @SerializedName("brief_bio") var briefBio: String = "",
    @SerializedName("sex") var sex: String? = null,
    @SerializedName("age") var age: Int = 0,
    @SerializedName("badges") var badges: ArrayList<Badges> = arrayListOf(),
    @SerializedName("followed") var followed: Boolean = false
) {
    override fun toString(): String {
        return name + login
    }
}

data class Data(
    @SerializedName("members") var newMembers: ArrayList<NewMembers> = arrayListOf()
)