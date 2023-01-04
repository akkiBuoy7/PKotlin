package plm.patientslikeme2.ui.main.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.signup.User
import plm.patientslikeme2.databinding.FragmentPersonalInfoBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.view.dialog.profile.UpdatePersonalInformationDialog
import plm.patientslikeme2.ui.main.viewmodel.profile.ProfileViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.PERSONAL_INFORMATION
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.*

@AndroidEntryPoint
class PersonalInfoFragment : BaseFragment<FragmentPersonalInfoBinding>() {

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPersonalInfoBinding = FragmentPersonalInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.navigation_personal_info
        binding?.handlers = this

        initViews()
        if (isConnected()) bindGetPersonalInfoObserver() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initViews() {
        toolbarBinding?.btnAction?.visibility =
            if (featureFlag.android_edit_profile) View.VISIBLE else View.GONE

        toolbarBinding?.btnAction?.setOnClickListener {
            UpdatePersonalInformationDialog.newInstance()
                .show(childFragmentManager, getString(R.string.edit_profile))
        }
    }

    private fun bindGetPersonalInfoObserver() {
        viewModel.getUserDetails().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.user != null) {
                        binding?.model = it.data
                        it?.data?.user.apply {
                            Preferences.setValue(Constants.PROFILE_COLOR, this?.profile_colour)
                            renderData(this)
                        }
                    } else {
                        showEmptyView(getString(R.string.error_result))
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }
    }

    private fun renderData(user: User?) {

        binding?.ivUser?.loadImage(user?.full_name, user?.avatar_url)

        binding?.tvBirthdate?.text = convertDate(
            "yyyy-MM-dd", "MM/dd/yyyy",
            if (user?.birth_date != null) user.birth_date else ""
        )

        var gender = ""
        user?.canned_genders.let { canned_genders ->
            canned_genders?.size.let { size ->
                if (size != null) {
                    gender = if (size > 0) user?.canned_genders?.get(0).toString() else "----"
                }
            }
        }
        binding?.tvGender?.text = gender

        binding?.tvPrimaryInterest?.setTextBold(
            getString(R.string.primary_interest_colon),
            " " + if (user?.primary_interest?.name != null) user.primary_interest?.name else "----"
        )

        var secondary = " "
        if (user?.secondary_interests?.size!! > 0)
            for ((index, item) in user.secondary_interests!!.withIndex()) {
                secondary += if (user.secondary_interests!!.size.minus(1) == index) {
                    "${item.name}"
                } else {
                    "${item.name}, "
                }
            } else secondary += "----"

        binding?.tvSecondaryInterests?.setTextBold(
            getString(R.string.secondary_interests_colon), secondary
        )

        if (user.secondary_interests?.size!! == 0 && user.primary_interest?.name == null) {
            binding?.tvPrimaryInterest?.visibility = View.GONE
            binding?.tvSecondaryInterests?.visibility = View.GONE
            binding?.tvInterestsEmpty?.visibility = View.VISIBLE
        }

        if (user.bio?.content != null && user.bio?.content?.isNotEmpty() == true && user.bio?.content != "<p><br></p>") {
            binding?.tvMyStory?.visibility = View.VISIBLE
            binding?.tvMyStoryEmpty?.visibility = View.GONE
            binding?.tvMyStory?.setHtmlText(user.bio?.content)
        } else {
            binding?.tvMyStory?.visibility = View.GONE
            binding?.tvMyStoryEmpty?.visibility = View.VISIBLE
        }

        binding?.tvEthnicity?.setTextBold(
            getString(R.string.ethnicity_colon),
            " " + if (user.ethnicity != null) user.ethnicity else "----"
        )

        binding?.tvHighestLevelOfEducation?.setTextBold(
            getString(R.string.highest_level_of_education_colon),
            " " + if (user.education_level != null) user.education_level else "----"
        )

        binding?.tvInsurance?.setTextBold(
            getString(R.string.insurance_colon),
            " " + if (user.health_insurance_type != null) user.health_insurance_type else "----"
        )

        binding?.tvMilitaryStatus?.setTextBold(
            getString(R.string.military_status_colon),
            " " + if (user.military_status != null) user.military_status else "----"
        )

        val cityState = when {
            !user.city.isNullOrEmpty() && !user.state.isNullOrEmpty() -> "${user.city}, ${user.state}"
            !user.city.isNullOrEmpty() && user.state.isNullOrEmpty() -> "${user.city}"
            user.city.isNullOrEmpty() && !user.state.isNullOrEmpty() -> "${user.state}"
            else -> ""
        }
        binding?.tvLocation?.setTextBold(getString(R.string.location_colon), " $cityState")
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
        when (view.id) {
            R.id.btn_view_my_profile -> {
                val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
                openUserProfile(PERSONAL_INFORMATION, user.id.toString())
            }
        }
    }

    private fun showEmptyView(error: String) {
        if (binding?.vsNoResult?.viewStub?.parent != null) {
            binding?.vsNoResult?.setOnInflateListener { _, _ ->
                val binding = binding?.vsNoResult?.binding as LayoutNoResultBinding
                binding.tvMessage.text = error
                binding.clEmpty.visibility = View.VISIBLE
            }
            binding?.vsNoResult?.viewStub?.inflate()
            binding?.parent?.visibility = View.GONE
        }
    }

    fun showSuccessErrorView(message: String?, status: Boolean?) {
        if (status != null) {
            if (message != null) {
                showSuccessErrorLayout(status, message)
                bindGetPersonalInfoObserver()
            }
        }
    }

    fun loadImageData(avatar_url: String) {
        val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
        binding?.ivUser?.loadImage(user.login, avatar_url)
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.nav_menu_personal_information),
            showBackArrow = false,
            hideRightIcon = true,
            showCloseRightIcon = true,
            showRightAction = true,
            actionButtonText = "Edit"
        )
    }
}