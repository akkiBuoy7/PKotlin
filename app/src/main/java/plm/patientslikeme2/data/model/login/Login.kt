package plm.patientslikeme2.data.model.login

import plm.patientslikeme2.utils.Constants

data class LoginModel(
    var success: Boolean? = null,
    var message: String? = null,
    var access_token: String? = null,
    var token_type: String? = null,
    var expires_in: Int? = null,
    var refresh_token: String? = null,
    var scope: String? = null,
    var signup_step: String? = null,
    var created_at: Int? = null,
    var user_id: Int? = null,
    var metric_guid: String? = null
)

data class LoginRequest(
    var email: String? = null,
    var username: String? = null,
    var password: String? = null,
    var grant_type: String? = Constants.PASSWORD,
    var app_type: String? = Constants.ANDROID,
    var client_id: String? = Constants.CLIENT_UID
)

data class ForgetPasswordResponse(
    var success: Boolean? = null,
    var message: String? = null
)