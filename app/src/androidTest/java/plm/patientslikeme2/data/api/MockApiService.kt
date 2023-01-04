package plm.patientslikeme2.data.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import okhttp3.MultipartBody
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.di.LocalJson
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.community.allgroups.AllGroupsResponse
import plm.patientslikeme2.data.model.community.allgroups.querygroup.QueryGroupResponse
import plm.patientslikeme2.data.model.community.events.EventDetailsResponse
import plm.patientslikeme2.data.model.community.events.EventResponse
import plm.patientslikeme2.data.model.community.groups.about.GroupAboutModel
import plm.patientslikeme2.data.model.community.groups.groupOverview.GroupOverviewResponse
import plm.patientslikeme2.data.model.community.groups.mygroups.MyGroupsResponse
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.SuggestedGroupResponse
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.joingroup.JoinGroupResponse
import plm.patientslikeme2.data.model.community.members.ExcludedUserRequest
import plm.patientslikeme2.data.model.community.members.MemberResponse
import plm.patientslikeme2.data.model.community.resource.ResourceDetailsResponse
import plm.patientslikeme2.data.model.community.resource.ResourceResponse
import plm.patientslikeme2.data.model.conditions.*
import plm.patientslikeme2.data.model.following.*
import plm.patientslikeme2.data.model.global.FeatureFlagModel
import plm.patientslikeme2.data.model.global.GlobalModel
import plm.patientslikeme2.data.model.global.MasterData
import plm.patientslikeme2.data.model.home.AnnouncementResponse
import plm.patientslikeme2.data.model.home.NotificationResponse
import plm.patientslikeme2.data.model.home.PromoCardResponse
import plm.patientslikeme2.data.model.home.RemovePromoCard
import plm.patientslikeme2.data.model.login.*
import plm.patientslikeme2.data.model.profile.*
import plm.patientslikeme2.data.model.settings.*
import plm.patientslikeme2.data.model.signup.*
import plm.patientslikeme2.data.model.treatments.*
import plm.patientslikeme2.utils.JsonProvider
import javax.inject.Inject

class MockApiService @Inject constructor() : ApiServices {

    var forceApiFailureCode: Int = 500
    var forceApiFailure: Boolean = true
    var forceInvalidResponse: Boolean = false
    override fun getGlobalState(): LiveData<Resource<GlobalModel>> {
        TODO("Not yet implemented")
    }

    override fun getFeatureFlag(): LiveData<Resource<FeatureFlagModel>> {
        TODO("Not yet implemented")
    }

    override fun postLogin(request: LoginRequest): LiveData<Resource<LoginModel>> {
        TODO("Not yet implemented")
    }

    override fun postUserDevices(request: UserDevices): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun postForgetPassword(request: ForgetPassword): LiveData<Resource<ForgetPasswordResponse>> {
        TODO("Not yet implemented")
    }

    override fun putForgetPassword(request: ResetPasswordRequest): LiveData<Resource<ResetPasswordResponse>> {
        TODO("Not yet implemented")
    }

    override fun postRefreshToken(request: RefreshTokenRequest): LiveData<Resource<LoginModel>> {
        TODO("Not yet implemented")
    }

    override fun postLogout(request: LogoutRequest): LiveData<Resource<Any>> {
        TODO("Not yet implemented")
    }

    override fun getCheckEmail(email: String): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun postUserRegistrations(request: SignupRequest): LiveData<Resource<SignupResponse>> {
        TODO("Not yet implemented")
    }

    override fun getConditionList(): LiveData<Resource<ConditionsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getSearchCondition(query: String): LiveData<Resource<ConditionsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getSearchPurpose(query: String): LiveData<Resource<PurposeSearchResponse>> {
        TODO("Not yet implemented")
    }

    override fun getTreatmentCategory(
        id: Int?,
        treatment_id: Int
    ): LiveData<Resource<MyFrequencyDosageResponse>> {
        TODO("Not yet implemented")
    }

    override fun getSuggestedSymptoms(condition_id: String): LiveData<Resource<SuggestedSymptomsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getRelatedStages(condition_id: String): LiveData<Resource<ConditionsStageResponse>> {
        TODO("Not yet implemented")
    }

    override fun getSearchSymptoms(query: String): LiveData<Resource<SuggestedSymptomsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getSearchSymptomsCOndition(query: String): LiveData<Resource<SearchSymptomResponse>> {
        TODO("Not yet implemented")
    }

    override fun getSearchTreatments(query: String): LiveData<Resource<SearchTreatmentResponse>> {
        TODO("Not yet implemented")
    }

    override fun getConditionStages(conditionId: Int?): LiveData<Resource<ConditionsStageResponse>> {
        TODO("Not yet implemented")
    }

    override fun postAddCondition(request: AddConditionsRequest): LiveData<Resource<AddConditionsResponse>> {
        TODO("Not yet implemented")
    }

    override fun postAddConditionRequest(request: AddConditionRequest): LiveData<Resource<AddConditionRequestResponse>> {
        TODO("Not yet implemented")
    }

    override fun postAddConditionRequest(request: AddTreatmentRequest): LiveData<Resource<AddTreatmentRequestResponse>> {
        TODO("Not yet implemented")
    }

    override fun putEditTreatment(
        request: AddTreatmentRequest,
        id: Int?
    ): LiveData<Resource<AddTreatmentRequestResponse>> {
        TODO("Not yet implemented")
    }

    override fun postAddSymptomRequest(request: AddSymptomRequest): LiveData<Resource<AddSymptomRequestResponse>> {
        TODO("Not yet implemented")
    }

    override fun putUpdateCondition(request: UpdateConditionsRequest): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun putEditCondition(request: AddConditionRequest): LiveData<Resource<AddConditionRequestResponse>> {
        TODO("Not yet implemented")
    }

    override fun deleteCondition(id: Int?): LiveData<Resource<DeleteConditionsResponse>> {
        TODO("Not yet implemented")
    }

    override fun deleteTreatment(id: Int?): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun deleteSymptom(id: Int?): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun getSearchZipCode(query: String): LiveData<Resource<ZipCodeResponse>> {
        TODO("Not yet implemented")
    }

    override fun postUserAvatars(avatar: MultipartBody.Part): LiveData<Resource<AvatarResponse>> {
        TODO("Not yet implemented")
    }

    override fun postUpdateUserInfo(request: UpdateUserInfo): LiveData<Resource<SignupResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMemberList(
        email: String,
        myFollowings: Boolean,
        myConditions: Boolean,
        page: String,
        per_page: String
    ): LiveData<Resource<MemberResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMyGroupsList(): LiveData<Resource<MyGroupsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getSuggestedGroupsList(): LiveData<Resource<SuggestedGroupResponse>> {
        TODO("Not yet implemented")
    }

    override fun getAllGroupsList(): LiveData<Resource<AllGroupsResponse>> {
        TODO("Not yet implemented")
    }

    override fun queryGroup(search: String): LiveData<Resource<QueryGroupResponse>> {
        TODO("Not yet implemented")
    }

    override fun joinGroup(groupId: Int?): LiveData<Resource<JoinGroupResponse>> {
        TODO("Not yet implemented")
    }

    override fun leaveGroup(groupId: Int?): LiveData<Resource<JoinGroupResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMyConditions(): LiveData<Resource<MyConditionsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getConditionDetails(conditionId: Int): LiveData<Resource<ConditionsDetailsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMyTreatments(): LiveData<Resource<MyTreatmentsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getEditTreatments(id: Int): LiveData<Resource<GetTreatmentResponse>> {
        TODO("Not yet implemented")
    }

    override fun updateEmail(request: EmailUpdateRequest): LiveData<Resource<EmailUpdateResponse>> {
        TODO("Not yet implemented")
    }

    override fun verifyEmail(): LiveData<Resource<EmailVerifyResponse>> {
        TODO("Not yet implemented")
    }

    override fun updatePassword(request: PasswordUpdateRequest): LiveData<Resource<PasswordUpdateResponse>> {
        TODO("Not yet implemented")
    }

    override fun getUserPreferences(): LiveData<Resource<PrivacySettingsResponse>> {
        TODO("Not yet implemented")
    }

    override fun updateUserPreferences(request: PrivacySettingsRequest): LiveData<Resource<PrivacySettingsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getUserDetails(): LiveData<Resource<UserDetailsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getGroupOverview(id: String): LiveData<Resource<GroupOverviewResponse>> {
        return MediatorLiveData<Resource<GroupOverviewResponse>>().apply {
            value = when {
                forceApiFailure -> {
                    (Resource.error(("Api failed"), forceApiFailureCode))
                }
                forceInvalidResponse -> {
                    (Resource(status = Status.SUCCESS, data = null))
                }
                else -> {
                    Resource.success(
                        JsonProvider.objectFromJsonFileWithType(LocalJson.GROUP_OVERVIEW),
                        200
                    )
                }
            }
        }
    }

    override fun getAboutGroup(id: String): LiveData<Resource<GroupAboutModel>> {
        return MediatorLiveData<Resource<GroupAboutModel>>().apply {
            value = when {
                forceApiFailure -> {
                    (Resource.error(("Api failed"), forceApiFailureCode))
                }
                forceInvalidResponse -> {
                    (Resource(status = Status.SUCCESS, data = null))
                }
                else -> {
                    Resource.success(
                        JsonProvider.objectFromJsonFileWithType(LocalJson.GROUP_ABOUT),
                        200
                    )
                }
            }
        }
    }

    override fun getBlockedPrivateMessages(
        page: Int?,
        per_page: Int?
    ): LiveData<Resource<BlockerUserResponse>> {
        TODO("Not yet implemented")
    }

    override fun postUnBlockedPrivateMessages(request: BlockUnBlockUserRequest): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun postBlockedPrivateMessages(request: BlockUnBlockUserRequest): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun getHiddenPosts(
        page: Int?,
        per_page: Int?
    ): LiveData<Resource<HiddenPostResponse>> {
        TODO("Not yet implemented")
    }

    override fun postUnHiddenPosts(request: HiddenUnHiddenPostRequest): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun postHiddenPosts(request: HiddenUnHiddenPostRequest): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun getPushNotificationSettings(): LiveData<Resource<PushNotificationSettingsResponse>> {
        TODO("Not yet implemented")
    }

    override fun putPushNotificationSettings(
        key: String,
        status: String
    ): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun getNotification(
        page: Int?,
        per_page: Int?
    ): LiveData<Resource<NotificationResponse>> {
        TODO("Not yet implemented")
    }

    override fun putUpdateSeenNotification(): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun putDismissNotification(id: Int?): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun getMemberConditions(id: String): LiveData<Resource<MemberConditionsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMyConditionsList(): LiveData<Resource<MemberConditionsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMemberFollowings(id: String): LiveData<Resource<MemberFollowingsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMyFollowings(): LiveData<Resource<MemberFollowingsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMemberProfile(id: String): LiveData<Resource<MyProfileResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMyProfile(): LiveData<Resource<MyProfileResponse>> {
        TODO("Not yet implemented")
    }

    override fun memberFollow(id: String): LiveData<Resource<FollowResponse>> {
        TODO("Not yet implemented")
    }

    override fun memberUnFollow(id: String): LiveData<Resource<FollowResponse>> {
        TODO("Not yet implemented")
    }

    override fun getStaticMasterData(): LiveData<Resource<MasterData>> {
        TODO("Not yet implemented")
    }

    override fun updatePersonalInfo(request: UpdatePersonalInfoRequest): LiveData<Resource<UserDetailsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getMembersLikeMe(): LiveData<Resource<MemberResponse>> {
        TODO("Not yet implemented")
    }

    override fun followMember(id: String): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun unFollowMember(id: String): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun excludedUser(request: ExcludedUserRequest): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun getGroupNewMembers(
        id: String,
        page: String,
        per_page: String
    ): LiveData<Resource<plm.patientslikeme2.data.model.community.groups.members.MemberResponse>> {
        TODO("Not yet implemented")
    }

    override fun getGroupMembers(
        id: String,
        email: String,
        myFollowings: Boolean,
        myConditions: Boolean,
        page: String,
        per_page: String
    ): LiveData<Resource<plm.patientslikeme2.data.model.community.groups.members.MemberResponse>> {
        TODO("Not yet implemented")
    }

    override fun searchUsers(
        search: String,
        fields: String,
        page: String,
        per_page: String
    ): LiveData<Resource<MemberResponse>> {
        TODO("Not yet implemented")
    }

    override fun searchMembers(
        id: String,
        search: String,
        fields: String,
        page: String,
        per_page: String
    ): LiveData<Resource<plm.patientslikeme2.data.model.community.groups.members.MemberResponse>> {
        TODO("Not yet implemented")
    }

    override fun removeUserAvatars(): LiveData<Resource<AvatarResponse>> {
        TODO("Not yet implemented")
    }

    override fun getFollowingDiscussion(
        page: Int?,
        per_page: Int?
    ): LiveData<Resource<FollowingDiscussionResponse>> {
        TODO("Not yet implemented")
    }

    override fun getFollowingMembers(
        page: Int?,
        per_page: Int?
    ): LiveData<Resource<FollowingMemberResponse>> {
        TODO("Not yet implemented")
    }

    override fun getFollowingTags(
        page: Int?,
        per_page: Int?
    ): LiveData<Resource<FollowingTagResponse>> {
        TODO("Not yet implemented")
    }

    override fun getTagDetailsList(
        tags: String?,
        page: Int?,
        per_page: Int?
    ): LiveData<Resource<TagDetailsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getFollowTagDetailsDiscussion(id: String?): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun postUnFollowDiscussion(id: String?): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun postUnFollowTag(id: String?): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun postUnFollowResource(id: String?): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun postFollowResource(id: String?): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun getFollowingResources(
        page: Int?,
        per_page: Int?
    ): LiveData<Resource<FollowingResourceResponse>> {
        TODO("Not yet implemented")
    }

    override fun getCommunityEvents(): LiveData<Resource<EventResponse>> {
        TODO("Not yet implemented")
    }

    override fun getEventOverview(id: String?): LiveData<Resource<EventDetailsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getGroupEvents(id: String?): LiveData<Resource<EventResponse>> {
        TODO("Not yet implemented")
    }

    override fun getGroupResources(id: String?): LiveData<Resource<ResourceResponse>> {
        TODO("Not yet implemented")
    }

    override fun getResourceOverview(id: String?): LiveData<Resource<ResourceDetailsResponse>> {
        TODO("Not yet implemented")
    }

    override fun getGroupFilterResource(filter: String): LiveData<Resource<ResourceResponse>> {
        TODO("Not yet implemented")
    }

    override fun getHomeAnnouncements(): LiveData<Resource<AnnouncementResponse>> {
        TODO("Not yet implemented")
    }

    override fun postDismissAnnouncements(): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

    override fun getHomePromoCardList(): LiveData<Resource<PromoCardResponse>> {
        TODO("Not yet implemented")
    }

    override fun postExcludePromoCard(request: RemovePromoCard): LiveData<Resource<CommonModel>> {
        TODO("Not yet implemented")
    }

}