package plm.patientslikeme2.data.model.following

import plm.patientslikeme2.data.model.community.groups.members.Badges
import plm.patientslikeme2.data.model.community.groups.members.Conditions
import plm.patientslikeme2.data.model.profile.ModeratorAvatar

data class MemberModel(
    var id: Int = 0,
    var login: String = "",
    var avatar_url: String = "",
    var address: String = "",
    var living_with: ArrayList<String> = arrayListOf(),
    var conditions: ArrayList<Conditions> = arrayListOf(),
    var brief_bio: String = "",
    var sex: String? = null,
    var age: Int = 0,
    var badges: ArrayList<Badges> = arrayListOf(),
    var avatar: ModeratorAvatar,
    var followed: Boolean = false,
    var showFollowed: Boolean = false
)

data class FollowingMemberResponse(
    var success: Boolean,
    var message: String,
    var data: MemberData
)

data class MemberData(
    var members: ArrayList<MemberModel>
)