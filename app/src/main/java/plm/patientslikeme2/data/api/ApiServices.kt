package plm.patientslikeme2.data.api


import androidx.lifecycle.LiveData
import okhttp3.MultipartBody
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.model.ImageUpload
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.community.allgroups.AllGroupsResponse
import plm.patientslikeme2.data.model.community.allgroups.querygroup.QueryGroupResponse
import plm.patientslikeme2.data.model.community.events.EventDetailsResponse
import plm.patientslikeme2.data.model.community.events.EventResponse
import plm.patientslikeme2.data.model.community.groups.about.GroupAboutModel
import plm.patientslikeme2.data.model.community.groups.discussions.*
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
import plm.patientslikeme2.data.model.global.FeatureFlagResponse
import plm.patientslikeme2.data.model.global.GlobalModel
import plm.patientslikeme2.data.model.global.MasterData
import plm.patientslikeme2.data.model.home.*
import plm.patientslikeme2.data.model.login.*
import plm.patientslikeme2.data.model.messages.*
import plm.patientslikeme2.data.model.profile.*
import plm.patientslikeme2.data.model.settings.*
import plm.patientslikeme2.data.model.signup.*
import plm.patientslikeme2.data.model.treatments.*
import plm.patientslikeme2.utils.Constants
import retrofit2.http.*

interface ApiServices {

    @POST("api/v3/global_state")
    fun getGlobalState(): LiveData<Resource<GlobalModel>>

    @GET("api/mobile/v1/feature_flags?os=android")
    fun getFeatureFlag(): LiveData<Resource<FeatureFlagResponse>>

    @POST("api/mobile/v1/tokens/login")
    fun postLogin(@Body request: LoginRequest): LiveData<Resource<LoginModel>>

    @POST("api/mobile/v1/tokens/logout")
    fun postUserLogout(): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/mobile_user_devices")
    fun postUserDevices(@Body request: UserDevices): LiveData<Resource<CommonModel>>

    @POST("/api/mobile/v1/user_reset_passwords")
    fun postForgetPassword(@Body request: ForgetPassword): LiveData<Resource<ForgetPasswordResponse>>

    @PUT("/api/mobile/v1/user_reset_passwords")
    fun putForgetPassword(@Body request: ResetPasswordRequest): LiveData<Resource<ResetPasswordResponse>>

    @POST("api/mobile/v1/tokens/refresh")
    fun postRefreshToken(@Body request: RefreshTokenRequest): LiveData<Resource<LoginModel>>

    @POST("api/mobile/v1/tokens/logout")
    fun postLogout(@Body request: LogoutRequest): LiveData<Resource<Any>>

    @GET("api/mobile/v1/users/check_email")
    fun getCheckEmail(
        @Query(value = "email", encoded = true) email: String,
    ): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/user_registrations")
    fun postUserRegistrations(@Body request: SignupRequest): LiveData<Resource<SignupResponse>>

    @GET("api/mobile/v1/condition_infos")
    fun getConditionList(): LiveData<Resource<ConditionsResponse>>

    @GET("api/mobile/v1/medical_entities/condition_search")
    fun getSearchCondition(@Query(value = "query") query: String): LiveData<Resource<ConditionsResponse>>

    @GET("api/mobile/v1/medical_entities/purpose_search")
    fun getSearchPurpose(@Query(value = "query") query: String): LiveData<Resource<PurposeSearchResponse>>

    @GET("/api/mobile/v1/treatment_categories/{category_id}")
    fun getTreatmentCategory(
        @Path("category_id") id: Int?, @Query("treatment_id") treatment_id: Int
    ): LiveData<Resource<MyFrequencyDosageResponse>>

    @GET("/api/mobile/v1/symptoms")
    fun getSuggestedSymptoms(@Query(value = "condition_id") condition_id: String): LiveData<Resource<SuggestedSymptomsResponse>>

    @GET("/api/mobile/v1/condition_infos/condition_stages")
    fun getRelatedStages(@Query(value = "condition_id") condition_id: String): LiveData<Resource<ConditionsStageResponse>>

    @GET("api/mobile/v1/medical_entities/condition_search")
    fun getSearchSymptoms(@Query(value = "query") query: String): LiveData<Resource<SuggestedSymptomsResponse>>

    @GET("api/mobile/v1/medical_entities/symptom_search")
    fun getSearchSymptomsCOndition(@Query(value = "query") query: String): LiveData<Resource<SearchSymptomResponse>>

    @GET("api/mobile/v1/medical_entities/treatment_search")
    fun getSearchTreatments(@Query(value = "query") query: String): LiveData<Resource<SearchTreatmentResponse>>

    @GET("api/mobile/v1/condition_infos/condition_stages")
    fun getConditionStages(@Query("condition_id") conditionId: Int?): LiveData<Resource<ConditionsStageResponse>>

    @POST("api/mobile/v1/condition_infos")
    fun postAddCondition(@Body request: AddConditionsRequest): LiveData<Resource<AddConditionsResponse>>

    @POST("api/mobile/v1/condition_infos")
    fun postAddConditionRequest(@Body request: AddConditionRequest): LiveData<Resource<AddConditionRequestResponse>>

    @POST("api/mobile/v1/treatment_histories")
    fun postAddConditionRequest(@Body request: AddTreatmentRequest): LiveData<Resource<AddTreatmentRequestResponse>>

    @PUT("api/mobile/v1/treatment_histories/{id}")
    fun putEditTreatment(
        @Body request: AddTreatmentRequest, @Path(value = "id") id: Int?
    ): LiveData<Resource<AddTreatmentRequestResponse>>

    @POST("api/mobile/v1/symptom_infos")
    fun postAddSymptomRequest(@Body request: AddSymptomRequest): LiveData<Resource<AddSymptomRequestResponse>>

    @PUT("api/mobile/v1/condition_infos/update_privacy")
    fun putUpdateCondition(@Body request: UpdateConditionsRequest): LiveData<Resource<CommonModel>>

    @PUT("api/mobile/v1/condition_infos")
    fun putEditCondition(@Body request: AddConditionRequest): LiveData<Resource<AddConditionRequestResponse>>

    @DELETE("api/mobile/v1/condition_infos/{id}")
    fun deleteCondition(@Path(value = "id") id: Int?): LiveData<Resource<DeleteConditionsResponse>>

    @DELETE("api/mobile/v1/treatment_histories/{id}")
    fun deleteTreatment(@Path(value = "id") id: Int?): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/symptom_infos/{id}")
    fun deleteSymptom(@Path(value = "id") id: Int?): LiveData<Resource<CommonModel>>

    @GET("/api/mobile/v1/zipcodes")
    fun getSearchZipCode(@Query(value = "query") query: String): LiveData<Resource<ZipCodeResponse>>

    @Multipart
    @POST("api/mobile/v1/user_avatars")
    fun postUserAvatars(@Part avatar: MultipartBody.Part): LiveData<Resource<AvatarResponse>>

    @POST("api/mobile/v1/users/update_info")
    fun postUpdateUserInfo(@Body request: UpdateUserInfo): LiveData<Resource<SignupResponse>>

    @GET("api/mobile/v1/members")
    fun getMemberList(
        @Query(value = "sort_by", encoded = true) email: String = Constants.NEWEST,
        @Query(value = "filter_params[my_followings]", encoded = true) myFollowings: Boolean = true,
        @Query(value = "filter_params[my_conditions]", encoded = true) myConditions: Boolean = true,
        @Query(value = "page", encoded = true) page: String,
        @Query(value = "per_page", encoded = true) per_page: String = "8",
    ): LiveData<Resource<MemberResponse>>

    @GET("api/mobile/v1/my_groups")
    fun getMyGroupsList(): LiveData<Resource<MyGroupsResponse>>

    @GET("api/mobile/v1/suggested_groups")
    fun getSuggestedGroupsList(): LiveData<Resource<SuggestedGroupResponse>>

    @GET("api/mobile/v1/groups")
    fun getAllGroupsList(): LiveData<Resource<AllGroupsResponse>>

    @GET("api/mobile/v1/groups")
    fun queryGroup(@Query("search") search: String): LiveData<Resource<QueryGroupResponse>>

    @POST("/api/mobile/v1/groups/{id}/join")
    fun joinGroup(@Path("id") groupId: Int?): LiveData<Resource<JoinGroupResponse>>

    @POST("/api/mobile/v1/groups/{id}/undo")
    fun leaveGroup(@Path("id") groupId: Int?): LiveData<Resource<JoinGroupResponse>>

    @GET("api/mobile/v1/my_conditions")
    fun getMyConditions(): LiveData<Resource<MyConditionsResponse>>

    @GET("/api/mobile/v1/my_conditions/{id}/details")
    fun getConditionDetails(@Path("id") conditionId: Int): LiveData<Resource<ConditionsDetailsResponse>>

    @GET("/api/mobile/v1/treatment_histories/list_treatment_by_condition")
    fun getMyTreatments(): LiveData<Resource<MyTreatmentsResponse>>

    @GET("/api/mobile/v1/treatment_histories/{id}")
    fun getEditTreatments(@Path("id") id: Int): LiveData<Resource<GetTreatmentResponse>>

    @PUT("api/mobile/v1/users/email_update")
    fun updateEmail(@Body request: EmailUpdateRequest): LiveData<Resource<EmailUpdateResponse>>

    @GET("api/mobile/v1/users/email_verify")
    fun verifyEmail(): LiveData<Resource<EmailVerifyResponse>>

    @PUT("api/mobile/v1/users/password_update")
    fun updatePassword(@Body request: PasswordUpdateRequest): LiveData<Resource<PasswordUpdateResponse>>

    @GET("api/mobile/v1/user_preferences")
    fun getUserPreferences(): LiveData<Resource<PrivacySettingsResponse>>

    @PUT("api/mobile/v1/user_preferences/privacy_settings")
    fun updateUserPreferences(@Body request: PrivacySettingsRequest): LiveData<Resource<PrivacySettingsResponse>>

    @GET("api/mobile/v1/users/details")
    fun getUserDetails(): LiveData<Resource<UserDetailsResponse>>

    @GET("api/mobile/v1/groups/{id}/overview")
    fun getGroupOverview(@Path("id") id: String): LiveData<Resource<GroupOverviewResponse>>

    @GET("api/mobile/v1/groups/{id}/about_group")
    fun getAboutGroup(@Path("id") id: String): LiveData<Resource<GroupAboutModel>>

    @GET("api/mobile/v1/users/blocked_users")
    fun getBlockedPrivateMessages(
        @Query("page") page: Int?, @Query("per_page") per_page: Int?
    ): LiveData<Resource<BlockerUserResponse>>

    @POST("api/mobile/v1/users/unblock_user")
    fun postUnBlockedPrivateMessages(@Body request: BlockUnBlockUserRequest): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/users/block_user")
    fun postBlockedPrivateMessages(@Body request: BlockUnBlockUserRequest): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/hide_lists/users")
    fun getHiddenPosts(
        @Query("page") page: Int?, @Query("per_page") per_page: Int?
    ): LiveData<Resource<HiddenPostResponse>>

    @POST("api/mobile/v1/hide_lists/remove")
    fun postUnHiddenPosts(@Body request: HiddenUnHiddenPostRequest): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/hide_lists/add")
    fun postHiddenPosts(@Body request: HiddenUnHiddenPostRequest): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/user_preferences/notification_settings")
    fun getPushNotificationSettings(): LiveData<Resource<PushNotificationSettingsResponse>>

    @PUT("api/mobile/v1/user_preferences/push_notification_settings/{key}/{status}")
    fun putPushNotificationSettings(
        @Path("key") key: String, @Path("status") status: String
    ): LiveData<Resource<CommonModel>>

    @GET("/api/mobile/v1/in_app_notifications")
    fun getNotification(
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?
    ): LiveData<Resource<NotificationResponse>>

    @PUT("api/mobile/v1/in_app_notifications/{id}/mark_as_read")
    fun putMarkAsReadNotification(@Path("id") id: Int?): LiveData<Resource<CommonModel>>

    @PUT("/api/mobile/v1/in_app_notifications/{id}/discard")
    fun putDismissNotification(
        @Path("id") id: Int?
    ): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/members/{id}/conditions")
    fun getMemberConditions(@Path("id") id: String): LiveData<Resource<MemberConditionsResponse>>

    @GET("api/mobile/v1/members/conditions")
    fun getMyConditionsList(): LiveData<Resource<MemberConditionsResponse>>

    @GET("api/mobile/v1/members/{id}/followings")
    fun getMemberFollowings(@Path("id") id: String): LiveData<Resource<MemberFollowingsResponse>>

    @GET("api/mobile/v1/members/followings")
    fun getMyFollowings(): LiveData<Resource<MemberFollowingsResponse>>

    @GET("api/mobile/v1/members/{id}/profile")
    fun getMemberProfile(@Path("id") id: String): LiveData<Resource<MyProfileResponse>>

    @GET("api/mobile/v1/members/my_profile")
    fun getMyProfile(): LiveData<Resource<MyProfileResponse>>

    @POST("api/mobile/v1/members/{id}/follow")
    fun memberFollow(@Path("id") id: String): LiveData<Resource<FollowResponse>>

    @DELETE("api/mobile/v1/members/{id}/unfollow")
    fun memberUnFollow(@Path("id") id: String): LiveData<Resource<FollowResponse>>

    @GET("api/mobile/v1/static_master_data")
    fun getStaticMasterData(): LiveData<Resource<MasterData>>

    @POST("api/mobile/v1/users/update_info")
    fun updatePersonalInfo(@Body request: UpdatePersonalInfoRequest): LiveData<Resource<UserDetailsResponse>>

    @GET("api/mobile/v1/members/like_me")
    fun getMembersLikeMe(): LiveData<Resource<MemberResponse>>

    @POST("api/mobile/v1/members/{id}/follow")
    fun followMember(@Path("id") id: String): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/members/{id}/unfollow")
    fun unFollowMember(@Path("id") id: String): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/user_excluded_buckets")
    fun excludedUser(@Body request: ExcludedUserRequest): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/groups/{id}/members/new_members")
    fun getGroupNewMembers(
        @Path("id") id: String,
        @Query(value = "page", encoded = true) page: String = "1",
        @Query(value = "per_page", encoded = true) per_page: String = "6",
    ): LiveData<Resource<plm.patientslikeme2.data.model.community.groups.members.MemberResponse>>

    @GET("api/mobile/v1/groups/{id}/members")
    fun getGroupMembers(
        @Path("id") id: String,
        @Query(value = "sort_by", encoded = true) email: String = Constants.NEWEST,
        @Query(value = "filter_params[my_followings]", encoded = true) myFollowings: Boolean = true,
        @Query(value = "filter_params[my_conditions]", encoded = true) myConditions: Boolean = true,
        @Query(value = "page", encoded = true) page: String,
        @Query(value = "per_page", encoded = true) per_page: String = "6",
    ): LiveData<Resource<plm.patientslikeme2.data.model.community.groups.members.MemberResponse>>

    @GET("api/mobile/v1/members")
    fun searchUsers(
        @Query("search") search: String,
        @Query("fields") fields: String = Constants.OPTIONS,
        @Query(value = "page", encoded = true) page: String = "1",
        @Query(value = "per_page", encoded = true) per_page: String = "10",
    ): LiveData<Resource<MemberResponse>>

    @GET("api/mobile/v1/groups/{id}/members")
    fun searchMembers(
        @Path("id") id: String,
        @Query("search") search: String,
        @Query("fields") fields: String = Constants.OPTIONS,
        @Query(value = "page", encoded = true) page: String = "1",
        @Query(value = "per_page", encoded = true) per_page: String = "10",
    ): LiveData<Resource<plm.patientslikeme2.data.model.community.groups.members.MemberResponse>>

    @DELETE("api/mobile/v1/user_avatars/remove_avatar")
    fun removeUserAvatars(): LiveData<Resource<AvatarResponse>>

    @GET("api/mobile/v1/groups/{id}/discussions")
    fun groupDiscussions(
        @Path("id") id: String,
        @Query(value = "sort_by", encoded = true) email: String = Constants.RECENT_ACTIVITY,
        @Query(value = "filter_params[my_followings]", encoded = true) myFollowings: Boolean = false,
        @Query(value = "filter_params[tag_followings]", encoded = true) myConditions: Boolean = false,
        @Query(value = "page", encoded = true) page: String = "1",
        @Query(value = "per_page", encoded = true) per_page: String = "20",
    ): LiveData<Resource<GroupDiscussions>>

    @GET("api/mobile/v1/groups/{id}/discussions")
    fun searchGroupDiscussions(
        @Path("id") id: String,
        @Query("search") search: String,
        @Query(value = "sort_by", encoded = true) email: String = Constants.RECENT_ACTIVITY,
        @Query(value = "fields", encoded = true) fields: String = Constants.OPTIONS,
        @Query(value = "page", encoded = true) page: String = "1",
        @Query(value = "per_page", encoded = true) per_page: String = "10",
    ): LiveData<Resource<GroupDiscussions>>

    @GET("api/mobile/v1/recommended_tags/for_group/{group_id}")
    fun getRecommendedTags(@Path("group_id") group_id: String): LiveData<Resource<RecommendedTagsResponse>>

    @GET("api/mobile/v1/messages")
    fun getViewAllMessages(
        @Query("page") page: Int,
        @Query(value = "per_page", encoded = true) per_page: Int = Constants.MESSAGES_PAGE_COUNT,
    ): LiveData<Resource<MessagesResponse>>

    @GET("api/mobile/v1/messages/search")
    fun getSearchMessages(
        @Query("query") search: String,
        @Query(value = "page", encoded = true) page: String = "1",
        @Query(value = "per_page", encoded = true) per_page: String = "10",
    ): LiveData<Resource<MessagesResponse>>

    @GET("api/mobile/v1/messages/conversation")
    fun getMessageThreadConversation(
        @Query("other_participant_id") search: String,
        @Query("page") page: Int,
        @Query(value = "per_page",
            encoded = true) per_page: Int = Constants.MESSAGE_CONVERSATION_PAGE_COUNT,
    ): LiveData<Resource<MessagesThreadResponse>>

    @PUT("api/mobile/v1/messages/{id}/block_user")
    fun putBlockMember(@Path("id") id: String): LiveData<Resource<MessagesResponse>>

    @PUT("api/mobile/v1/messages/{id}/mark_as_spam")
    fun putReportAsSpam(@Path("id") id: String): LiveData<Resource<MessagesResponse>>

    @PUT("api/mobile/v1/messages/{id}/mark_as_read")
    fun markAsRead(@Path("id") id: String): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/messages/unread_conversations_count")
    fun getUnreadConversationCount(): LiveData<Resource<MessageUnreadCountResponse>>

    @GET("api/mobile/v1/in_app_notifications/unseen_count")
    fun getUnreadNotificationCount(): LiveData<Resource<NotificationCountResponse>>

    @GET("api/mobile/v1/messages/list_members_by_login")
    fun getSearchMembers(
        @Query("login") query: String,
        @Query(value = "limit", encoded = true) limit: String = "10",
    ): LiveData<Resource<MemberResponse>>

    @POST("api/mobile/v1/messages")
    fun sendNewMessage(@Body request: NewMessageRequest): LiveData<Resource<NewMessageResponse>>

    @POST("api/mobile/v1/messages")
    fun replyMessage(@Body request: ReplyMessageRequest): LiveData<Resource<NewMessageResponse>>

    @GET("/api/mobile/v1/tags")
    fun searchTags(@Query("search") search: String): LiveData<Resource<RecommendedTagsResponse>>

    @POST("api/mobile/v1/groups/{group_id}/discussions")
    fun createDiscussion(
        @Path("group_id") group_id: String,
        @Body request: NewDiscussionRequest
    ): LiveData<Resource<CreateDiscussionResponse>>

    @GET("api/mobile/v1/discussions/{discussion_id}/comments")
    fun getCommentsOnDiscussion(
        @Path("discussion_id") discussionId: String,
        @Query(value = "sort_by", encoded = true) email: String = Constants.EMPTY,
        @Query(value = "page", encoded = true) page: String = "1",
        @Query(value = "per_page", encoded = true) per_page: String = "4",
    ): LiveData<Resource<GroupDiscussions>>

    @POST("api/mobile/v1/discussions/{discussion_id}/comments")
    fun addCommentsOnDiscussion(@Path("discussion_id") discussionId: String, @Body request: AddComment): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/discussions/{discussion_id}/comments/{comment_id}/quoted_post_comments")
    fun addReplyOnComments(@Path("discussion_id") discussionId: String, @Path("comment_id") commentId: String, @Body request: AddComment): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/discussions/{discussion_id}/comments/{comment_id}/quoted_post_comments")
    fun getReplyOnComments(
        @Path("discussion_id") discussionId: String, @Path("comment_id") commentId: String,
        @Query(value = "page", encoded = true) page: String = "1",
        @Query(value = "per_page", encoded = true) per_page: String = "4",
    ): LiveData<Resource<GroupDiscussions>>

    @POST("api/mobile/v1/discussions/{discussion_id}/ratings")
    fun addThumbsUpDiscussion(@Path("discussion_id") discussionId: String): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/discussions/{discussion_id}/ratings")
    fun removeThumbsUpDiscussion(@Path("discussion_id") discussionId: String): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/discussions/{discussion_id}/comments/{comment_id}/ratings")
    fun addThumbsUpComment(@Path("discussion_id") discussionId: String, @Path("comment_id") commentId: String): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/discussions/{discussion_id}/comments/{comment_id}/ratings")
    fun removeThumbsUpComment(@Path("discussion_id") discussionId: String, @Path("comment_id") commentId: String): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/discussions/{discussion_id}/comments/{comment_id}/quoted_post_comments/{reply_id}/ratings")
    fun addThumbsUpReply(@Path("discussion_id") discussionId: String, @Path("comment_id") commentId: String, @Path("reply_id") replyId: String): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/discussions/{discussion_id}/comments/{comment_id}/quoted_post_comments/{reply_id}/ratings")
    fun removeThumbsUpReply(@Path("discussion_id") discussionId: String, @Path("comment_id") commentId: String, @Path("reply_id") replyId: String): LiveData<Resource<CommonModel>>

    @Multipart
    @POST("api/mobile/v1/uploaded_images")
    fun uploadImage(@Part image: MultipartBody.Part): LiveData<Resource<ImageUpload>>

    @GET("api/mobile/v1/discussions/{discussion_id}")
    fun getDiscussion(@Path("discussion_id") discussionId: String): LiveData<Resource<GroupDiscussions>>

    @POST("api/mobile/v1/user_excluded_buckets")
    fun flagPost(@Body request: FlagPost): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/user_excluded_buckets")
    fun flagCommentReply(@Body request: FlagCommentReply): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/followings/discussions")
    fun getFollowingDiscussion(
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?
    ): LiveData<Resource<FollowingDiscussionResponse>>

    @GET("api/mobile/v1/followings/members")
    fun getFollowingMembers(
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?
    ): LiveData<Resource<FollowingMemberResponse>>

    @GET("api/mobile/v1/followings/tags")
    fun getFollowingTags(
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?
    ): LiveData<Resource<FollowingTagResponse>>

    @GET("api/mobile/v1/discussions")
    fun getTagDetailsList(
        @Query("tags[]") tags: String?,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?
    ): LiveData<Resource<TagDetailsResponse>>

    @POST("api/mobile/v1/discussions/{id}/follow")
    fun getFollowTagDetailsDiscussion(
        @Path("id") id: String?
    ): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/discussions/{id}/unfollow")
    fun postUnFollowDiscussion(
        @Path("id") id: String?
    ): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/tags/{id}/unfollow")
    fun postUnFollowTag(
        @Path("id") id: String?
    ): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/tags/{id}/follow")
    fun postFollowTag(
        @Path("id") id: String?
    ): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/resources/{id}/unfollow")
    fun postUnFollowResource(
        @Path("id") id: String?
    ): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/resources/{id}/follow")
    fun postFollowResource(
        @Path("id") id: String?
    ): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/followings/resources")
    fun getFollowingResources(
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?
    ): LiveData<Resource<FollowingResourceResponse>>

    @GET("api/mobile/v1/events/upcoming_events")
    fun getCommunityEvents(): LiveData<Resource<EventResponse>>

    @GET("api/mobile/v1/events/{id}/overview")
    fun getEventOverview(@Path("id") id: String?): LiveData<Resource<EventDetailsResponse>>

    @GET("api/mobile/v1/groups/{id}/events")
    fun getGroupEvents(@Path("id") id: String?): LiveData<Resource<EventResponse>>

    @GET("api/mobile/v1/groups/{id}/resources")
    fun getGroupResources(@Path("id") id: String?): LiveData<Resource<ResourceResponse>>

    @GET("api/mobile/v1/resources/{id}")
    fun getResourceOverview(@Path("id") id: String?): LiveData<Resource<ResourceDetailsResponse>>

    @GET("api/mobile/v1/resources")
    fun getGroupFilterResource(@Query("filter") filter: String): LiveData<Resource<ResourceResponse>>

    @GET("api/mobile/v1/announcements")
    fun getHomeAnnouncements(): LiveData<Resource<AnnouncementResponse>>

    @POST("api/mobile/v1/announcements/dismiss_all")
    fun postDismissAnnouncements(): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/home_cards")
    fun getHomePromoCardList(): LiveData<Resource<PromoCardResponse>>

    @POST("api/mobile/v1/home_cards/exclude_promo_card")
    fun postExcludePromoCard(@Body request: RemovePromoCard): LiveData<Resource<CommonModel>>

    @POST("api/mobile/v1/discussions/{discussion_id}/follow")
    fun followDiscussions(@Path("discussion_id") discussionId: String): LiveData<Resource<CommonModel>>

    @DELETE("api/mobile/v1/discussions/{discussion_id}/unfollow")
    fun unfollowDiscussions(@Path("discussion_id") discussionId: String): LiveData<Resource<CommonModel>>

    @GET("api/mobile/v1/discussions/{discussion_id}/comments/{comment_id}")
    fun getCommentDetails(@Path("discussion_id") discussionId: String, @Path("comment_id") commentId: String): LiveData<Resource<GroupDiscussions>>
}

