package plm.patientslikeme2.data.model.community.groups.discussions

import com.google.gson.annotations.SerializedName
import plm.patientslikeme2.data.model.Discussions

data class RecommendedTagsResponse(
    @field:SerializedName("data")
    val data: RecommendedTagsData? = null,
    @field:SerializedName("success")
    val success: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
)

data class RecommendedTagsData(
    @field:SerializedName("tags") val tags: ArrayList<TagsItem>? = null
)

data class TagsItem(
    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("text") val text: String? = null
) {
    override fun toString(): String {
        return text.toString()
    }
}

data class NewDiscussionRequest(
    @field:SerializedName("discussion") var discussion: CreateDiscussion? = null
)

data class CreateDiscussion(
    @field:SerializedName("featured") var featured: Boolean? = null,
    @field:SerializedName("title") var title: String? = null,
    @field:SerializedName("body") var body: String? = null,
    @field:SerializedName("tags") var tags: List<String?>? = null
)

data class CreateDiscussionResponse(
    @field:SerializedName("data") val data: CreateDiscussionData? = null,
    @field:SerializedName("success") val success: Boolean? = null,
    @field:SerializedName("message") val message: String? = null
)

data class CreateDiscussionData(
    @field:SerializedName("discussion") val discussion: Discussions? = null
)