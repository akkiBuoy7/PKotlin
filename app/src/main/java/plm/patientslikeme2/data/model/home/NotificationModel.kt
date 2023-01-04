package plm.patientslikeme2.data.model.home

import com.google.gson.annotations.SerializedName

data class NotificationModel(
    var id: Int,
    var seen: Boolean,
    var title: String,
    var body: String,
    var time: String,
    var context_type: String,
    var context_id: String,
    var context_url: String,
    var images: ArrayList<NotificationImages>
)

data class NotificationImages(
    var type: String,
    var value: String
)

data class NotificationResponse(
    var success: Boolean,
    var message: String,
    var data: NotificationData
)

data class NotificationData(
    var mobile_notifications: ArrayList<NotificationModel>
)

data class NotificationCountResponse(
    var success: Boolean,
    var message: String,
    var data: NotificationCount
)

data class NotificationCount(
    @SerializedName("count")
    var notificationCount: Int = 0
)