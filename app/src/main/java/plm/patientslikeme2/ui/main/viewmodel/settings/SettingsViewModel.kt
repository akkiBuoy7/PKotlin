package plm.patientslikeme2.ui.main.viewmodel.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.model.settings.*
import plm.patientslikeme2.data.repository.settings.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository) :
    ViewModel() {

    private val pageSize = 20

    private fun putUpdateEmail(request: EmailUpdateRequest) = repository.updateEmail(request)

    fun updateEmail(request: EmailUpdateRequest) = putUpdateEmail(request)

    private fun putVerifyEmail() = repository.verifyEmail()

    fun verifyEmail() = putVerifyEmail()

    private fun putUpdatePassword(request: PasswordUpdateRequest) =
        repository.updatePassword(request)

    fun updatePassword(request: PasswordUpdateRequest) = putUpdatePassword(request)

    private fun fetchUserPreferences() = repository.getUserPreferences()

    fun getUserPreferences() = fetchUserPreferences()

    private fun putUserPreferences(request: PrivacySettingsRequest) =
        repository.updateUserPreferences(request)

    fun updateUserPreferences(request: PrivacySettingsRequest) = putUserPreferences(request)

    private fun fetUserDetails() = repository.getUserDetails()

    fun getUserDetails() = fetUserDetails()

    private fun fetchBlockedPrivateMessages(page: Int?) =
        repository.getBlockedPrivateMessages(page, pageSize)

    fun getBlockedPrivateMessages(page: Int?) = fetchBlockedPrivateMessages(page)

    private fun postUnBlockedPrivateMessages(request: BlockUnBlockUserRequest) =
        repository.postUnBlockedPrivateMessages(request)

    fun unBlockedPrivateMessages(request: BlockUnBlockUserRequest) =
        postUnBlockedPrivateMessages(request)

    private fun postBlockedPrivateMessages(request: BlockUnBlockUserRequest) =
        repository.postBlockedPrivateMessages(request)

    fun blockedPrivateMessages(request: BlockUnBlockUserRequest) =
        postBlockedPrivateMessages(request)

    private fun fetchHiddenPosts(page: Int?) = repository.getHiddenPosts(page, pageSize)

    fun getHiddenPosts(page: Int?) = fetchHiddenPosts(page)

    private fun postUnHiddenPosts(request: HiddenUnHiddenPostRequest) =
        repository.postUnHiddenPosts(request)

    fun unHiddenPosts(request: HiddenUnHiddenPostRequest) = postUnHiddenPosts(request)

    private fun postHiddenPosts(request: HiddenUnHiddenPostRequest) =
        repository.postHiddenPosts(request)

    fun hiddenPosts(request: HiddenUnHiddenPostRequest) = postHiddenPosts(request)

    private fun fetchPushNotificationSettings() =
        repository.getPushNotificationSettings()

    fun getPushNotificationSettings() = fetchPushNotificationSettings()

    private fun updatePushNotificationSettings(key: String, status: String) =
        repository.putPushNotificationSettings(key, status)

    fun putPushNotificationSettings(key: String, status: String) =
        updatePushNotificationSettings(key, status)
}