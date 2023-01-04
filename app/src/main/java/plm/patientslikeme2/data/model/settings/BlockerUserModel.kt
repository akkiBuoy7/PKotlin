package plm.patientslikeme2.data.model.settings

import java.util.*

data class BlockerUser(
    var id: Int,
    var login: String,
    var first_name: String,
    var last_name: String,
    var avatar_url: String,
    var blocked: Boolean = true
) {

    fun userName(): String {
        return login.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

data class BlockerUserResponse(
    var success: Boolean,
    var data: BlockerUserData
)

data class BlockerUserData(
    var blocked_users: ArrayList<BlockerUser>,
    var last_page: Boolean
)

data class BlockUnBlockUserRequest(
    var id: Int
)