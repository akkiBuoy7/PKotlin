package plm.patientslikeme2.data.model.profile

data class MyProfile(
    var success: Boolean? = null,
    var message: String? = null,
)

data class MemberConditionsResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: ConditionsData? = ConditionsData(),
)

data class MemberFollowingsResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: DataModel? = DataModel(),
)

data class Symptoms(
    var id: Int? = null,
    var name: String? = null,
    var severity: String? = null
)

data class Treatments(
    var id: Int? = null,
    var name: String? = null,
    var effectiveness: String? = null
)

data class ConditionsData(
    var conditions: ArrayList<Conditions>? = null
)

data class Conditions(
    var id: Int? = null,
    var name: String? = null,
    var symptoms: ArrayList<Symptoms>? = null,
    var treatments: ArrayList<Treatments>? = null
)

data class UserFollowings(
    var id: Int? = null,
    var login: String? = null,
    var name: String? = null,
    var profile_pic: String? = null
)

data class DataModel(
    var user_followings: ArrayList<UserFollowings>? = null
)

data class AboutConditions(
    var id: Int? = null,
    var name: String? = null,
    var stage: String? = null,
    var diagnosed: String? = null
)

data class QuickStats(
    var member_since: String? = null,
    var followers_count: Int? = null,
    var followings_count: Int? = null,
    var helpful_marks_received: Int? = null,
    var posted_comments_count: Int? = null
)

data class About(
    var story_title: String? = null,
    var story_content: String? = null,
    var conditions: ArrayList<AboutConditions>? = null,
    var community_badges: ArrayList<Badges>? = null,
    var quick_stats: QuickStats? = QuickStats(),
    var interests: Interests? = Interests()
)

data class Badges(
    var badge: String? = null,
    var name: String? = null
)

data class Interests(
    var primary: String? = null,
    var secondary: ArrayList<String>? = null
)

data class MyProfileResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: UserProfileData? = UserProfileData()
)

data class UserProfileData(
    var user_profile: UserProfile? = UserProfile()
)

data class UserProfile(
    var id: Int? = null,
    var login: String? = null,
    var name: String? = null,
    var brief_bio: String? = null,
    var living_with: ArrayList<String>? = null,
    var address: String? = null,
    var posted_comments_count: Int? = null,
    var posted_likes_count: Int? = null,
    var followed: Boolean? = null,
    var about: About? = About(),
    var avatar_url: String? = null,
)

data class FollowResponse(
    var success: Boolean? = null,
    var message: String? = null,
)

data class UpdatePersonalInfoRequest(
    var user: UserInfo? = UserInfo()
)

data class UserProfileAttrs(
    var profile_color: String? = null
)

data class BioAttrs(
    var brief_bio: String? = null,
    var content: String? = null
)

data class MilitaryServiceAttributes(
    var status: String? = null,
    var branch_id: String? = null,
    var rank: String? = null,
    var job: String? = null,
    var deployed_to_combat_zone: String? = null,
    var va_healthcare_eligibility: String? = null,
    var service_start_date_hash: ServiceStartDateHash? = ServiceStartDateHash(),
    var service_end_date_hash: ServiceEndDateHash? = ServiceEndDateHash()
)

data class ServiceStartDateHash(
    var year: Int? = null,
    var month: Int? = null
)

data class ServiceEndDateHash(
    var year: Int? = null,
    var month: Int? = null
)

data class UserInfo(
    var sex: String? = null,
    var user_profile_attrs: UserProfileAttrs? = UserProfileAttrs(),
    var bio_attrs: BioAttrs? = BioAttrs(),
    var military_service_attributes: MilitaryServiceAttributes? = MilitaryServiceAttributes(),
    var birth_date: String? = null,
    var ethnicity_id: String? = null,
    var education_level_id: String? = null,
    var primary_interest_id: String? = null,
    var health_insurance_type_id: String? = null,
    var canned_genders: Array<String>? = arrayOf(),
    var city: String? = null,
    var postcode: String? = null,
    var country: String? = null,
    var state: String? = null,
    var secondary_interest_ids: Array<Int>? = arrayOf()
)