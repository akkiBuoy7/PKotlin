package plm.patientslikeme2.data.model.action

import com.google.gson.Gson
import plm.patientslikeme2.utils.extensions.isJSONValid

data class CommonModel(
    var success: Boolean,
    var message: Any,
    var data: Any
)

data class ErrorResponse(
    var success: Boolean? = null,
    var message: Any? = null,
    var data: Any? = null
) {
    fun stringToObject(string: String?): ErrorResponse {
        return if (isJSONValid(string)) {
            Gson().fromJson(string, ErrorResponse::class.java)
        } else {
            ErrorResponse(false, string, string)
        }
    }
}

data class SignupError(
    var success: Boolean? = null,
    var message: String? = null,
    var data: ErrorData? = null
) {

    fun stringToObject(string: String?): SignupError {
        return Gson().fromJson(string, SignupError::class.java)
    }
}

data class ErrorData(
    var error: ArrayList<String>? = null
)
