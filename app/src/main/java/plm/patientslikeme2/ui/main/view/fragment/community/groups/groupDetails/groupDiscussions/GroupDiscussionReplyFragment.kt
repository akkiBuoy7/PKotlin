@file:Suppress("NestedLambdaShadowedImplicitParameter")

package plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails.groupDiscussions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.Discussions
import plm.patientslikeme2.data.model.RecentComments
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.databinding.FragmentCommunityGroupDiscussionReplyBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowDiscussionCommentReplyWithLineBinding
import plm.patientslikeme2.databinding.RowPaginationBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.activity.home.MainActivity
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.CommentsOptionsDialog
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.CreateNewReplyDialog
import plm.patientslikeme2.ui.main.viewmodel.community.groups.DiscussionReplyViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.loadImage
import plm.patientslikeme2.utils.extensions.openUserProfile
import plm.patientslikeme2.utils.extensions.threadDescription

@AndroidEntryPoint
class GroupDiscussionReplyFragment : BaseFragment<FragmentCommunityGroupDiscussionReplyBinding>() {

    private val viewModel: DiscussionReplyViewModel by activityViewModels()
    private lateinit var adapter: GenericListAdapter<Discussions, RowDiscussionCommentReplyWithLineBinding>
    private lateinit var pagerAdapter: GenericListAdapter<String, RowPaginationBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCommunityGroupDiscussionReplyBinding =
        FragmentCommunityGroupDiscussionReplyBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.community_group_discussion_reply

        arguments?.getString(Constants.DISCUSSION_ID)?.let {
            viewModel.discussionId = it
        }

        arguments?.getBoolean(Constants.IS_MEMBER)?.let {
            viewModel.isMember = it
        }

        arguments?.getString(Constants.COMMENT_ID)?.let {
            viewModel.commentId = it
        }

        initView()
        bindObservers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        adapter = GenericListAdapter(R.layout.row_discussion_comment_reply_with_line, object : GenericRecyclerBindingInterface<RowDiscussionCommentReplyWithLineBinding, Discussions> {
            override fun bindData(binder: RowDiscussionCommentReplyWithLineBinding, model: Discussions, position: Int) {

                binder.ivUser.loadImage(model.user?.name, model.user?.avatar?.value)
                binder.ivVerified.isVisible = model.user?.followed == true

                val myPost = Preferences.getValue(Constants.USER_ID, "") == model.user?.id.toString()
                binder.like.isClickable = myPost.not()
                binder.like.isEnabled = myPost.not()
                binder.like.alpha = if (myPost) 0.5F else 1F
                binder.tvUserName.text = if(myPost) getString(R.string.you_replied) else model.user?.name
                binder.ivOptionMore.isVisible = myPost.not()

                binder.postedAt.text = model.postedAt
                binder.tvDiscussionDetails.threadDescription(model.body)
                binder.tvLikeCount.text = model.helpfulCount.toString()

                binder.executePendingBindings()

                binder.ivHelpful.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (model.markedHelpful == true) R.drawable.ic_helpful else R.drawable.ic_like_24))

                binder.like.setOnClickListener {
                    model.id?.let {
                        model.helpfulCount = if (model.markedHelpful!!) (model.helpfulCount - 1) else (model.helpfulCount + 1)
                        model.markedHelpful = model.markedHelpful!!.not()
                        adapter.notifyDataSetChanged()
                        viewModel.replyLikeUnlike(it, model.markedHelpful ?: false).observe(viewLifecycleOwner) {

                        }
                    }
                }

                binder.ivOptionMore.setOnClickListener {
                    model.id?.let { discussionId ->
                        model.user?.id?.let { userId ->
                            CommentsOptionsDialog(discussionId, userId) {
                                getComments()
                            }.show(childFragmentManager, getString(R.string.add_a_link))
                        }
                    }
                }

                binder.userLayout.setOnClickListener {
                    openUserProfile(Constants.DISCUSSION_REPLY, model.user?.id.toString())
                }

                binder.lineView.isVisible = (position == adapter.currentList.size - 1).not()

            }
        })
        binding?.rvReplies?.adapter = adapter

        pagerAdapter = GenericListAdapter(
            R.layout.row_pagination,
            object : GenericRecyclerBindingInterface<RowPaginationBinding, String> {
                @SuppressLint("NotifyDataSetChanged")
                override fun bindData(binder: RowPaginationBinding, model: String, position: Int) {
                    binder.page = model
                    binder.tvPage.setTextColor(
                        if (model == viewModel.currentPage) ContextCompat.getColor(
                            requireContext(),
                            R.color.teal
                        )
                        else ContextCompat.getColor(requireContext(), R.color.black)
                    )
                    binder.root.setOnClickListener {
                        viewModel.currentPage = model
                        pagerAdapter.notifyDataSetChanged()
                        getComments()
                    }
                    binder.executePendingBindings()
                }
            })
        binding?.paginationView?.rvPagination?.adapter = pagerAdapter

        binding?.paginationView?.ivBack?.setOnClickListener {
            var currentPage = viewModel.currentPage.toInt()
            if (currentPage > 1) {
                currentPage -= 1
                viewModel.currentPage = currentPage.toString()
                pagerAdapter.notifyDataSetChanged()
                getComments()
            }
        }

        binding?.paginationView?.ivForward?.setOnClickListener {
            var currentPage = viewModel.currentPage.toInt()
            if (currentPage < viewModel.totalPage) {
                currentPage += 1
                viewModel.currentPage = currentPage.toString()
                pagerAdapter.notifyDataSetChanged()
                getComments()
            }
        }

        binding?.ivScrollUp?.setOnClickListener {
            binding?.parent?.smoothScrollTo(0, 0)
        }

        binding?.like?.setOnClickListener {
            viewModel.recentComment.value?.let { comment ->

                comment.helpfulCount = if (comment.markedHelpful) (comment.helpfulCount - 1) else (comment.helpfulCount + 1)
                comment.markedHelpful = comment.markedHelpful.not()
                viewModel.recentComment.value = comment
                binding?.comment = comment
                binding?.ivHelpful?.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (comment.markedHelpful) R.drawable.ic_helpful else R.drawable.ic_like_24))

                viewModel.commentLikeUnlike(comment.markedHelpful).observe(viewLifecycleOwner) {

                }
            }
        }

        binding?.btnAddComment?.isVisible = viewModel.isMember
        binding?.divider?.isVisible = viewModel.isMember
        binding?.btnAddComment?.setOnClickListener {
            viewModel.recentComment.value?.let { comment ->
                CreateNewReplyDialog(comment.body ?: "", viewModel.discussionId, viewModel.commentId, comment.user?.name.toString()) {
                    getComments()
                }.show(childFragmentManager, getString(R.string.new_thread))
            }
        }

        binding?.addReply?.isVisible = viewModel.isMember
        binding?.addReply?.setOnClickListener {
            viewModel.recentComment.value?.let { comment ->
                CreateNewReplyDialog(comment.body ?: "", viewModel.discussionId, viewModel.commentId, comment.user?.name.toString()) {
                    getComments()
                }.show(childFragmentManager, getString(R.string.new_thread))
            }
        }

        binding?.userLayout?.setOnClickListener {
            viewModel.recentComment.value?.user?.id?.let { userId ->
                openUserProfile(Constants.DISCUSSION_REPLY, userId.toString())
            }
        }

        binding?.ivOptionMore?.setOnClickListener {
            viewModel.recentComment.value?.user?.id?.let { userId ->
                CommentsOptionsDialog(viewModel.discussionId.toInt(), userId) {
                    activity?.onBackPressed()
                }.show(childFragmentManager, getString(R.string.add_a_link))
            }
        }
    }


    private fun bindObservers() {

        viewModel.getCommentDetails().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    getComments()
                    it.data?.data?.comment?.let {
                        viewModel.recentComment.value = it
                        binding?.comment = it
                        updateUI(it)
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(it.errorMessage ?: getString(R.string.error_api))
                }
            }
        }
    }

    private fun getComments() {
        viewModel.getReplyOnComments().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    it.data?.let {
                        it.data?.comments?.let {
                            adapter.submitList(it) {
                                binding?.parent?.smoothScrollTo(0, 0)
                            }
                        }
                        it.meta?.let { meta ->
                            meta.totalEntries?.let { total ->
                                meta.perPage?.let { onPage ->
                                    val pageList = ArrayList<String>()
                                    var pages = (total / onPage)
                                    pages = if (total % onPage == 0) pages else (pages + 1)
                                    viewModel.totalPage = pages
                                    if (pages > 1) {
                                        for (page in 1 until pages + 1) {
                                            pageList.add(page.toString())
                                        }
                                    }
                                    if (pageList.isEmpty().not()) {
                                        pagerAdapter.submitList(pageList)
                                        binding?.paginationView?.paginationView?.isVisible = true
                                    } else {
                                        binding?.paginationView?.paginationView?.isVisible = false
                                    }
                                }
                            }
                        }

                        binding?.ivScrollUp?.isVisible = true
                        binding?.rvReplies?.isVisible = true
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage = ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showEmptyView(errorMessage)
                }
            }
        }
    }

    private fun updateUI(disucssion: RecentComments) {
        binding?.ivHelpful?.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (disucssion.markedHelpful) R.drawable.ic_helpful else R.drawable.ic_like_24))
        binding?.ivUser?.loadImage(disucssion.user?.name, disucssion.user?.avatar?.value)
        binding?.ivVerified?.isVisible = disucssion.user?.followed == true


        val myPost = Preferences.getValue(Constants.USER_ID, "") == disucssion.user?.id.toString()
        binding?.like?.isClickable = myPost.not()
        binding?.like?.isEnabled = myPost.not()
        binding?.like?.alpha = if (myPost) 0.5F else 1F
        binding?.tvUserName?.text = if(myPost) getString(R.string.you_replied) else disucssion.user?.name

        binding?.ivOptionMore?.isVisible = myPost.not()

        if (disucssion.commentCount - 1 > 0) {
            binding?.tvCommenterCount?.text = "+${Integer.min(disucssion.commentCount - 1, 99)}"
            binding?.flUser2?.isVisible = true
        }

        disucssion.recentCommenters.forEachIndexed { index, element ->
            when (index) {
                0 -> {
                    binding?.ivUser1?.loadImage(element.name, element.avatar?.value)
                    binding?.ivUser1?.isVisible = true
                }
            }
        }

        binding?.parent?.visibility = View.VISIBLE
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

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.back_to_discussion),
            showBackArrow = true,
            hideRightIcon = true)
    }
}