package plm.patientslikeme2.ui.main.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.profile.AboutConditions
import plm.patientslikeme2.data.model.profile.UserProfile
import plm.patientslikeme2.databinding.FragmentMembersAboutBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowProfileConditionBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.viewmodel.profile.ProfileViewModel
import plm.patientslikeme2.utils.extensions.setHtmlText
import plm.patientslikeme2.utils.extensions.setTextBold

@AndroidEntryPoint
class MemberAboutFragment : BaseFragment<FragmentMembersAboutBinding>() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var conditionsAdapter: GenericListAdapter<AboutConditions, RowProfileConditionBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMembersAboutBinding = FragmentMembersAboutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.user_profile

        initViews()
        if (isConnected()) bindMemberAboutObserver() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initViews() {
        conditionsAdapter =
            GenericListAdapter(R.layout.row_profile_condition, object :
                GenericRecyclerBindingInterface<RowProfileConditionBinding, AboutConditions> {
                override fun bindData(
                    binder: RowProfileConditionBinding,
                    model: AboutConditions,
                    position: Int
                ) {
                    binder.model = model
                    binder.executePendingBindings()
                }
            })
        binding?.rvConditions?.adapter = conditionsAdapter
    }

    private fun bindMemberAboutObserver() {
        viewModel.getMemberProfile().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data != null) {
                        binding?.model = it.data

                        it.data?.data?.user_profile?.let { userProfile ->
                            renderData(userProfile)
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun renderData(userProfile: UserProfile) {
        if (userProfile.about?.story_content != null && userProfile.about?.story_content?.length != 0 && userProfile.about?.story_content != "<p><br></p>") {
            binding?.tvDescription?.visibility = View.VISIBLE
            binding?.tvMyStoryEmpty?.visibility = View.GONE
            binding?.tvDescription?.setHtmlText(userProfile.about?.story_content)
        } else {
            binding?.tvDescription?.visibility = View.GONE
            binding?.tvMyStoryEmpty?.visibility = View.VISIBLE
        }

        binding?.tvPrimaryInterest?.setTextBold(
            getString(R.string.primary_interest_colon),
            " " + if (userProfile.about?.interests?.primary != null) userProfile.about?.interests?.primary else "----",
        )

        var secondary = " "
        if (userProfile.about?.interests?.secondary?.size!! > 0)
            for ((index, item) in userProfile.about?.interests?.secondary!!.withIndex()) {
                secondary += if (userProfile.about?.interests?.secondary!!.size.minus(
                        1
                    ) == index
                ) item else "${item}, "
            } else secondary += "----"

        binding?.tvSecondaryInterests?.setTextBold(
            getString(R.string.secondary_interests_colon), secondary
        )

        if (userProfile.about?.interests?.secondary?.size!! == 0 && userProfile.about?.interests?.primary == null) {
            binding?.tvPrimaryInterest?.visibility = View.GONE
            binding?.tvSecondaryInterests?.visibility = View.GONE
            binding?.tvInterestsEmpty?.visibility = View.VISIBLE
        }
        conditionsAdapter.submitList(userProfile.about?.conditions)

        userProfile.about?.community_badges.let {
            if (it != null && it.size > 0) {
                binding?.llBadges?.visibility = View.GONE
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

}