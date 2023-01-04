package plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.Discussions
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.community.groups.groupOverview.GroupOverviewResponse
import plm.patientslikeme2.databinding.FragmentGroupOverviewBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowGroupOverviewFeaturedDiscussionsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.DiscussionOptionsDialog
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.*

@AndroidEntryPoint
class GroupOverviewFragment : BaseFragment<FragmentGroupOverviewBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()
    private lateinit var adapter: GenericListAdapter<Discussions, RowGroupOverviewFeaturedDiscussionsBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentGroupOverviewBinding =
        FragmentGroupOverviewBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.group_fragment
        initView()
        bindObservers()
    }

    private fun initView() {
        if (viewModel.isPrivateGroup) {
            binding?.highlight?.tvGroupHighlight?.text = getString(R.string.join_the_conversation)
        }

        binding?.groupOverviewResponse = GroupOverviewResponse()
        adapter = GenericListAdapter(R.layout.row_group_overview_featured_discussions, object :
            GenericRecyclerBindingInterface<RowGroupOverviewFeaturedDiscussionsBinding, Discussions> {
            override fun bindData(
                binder: RowGroupOverviewFeaturedDiscussionsBinding,
                model: Discussions,
                position: Int
            ) {
                binder.featuredDiscussions = model
                binder.ivUser.loadImage(model.user?.name, model.user?.avatar?.value)
                binder.tags.setClickableTagsOrBadges(model.tags, true) {
                    Log.e("setClickableTagsOrBadges", "--->> $it")
                    // TODO add navigation
                }

                val myPost =
                    Preferences.getValue(Constants.USER_ID, "") == model.user?.id.toString()
                binder.like.isClickable = myPost.not()
                binder.like.isEnabled = myPost.not()
                binder.like.alpha = if (myPost) 0.5F else 1F

                binder.ivOptionMore.isVisible = myPost.not()

                binder.root.setOnClickListener {
                    model.id?.let { discussionId ->
                        openGroupDiscussionDetails(Constants.GROUP_DISCUSSIONS, discussionId.toString(), viewModel.aboutGroupResponse.value?.data?.data?.name, model, (viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember == true))
                    }
                }

                binder.tvDiscussionDetails.setOnClickListener {
                    model.id?.let { discussionId ->
                        openGroupDiscussionDetails(Constants.GROUP_DISCUSSIONS, discussionId.toString(), viewModel.aboutGroupResponse.value?.data?.data?.name, model, (viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember == true))
                    }
                }

                binder.tvRecentComment.setOnClickListener {
                    model.id?.let { discussionId ->
                        openGroupDiscussionDetails(Constants.GROUP_DISCUSSIONS, discussionId.toString(), viewModel.aboutGroupResponse.value?.data?.data?.name, model, (viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember == true))
                    }
                }

                binder.userLayout.setOnClickListener {
                    model.user?.id?.let { Id ->
                        openUserProfile(Constants.GROUP_MEMBER, Id.toString())
                    }
                }

                binder.ivHelpful.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        if (model.markedHelpful == true) R.drawable.ic_helpful else R.drawable.ic_like_24
                    )
                )
                binder.like.setOnClickListener {
                    if (model.id != null && model.markedHelpful != null) {
                        model.helpfulCount =
                            if (model.markedHelpful!!) (model.helpfulCount - 1) else (model.helpfulCount + 1)
                        model.markedHelpful = model.markedHelpful!!.not()
                        adapter.notifyDataSetChanged()
                        viewModel.discussionLikeUnlike(model.id!!, model.markedHelpful!!)
                            .observe(viewLifecycleOwner) {

                            }
                    }
                }
                binder.ivOptionMore.setOnClickListener {
                    model.id?.let { discussionId ->
                        model.user?.id?.let { userId ->
                            DiscussionOptionsDialog(discussionId, userId) {
                                viewModel.groupOverview().observe(viewLifecycleOwner) {
                                    viewModel.groupOverviewResponse.postValue(it)
                                }
                            }.show(childFragmentManager, getString(R.string.add_a_link))
                        }
                    }
                }
                binder.executePendingBindings()
            }
        })
        binding?.discussions?.rvDiscussions?.adapter = adapter

        binding?.btnLeaveGroup?.setOnClickListener {
            showConfirmDialog()
        }
    }

    private fun showConfirmDialog() {
        activity?.let {
            showActionDialog(it,
                getString(R.string.leave_this_group),
                getString(R.string.msg_leave_group),
                getString(R.string.yes),
                getString(R.string.cancel),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        unJoinGroup()
                    }
                })
        }
    }

    private fun unJoinGroup() {
        viewModel.groupOverviewResponse.value?.data?.data?.group?.id?.let { groupId ->
            viewModel.unJoinGroup(groupId).observe(viewLifecycleOwner) {
                when {
                    it.status.isLoading() -> {
                        showProgress()
                    }
                    it.status.isSuccessful() -> {
                        hideProgress()
                        showSuccessErrorLayout(true, it.data?.message.toString())
                        findNavController().popBackStack(currentFragment, true)
                    }
                    it.status.isError() -> {
                        hideProgress()
                        var errorMessage =
                            ErrorResponse().stringToObject(it.errorMessage).message.toString()
                        if (errorMessage.isEmpty()) {
                            errorMessage = getString(R.string.failed_leave_group)
                        }
                        showSuccessErrorLayout(false, errorMessage)
                    }
                }
            }
        }
    }

    private fun bindObservers() {
        viewModel.groupOverviewResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    it.data?.let {
                        binding?.groupOverviewResponse = it
                        it.data?.group?.highlights?.topContributors?.let{
                            it.forEachIndexed { index, element ->
                                when(index){
                                    0 -> {
                                        binding?.highlight?.ivUser1?.loadImage(element.name, element.avatarUrl)
                                    }
                                    1 -> {
                                        binding?.highlight?.ivUser2?.loadImage(element.name, element.avatarUrl)
                                    }
                                    2 -> {
                                        binding?.highlight?.ivUser3?.loadImage(element.name, element.avatarUrl)
                                    }
                                }
                            }
                        }
                        it.data?.group?.featuredDiscussions?.let {
                            adapter.submitList(it)
                        }
                    }
                }
            }
            checkDataStatus()
        }
        checkDataStatus()
    }

    private fun checkDataStatus() {
        if (viewModel.groupOverviewResponse.value?.status == Status.LOADING) {
            showProgress()
        } else if (viewModel.groupOverviewResponse.value?.status == Status.SUCCESS) {
            hideProgress()
        } else if (viewModel.groupOverviewResponse.value?.status == Status.ERROR) {
            hideProgress()
            showEmptyView(getString(R.string.error_api))
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