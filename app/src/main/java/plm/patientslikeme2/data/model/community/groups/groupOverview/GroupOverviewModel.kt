package plm.patientslikeme2.data.model.community.groups.groupOverview

import com.google.gson.annotations.SerializedName
import plm.patientslikeme2.data.model.Discussions

data class GroupOverviewResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = null,
)

data class Moderator(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("profile_pic") var profilePic: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("login") var login: String? = null

)

data class NewStats(
    @SerializedName("members") var members: Int? = null,
    @SerializedName("discussions") var discussions: Int? = null,
    @SerializedName("comments") var comments: Int? = null
)

data class TopContributors(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("full_name") var name: String? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null
)

data class Highlights(
    @SerializedName("new_stats") var newStats: NewStats? = NewStats(),
    @SerializedName("top_contributors") var topContributors: ArrayList<TopContributors> = arrayListOf()
)

data class Events(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("destination_url") var destinationUrl: String? = null,
    @SerializedName("start_time") var startTime: String? = null,
    @SerializedName("end_time") var endTime: String? = null,
    @SerializedName("event_type") var eventType: String? = null,
    @SerializedName("cover_photo") var coverPhoto: String? = null,
    @SerializedName("short_description") var shortDescription: String? = null,
    @SerializedName("long_description") var longDescription: String? = null,
    @SerializedName("followed") var followed: Boolean? = null
)

data class Avatar(
    @SerializedName("type") var type: String? = null,
    @SerializedName("value") var value: String? = null
)

data class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("badge") var badge: ArrayList<String>? = null,
    @SerializedName("avatar") var avatar: Avatar? = Avatar(),
    @SerializedName("login") var login: String? = null,
    @SerializedName("followed") var followed: Boolean? = null
)

data class Group(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("is_member") var isMember: Boolean = false,
    @SerializedName("moderator") var moderator: Moderator? = null,
    @SerializedName("highlights") var highlights: Highlights? = Highlights(),
    @SerializedName("events") var events: ArrayList<Events> = arrayListOf(),
    @SerializedName("featured_discussions") var featuredDiscussions: ArrayList<Discussions> = arrayListOf()
)

data class Data(
    @SerializedName("group_overview") var group: Group? = Group()
)