package plm.patientslikeme2.data.model.login

import plm.patientslikeme2.utils.Constants

data class RefreshTokenRequest(
    var refresh_token: String? = null,
    var grant_type: String? = Constants.REFRESH_TOKEN,
    var client_id: String? = Constants.CLIENT_UID,
    var app_type: String? = Constants.ANDROID
)
