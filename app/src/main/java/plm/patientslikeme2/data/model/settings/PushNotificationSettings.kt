package plm.patientslikeme2.data.model.settings

data class PushNotificationSettings(
    var treatment_reminders: Boolean,
    var goal_reminders: Boolean,
    var private_messages: Boolean,
    var reactions_to_discussion: Boolean,
    var discussion_following: Boolean,
    var someone_mentioned: Boolean,
    var someone_following: Boolean,
    var discussion_started: Boolean
)

data class UserPreference(
    var mobile_push_notification: PushNotificationSettings
)

data class PushNotificationSettingsData(
    var user_preference: UserPreference
)

data class PushNotificationSettingsResponse(
    var success: Boolean,
    var message: String,
    var data: PushNotificationSettingsData
)

