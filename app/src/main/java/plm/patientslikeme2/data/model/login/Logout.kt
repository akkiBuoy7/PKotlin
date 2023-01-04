package plm.patientslikeme2.data.model.login

import plm.patientslikeme2.utils.Constants

data class LogoutRequest(
    var token: String? = null,
    var client_id: String = Constants.CLIENT_UID,
    var app_type: String = Constants.APP_TYPE
)