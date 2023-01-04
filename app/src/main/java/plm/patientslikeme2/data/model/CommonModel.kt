package plm.patientslikeme2.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import plm.patientslikeme2.data.model.community.groups.discussions.Avatar
import plm.patientslikeme2.data.model.community.groups.discussions.User

data class Meta(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("per_page") var perPage: Int? = null,
    @SerializedName("total_entries") var totalEntries: Int? = null
)

data class LatestReply(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("user") var user: User? = User(),
    @SerializedName("body") var body: String? = null,
    @SerializedName("time") var time: String? = null
)

data class Tags(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("text") var text: String? = null
)

data class Discussions(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("posted_at") var postedAt: String? = null,
    @SerializedName("featured") var featured: Boolean? = null,
    @SerializedName("tags") var tags: ArrayList<Tags> = arrayListOf(),
    @SerializedName("user") var user: User? = User(),
    @SerializedName("comment_count") var commentCount: Int = 0,
    @SerializedName("helpful_count") var helpfulCount: Int = 0,
    @SerializedName("latest_reply") var latestReply: LatestReply? = LatestReply(),
    @SerializedName("marked_helpful") var markedHelpful: Boolean? = null,
    @SerializedName("followed") var followed: Boolean? = null,
    @SerializedName("recent_commenters") var recentCommenters: ArrayList<RecentCommenters> = arrayListOf(),
    @SerializedName("recent_comments") var recentComments: ArrayList<RecentComments> = arrayListOf(),
) {
    fun stringToObject(string: String?): Discussions {
        return Gson().fromJson(string, Discussions::class.java)
    }
}

data class RecentCommenters(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("login") var login: String? = null,
    @SerializedName("avatar") var avatar: Avatar? = Avatar(),
    @SerializedName("commented_at") var commentedAt: String? = null
)

data class RecentComments(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("markdown") var markdown: String? = null,
    @SerializedName("user") var user: User? = User(),
    @SerializedName("comment_count") var commentCount: Int = 0,
    @SerializedName("helpful_count") var helpfulCount: Int = 0,
    @SerializedName("recent_comments") var recentComments: ArrayList<String> = arrayListOf(),
    @SerializedName("recent_commenters") var recentCommenters: ArrayList<RecentCommenters> = arrayListOf(),
    @SerializedName("marked_helpful") var markedHelpful: Boolean = false,
    @SerializedName("followed") var followed: Boolean = false,
    @SerializedName("posted_at") var postedAt: String? = null
){
    fun stringToObject(string: String?): RecentComments {
        return Gson().fromJson(string, RecentComments::class.java)
    }
}
