package plm.patientslikeme2.ui.main.view.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentSettingsBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.profile.ProfileViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.handlers = this
        binding?.model = featureFlag
        bindUserDetails()
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
        when (view.id) {
            R.id.tv_push_notification -> {
                findNavController().navigate(R.id.action_navigation_settings_to_pushNotificationsFragment)
            }
            R.id.tv_units -> {
                findNavController().navigate(R.id.action_navigation_settings_to_unitsFragment)
            }
            R.id.tv_privacy_settings -> {
                findNavController().navigate(R.id.action_navigation_settings_to_privacySettingsFragment)
            }
            R.id.tv_screen_brightness -> {
                findNavController().navigate(R.id.action_navigation_settings_to_screenBrightnessFragment)
            }
            R.id.tv_appearance -> {
                findNavController().navigate(R.id.action_navigation_settings_to_appearanceFragment)
            }
            R.id.tv_email_password -> {
                findNavController().navigate(R.id.action_navigation_settings_to_emailPasswordFragment)
            }
            R.id.tv_blocked_members -> {
                findNavController().navigate(R.id.action_navigation_settings_to_blockedMembersFragment)
            }
        }
    }

    private fun bindUserDetails() {
        viewModel.getUserDetails().observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    Preferences.setValue(Constants.USER, Gson().toJson(it.data?.user))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.nav_menu_settings),
            showBackArrow = false,
            hideRightIcon = true
        )
    }
}