package plm.patientslikeme2.ui.main.view.fragment.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.settings.PushNotificationSettings
import plm.patientslikeme2.databinding.FragmentPushNotificationsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.settings.SettingsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.objectToString
import plm.patientslikeme2.utils.extensions.stringToObject

@AndroidEntryPoint
class PushNotificationsFragment : BaseFragment<FragmentPushNotificationsBinding>(),
    View.OnClickListener {

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPushNotificationsBinding =
        FragmentPushNotificationsBinding.inflate(inflater, container, false)

    private lateinit var settings: PushNotificationSettings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.pushNotificationsFragment

        initView()
    }

    private fun initView() {
        if (isConnected()) {
            bindObservers()
        } else {
            showSuccessErrorLayout(false, getString(R.string.error_internet))
        }
        binding?.scTreatmentReminder?.setOnClickListener(this)
        binding?.scGoalReminder?.setOnClickListener(this)
        binding?.scPrivateMessages?.setOnClickListener(this)
        binding?.scRepliesReactions?.setOnClickListener(this)
        binding?.scDiscussion?.setOnClickListener(this)
        binding?.scSomeoneMentions?.setOnClickListener(this)
        binding?.scSomeoneStarted?.setOnClickListener(this)
        binding?.scDiscussionStarted?.setOnClickListener(this)

        val preferences = Preferences.getValue(Constants.PUSH_NOTIFICATION, "")
        if (!TextUtils.isEmpty(preferences)) {
            settings = stringToObject(preferences)
            updateNotificationState()
        }
    }

    private fun bindObservers() {
        viewModel.getPushNotificationSettings().observe(viewLifecycleOwner) { it ->
            when {
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        it.data?.data?.user_preference?.mobile_push_notification?.let {
                            settings = it
                            updateNotificationState()
                            updateSettingPreferences()
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    showSuccessErrorLayout(false, getString(R.string.error_api))
                }
            }
        }
    }

    private fun updateSettingPreferences() {
        Preferences.setValue(Constants.PUSH_NOTIFICATION, objectToString(settings))
    }

    private fun updateNotificationState() {
        binding?.scTreatmentReminder?.isChecked = settings.treatment_reminders
        binding?.scGoalReminder?.isChecked = settings.goal_reminders
        binding?.scPrivateMessages?.isChecked = settings.private_messages
        binding?.scRepliesReactions?.isChecked = settings.reactions_to_discussion
        binding?.scDiscussion?.isChecked = settings.discussion_following
        binding?.scSomeoneMentions?.isChecked = settings.someone_mentioned
        binding?.scSomeoneStarted?.isChecked = settings.someone_following
        binding?.scDiscussionStarted?.isChecked = settings.discussion_started
    }

    override fun onClick(view: View?) {
        if (!isAdded) {
            return
        }
        var key = ""
        val status = getCheckButtonState((view as SwitchCompat).isChecked)
        when (view.id) {
            R.id.sc_treatment_reminder -> {
                settings.treatment_reminders = binding?.scTreatmentReminder?.isChecked ?: false
                key = Constants.TREATMENT_REMINDERS
            }
            R.id.sc_goal_reminder -> {
                settings.goal_reminders = binding?.scGoalReminder?.isChecked ?: false
                key = Constants.GOAL_REMINDERS
            }
            R.id.sc_private_messages -> {
                settings.private_messages = binding?.scPrivateMessages?.isChecked ?: false
                key = Constants.PRIVATE_MESSAGES
            }
            R.id.sc_replies_reactions -> {
                settings.reactions_to_discussion = binding?.scRepliesReactions?.isChecked ?: false
                key = Constants.REACTIONS_TO_DISCUSSION
            }
            R.id.sc_discussion -> {
                settings.discussion_following = binding?.scDiscussion?.isChecked ?: false
                key = Constants.DISCUSSION_FOLLOWING
            }
            R.id.sc_someone_mentions -> {
                settings.someone_mentioned = binding?.scSomeoneMentions?.isChecked ?: false
                key = Constants.SOMEONE_MENTIONED
            }
            R.id.sc_someone_started -> {
                settings.someone_following = binding?.scSomeoneStarted?.isChecked ?: false
                key = Constants.SOMEONE_FOLLOWING
            }
            R.id.sc_discussion_started -> {
                settings.discussion_started = binding?.scDiscussionStarted?.isChecked ?: false
                key = Constants.DISCUSSION_STARTED
            }
        }

        if (isConnected()) {
            updateNotification(key, status)
        }
    }

    private fun updateNotification(key: String, status: String) {
        viewModel.putPushNotificationSettings(key, status).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    showSuccessErrorLayout(true, it.data?.message.toString())
                    updateSettingPreferences()
                }

                it.status.isError() -> {
                    hideProgress()
                    var errorMessage = apiErrorMessage(it.errorMessage)
                    if (errorMessage.isEmpty()) {
                        errorMessage = getString(R.string.failed_push_notification)
                    }
                    showSuccessErrorLayout(false, errorMessage)
                }
            }
        }
    }

    private fun getCheckButtonState(state: Boolean): String {
        return if (state) {
            Constants.ENABLE
        } else {
            Constants.DISABLE
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.push_notifications),
            showBackArrow = true,
            hideRightIcon = true
        )
    }
}