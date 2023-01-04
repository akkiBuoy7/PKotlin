package plm.patientslikeme2.data.model.community.resource

import plm.patientslikeme2.data.model.community.members.Badges

data class ResourceDetailsModel(
    var id: Int,
    var resource_content: String,
    var resource_url: String,
    var author_type: String,
    var author_source: String,
    var headline: String,
    var detailed_author: String,
    var resource_date: String,
    var promo_image: String,
    var likes_count: String,
    var comments_count: String,
    var author_name: String,
    var user: ResourceUser,
    var followed: Boolean
)

data class ResourceUser(
    var id: Int,
    var name: String,
    var login: String,
    var avatar: String,
    var badges: ArrayList<Badges> = arrayListOf()
)

data class ResourceDetailsResponse(
    var success: Boolean,
    var message: String,
    var data: ResourceDetailsData
)

data class ResourceDetailsData(
    var resource: ResourceDetailsModel
)