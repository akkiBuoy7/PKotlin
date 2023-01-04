package plm.patientslikeme2.ui.main.viewmodel.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.model.home.RemovePromoCard
import plm.patientslikeme2.data.model.login.UserDevices
import plm.patientslikeme2.data.repository.community.CommunityGroupRepository
import plm.patientslikeme2.data.repository.home.HomeRepository
import plm.patientslikeme2.data.repository.profile.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val communityRepository: CommunityGroupRepository,
    private val profileRepository: ProfileRepository
) :
    ViewModel() {

    private val pageSize = 20

    private fun fetchHomeAnnouncements() = repository.getHomeAnnouncements()

    fun getHomeAnnouncements() = fetchHomeAnnouncements()

    private fun postDismissAnnouncements() = repository.postDismissAnnouncements()

    fun updateDismissAnnouncements() = postDismissAnnouncements()

    private fun fetchHomePromoCardList() = repository.getHomePromoCardList()

    fun getHomePromoCardList() = fetchHomePromoCardList()

    private fun updateExcludePromoCard(request: RemovePromoCard) =
        repository.postExcludePromoCard(request)

    fun postExcludePromoCard(request: RemovePromoCard) = updateExcludePromoCard(request)

    private fun fetchHomeConditionCard() = repository.getHomeConditionCard()

    fun getHomeConditionCard() = fetchHomeConditionCard()

    private fun fetchHomeGoalTrackingList() = repository.getHomeGoalTrackingList()

    fun getHomeGoalTrackingList() = fetchHomeGoalTrackingList()

    private fun fetchNotificationList(page: Int?) = repository.getNotificationList(page, pageSize)

    fun getNotificationList(page: Int?) = fetchNotificationList(page)

    private fun updateMarkAsReadNotification(id: Int?) = repository.putMarkAsReadNotification(id)

    fun putMarkAsReadNotification(id: Int?) = updateMarkAsReadNotification(id)

    private fun dismissNotification(id: Int?) = repository.putDismissNotification(id)

    fun putDismissNotification(id: Int?) = dismissNotification(id)

    private fun updateUserDevices(request: UserDevices) = repository.postUserDevices(request)

    fun postUserDevices(request: UserDevices) = updateUserDevices(request)

    private fun fetchUnreadConversationCount() = repository.getUnreadConversationCount()

    fun getUnreadConversationCount() = fetchUnreadConversationCount()

    private fun fetchUnreadNotificationCount() = repository.getUnreadNotificationCount()

    fun getUnreadNotificationCount() = fetchUnreadNotificationCount()

    private fun fetchMyGroupList() = communityRepository.getMyGroupList()

    fun getMyGroupList() = fetchMyGroupList()

    private fun updateUserLogout() = repository.postUserLogout()

    fun postUserLogout() = updateUserLogout()

    private fun fetchUserDetails() = profileRepository.getUserDetails()

    fun getUserDetails() = fetchUserDetails()
}