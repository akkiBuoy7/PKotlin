package plm.patientslikeme2.data.model.following

import plm.patientslikeme2.data.model.community.resource.ResourceModel

data class FollowingResourceResponse(
    var success: Boolean,
    var message: String,
    var data: FollowingResourceData
)

data class FollowingResourceData(
    var resources: ArrayList<ResourceModel>
)