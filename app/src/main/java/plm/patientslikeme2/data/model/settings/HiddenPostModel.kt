package plm.patientslikeme2.data.model.settings

import java.util.*

data class HiddenPost(
    var id: Int,
    var login: String,
    var first_name: String,
    var last_name: String,
    var avatar_url: String,
    var hidden: Boolean = true
) {

    fun userName(): String {
        return login.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

data class HiddenPostResponse(
    var success: Boolean,
    var data: HiddenPostData
)

data class HiddenPostData(
    var hidden_users: ArrayList<HiddenPost>,
    var last_page: Boolean
)

data class HiddenUnHiddenPostRequest(
    var user_id: Int
)