package plm.patientslikeme2.data.model.community.resource

data class ResourceModel(
    var id: Int,
    var headline: String,
    var resource_content: String,
    var resource_url: String,
    var resource_urls: String,
    var content_type: String,
    var detailed_author: String,
    var resource_date: String,
    var promo_image: String,
    var likes_count: String,
    var comments_count: String,
    var author_name: String,
    var followed: Boolean,
    var showDelete: Boolean
)

data class ResourceResponse(
    var success: Boolean,
    var message: String,
    var data: ResourceData
)

data class ResourceData(
    var resources: ArrayList<ResourceModel>
)