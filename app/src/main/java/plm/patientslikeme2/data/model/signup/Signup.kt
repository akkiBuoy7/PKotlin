package plm.patientslikeme2.data.model.signup

import com.google.gson.annotations.SerializedName
import plm.patientslikeme2.data.model.login.LoginModel
import plm.patientslikeme2.utils.Constants

data class SignupRequest(
    var email: String? = null,
    var username: String? = null,
    var password: String? = null,
    var role_id: Int = 1,
    var dob: String? = null,
    var verified: Boolean = true,
    var caregiver: Boolean? = null,
    var terms_of_service: Boolean? = null,
    var grant_type: String = Constants.PASSWORD,
    var app_type: String = Constants.ANDROID,
    var client_id: String = Constants.CLIENT_UID
)

data class CheckMailResponse(
    var success: Boolean? = null
)

data class SignupResponse(
    var success: Boolean? = null,
    var message: Any? = null,
    var data: SignupData? = null
)

data class SignupData(
    var user: User? = null,
    var data: LoginModel? = null
)

data class UpdateUserInfo(
    var user: UserUpdate? = null
)

data class ZipCodeResponse(
    var success: Boolean? = null,
    var data: ArrayList<ZipCode>? = null
)

data class ZipCode(
    var code: String? = null,
    var city: String? = null,
    var state: String? = null
)

data class UserUpdate(
    var sex: String? = null,
    var postcode: String? = null,
    var user_profile_attrs: UserProfileAttrs? = null,
    var bio_attrs: UserBioAttrs? = null
)

data class UserProfileAttrs(
    var profile_color: String? = null
)

data class UserBioAttrs(
    var brief_bio: String? = null
)

data class AvatarResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: AvatarData? = AvatarData()
)

data class AvatarData(
    @SerializedName("data") var image: String? = null
)