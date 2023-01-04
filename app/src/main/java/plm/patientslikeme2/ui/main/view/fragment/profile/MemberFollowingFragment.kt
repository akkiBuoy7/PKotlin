package plm.patientslikeme2.ui.main.view.fragment.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.profile.UserFollowings
import plm.patientslikeme2.databinding.FragmentMembersFollowingBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowProfileMemberBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.viewmodel.profile.ProfileViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.openUserProfile

@AndroidEntryPoint
class MemberFollowingFragment : BaseFragment<FragmentMembersFollowingBinding>() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var memberFollowingAdapter: GenericListAdapter<UserFollowings, RowProfileMemberBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMembersFollowingBinding =
        FragmentMembersFollowingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.user_profile

        initViews()
        if (isConnected()) bindMemberFollowingsObserver() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initViews() {
        memberFollowingAdapter = GenericListAdapter(R.layout.row_profile_member, object :
            GenericRecyclerBindingInterface<RowProfileMemberBinding, UserFollowings> {
            override fun bindData(
                binder: RowProfileMemberBinding,
                model: UserFollowings,
                position: Int
            ) {
                binder.model = model
                binder.root.setOnClickListener {
                    openUserProfile(Constants.FOLLOWING, model.id.toString())
                }
                binder.executePendingBindings()
            }
        })
        binding?.rvFollowings?.adapter = memberFollowingAdapter
    }

    private fun bindMemberFollowingsObserver() {
        viewModel.getMemberFollowings().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.user_followings != null) {
                        renderData(it.data?.data?.user_followings!!)
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

    @SuppressLint("SetTextI18n")
    private fun renderData(userFollowings: ArrayList<UserFollowings>) {
        memberFollowingAdapter.submitList(userFollowings)
        binding?.tvHeading?.text =
            getString(R.string.following) + " ${userFollowings.size} " + getString(R.string.members_lowercase)
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