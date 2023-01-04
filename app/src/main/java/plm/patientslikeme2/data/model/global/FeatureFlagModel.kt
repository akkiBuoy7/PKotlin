package plm.patientslikeme2.data.model.global

data class FeatureFlagModel(
    val android_sample_test: Boolean = true,
    val android_login_test: Boolean = true,
    val android_daily_me_persistent: Boolean = true,
    val android_app_search: Boolean = true,
    val android_development: Boolean = true,
    val android_login: Boolean = true,
    val android_menu: Boolean = true,
    val android_community_groups: Boolean = true,
    val android_signupandonboarding: Boolean = true,
    val android_email_password: Boolean = true,
    val android_privacy_settings: Boolean = true,
    val android_blocked_members: Boolean = true,
    val android_my_conditions: Boolean = true,
    val android_mobile_promo: Boolean = true,
    val android_events: Boolean = true,
    val android_members: Boolean = true,
    val android_edit_profile: Boolean = true,
    val android_notification_settings: Boolean = true,
    val android_notifications: Boolean = true,
    val android_private_messaging: Boolean = true,
    val android_discussions: Boolean = true,
    val android_followings: Boolean = true,
    val android_home_page: Boolean = true,
    val android_resources: Boolean = true,
    val android_announcements: Boolean = true
)

data class FeatureFlagResponse(
    var success: Boolean,
    var data: FeatureFlagData
)

data class FeatureFlagData(
    var feature_flags: FeatureFlagModel
)