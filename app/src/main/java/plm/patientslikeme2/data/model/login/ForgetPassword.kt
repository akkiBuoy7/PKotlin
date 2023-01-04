package plm.patientslikeme2.data.model.login

import com.google.gson.Gson
import plm.patientslikeme2.utils.extensions.isJSONValid

data class ForgetPassword(
    var email: String? = null
)

data class ResetPasswordRequest(
    var reset_password_token: String? = null,
    var password: String? = null,
    var password_confirmation: String? = null
)

data class ResetPasswordResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: ResetPasswordData? = null
)

data class ResetPasswordData(
    var error: String? = null
)

data class ResetPasswordError(
    var success: Boolean? = null,
    var message: String? = null,
    var data: ErrorData? = null
) {

    fun stringToObject(string: String?): ResetPasswordError {
        return if (isJSONValid(string)) {
            Gson().fromJson(string, ResetPasswordError::class.java)
        } else {
            ResetPasswordError(false, string, ErrorData(string))
        }
    }
}

data class ErrorData(
    var error: String? = null
)

