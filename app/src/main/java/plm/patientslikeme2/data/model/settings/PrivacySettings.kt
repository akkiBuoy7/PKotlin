package plm.patientslikeme2.data.model.settings

import com.google.gson.annotations.SerializedName

data class EmailUpdateRequest(
    var password: String? = null,
    var new_email: String? = null,
    var confirm_email: String? = null,
)

data class EmailUpdateResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()
)

data class Data(
    @SerializedName("error") var error: ArrayList<String> = arrayListOf()
)

data class EmailVerifyResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: VerifyData? = VerifyData()
)

data class VerifyData(
    @SerializedName("email_verified") var email_verified: Boolean? = null
)

data class PasswordUpdateRequest(
    var current_password: String? = null,
    var password: String? = null,
    var confirm_password: String? = null,
)

data class PasswordUpdateResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
)

data class PrivacySettingsRequest(
    @SerializedName("privacy_settings") var privacySettings: PrivacySettingsModel? = PrivacySettingsModel()
)

data class PrivacySettingsModel(
    @SerializedName("visible_to") var visibleTo: String? = null,
    @SerializedName("request_from_account_privacy") var requestFromAccountPrivacy: Boolean? = null,
    @SerializedName("own_controlled") var ownControlled: ArrayList<OwnControlled> = arrayListOf()
)

data class PrivacySettingsResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: DataModel? = DataModel(),
)

data class PrivacySettings(
    @SerializedName("visible_to") var visibleTo: String? = null,
    @SerializedName("own_controlled") var ownControlled: ArrayList<OwnControlled> = arrayListOf()
)

data class OwnControlled(
    @SerializedName("controllable_id") var controllableId: String? = null,
    @SerializedName("controllable_name") var controllableName: String? = null,
    @SerializedName("controllable_type") var controllableType: String? = null,
    @SerializedName("permission_level") var permissionLevel: String? = null
)

data class DefaultPrivacySettings(
    @SerializedName("visible_to") var visibleTo: String? = null,
    @SerializedName("own_controlled") var ownControlled: ArrayList<OwnControlled> = arrayListOf()
)

data class DataModel(
    @SerializedName("privacy_settings") var privacySettings: PrivacySettings? = PrivacySettings(),
    @SerializedName("default_privacy_settings") var defaultPrivacySettings: DefaultPrivacySettings? = DefaultPrivacySettings(),
    @SerializedName("updated_at") var updatedAt: String? = null
)