package plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails.groupDiscussions

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.databinding.FragmentCommunityGroupDiscussionThreadBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowDiscussionCommentsBinding
import plm.patientslikeme2.databinding.RowPaginationBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.activity.home.MainActivity
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.CommentsOptionsDialog
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.CreateNewCommentDialog
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.CreateNewReplyDialog
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.DiscussionOptionsDialog
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.comments.SortCommentsDialog
import plm.patientslikeme2.ui.main.viewmodel.community.groups.DiscussionThreadViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.*
import java.lang.Integer.min

@AndroidEntryPoint
class GroupDiscussionThreadFragment : BaseFragment<FragmentCommunityGroupDiscussionThreadBinding>() {

    private val viewModel: DiscussionThreadViewModel by activityViewModels()
    private lateinit var adapter: GenericListAdapter<Discussions, RowDiscussionCommentsBinding>
    private lateinit var pagerAdapter: GenericListAdapter<String, RowPaginationBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCommunityGroupDiscussionThreadBinding =
        FragmentCommunityGroupDiscussionThreadBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.community_group_discussion_thread

        arguments?.getString(Constants.DISCUSSION_ID)?.let {
            viewModel.discussionId = it
        }

        arguments?.getBoolean(Constants.IS_MEMBER)?.let {
            viewModel.isMember = it
        }

        arguments?.getString(Constants.DISCUSSION_DETAILS)?.let {
            val discussion = Discussions().stringToObject(it)
            viewModel.discussion.value = discussion
            binding?.discussions = discussion
            binding?.parent?.visibility = View.VISIBLE
        }

        initView()
        bindObservers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        if (activity is MainActivity) {
            (activity as MainActivity).toolbarHome?.toolbarTitle?.text = arguments?.getString(Constants.GROUP_TITLE)
        }
        updateBookmarkIcon(false)
        adapter = GenericListAdapter(R.layout.row_discussion_comments, object : GenericRecyclerBindingInterface<RowDiscussionCommentsBinding, Discussions> {
            override fun bindData(binder: RowDiscussionCommentsBinding, model: Discussions, position: Int) {
                binder.discussions = model
                binder.ivUser.loadImage(model.user?.name, model.user?.avatar?.value)
                binder.ivVerified.isVisible = model.user?.followed == true

                binder.seeMoreReplies.text = getString(R.string.see_more_replies, (model.commentCount - 1).toString())
                binder.seeMoreReplies.visibility = if (model.commentCount - 1 > 0) View.VISIBLE else View.INVISIBLE
                binder.curveView.visibility = if (model.commentCount - 1 > 0) View.VISIBLE else View.INVISIBLE
                binder.lineView.visibility = if (model.commentCount - 1 > 0) View.VISIBLE else View.INVISIBLE

                binder.ivOptionMore.setOnClickListener {
                    model.id?.let { discussionId ->
                        model.user?.id?.let { userId ->
                            CommentsOptionsDialog(discussionId, userId) {
                                getComments(viewModel.discussionId)
                            }.show(childFragmentManager, getString(R.string.add_a_link))
                        }
                    }
                }

                if (model.recentComments.size > 0) {
                    val recentComment = model.recentComments[0]

                    val recentCommentMyPost = Preferences.getValue(Constants.USER_ID, "") == recentComment.user?.id.toString()
                    binder.commentReply.like.isClickable = recentCommentMyPost.not()
                    binder.commentReply.like.isEnabled = recentCommentMyPost.not()
                    binder.commentReply.like.alpha = if (recentCommentMyPost) 0.5F else 1F
                    binder.commentReply.ivOptionMore.isVisible = recentCommentMyPost.not()
                    binder.commentReply.ivHelpful.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (recentComment.markedHelpful) R.drawable.ic_helpful else R.drawable.ic_like_24))
                    binder.commentReply.ivUser.loadImage(recentComment.user?.name, recentComment.user?.avatar?.value)
                    binder.commentReply.ivVerified.isVisible = recentComment.user?.followed == true
                    binder.commentReply.postedAt.text = recentComment.postedAt
                    binder.commentReply.tvDiscussionDetails.threadDescription(recentComment.body)
                    binder.commentReply.tvLikeCount.text = recentComment.helpfulCount.toString()
                    binder.commentReply.tvUserName.text = if(recentCommentMyPost) getString(R.string.you_replied) else recentComment.user?.name

                    binder.commentReply.like.setOnClickListener {
                        model.id?.let { commentId ->
                            recentComment.id?.let {
                                recentComment.helpfulCount = if (recentComment.markedHelpful) (recentComment.helpfulCount - 1) else (recentComment.helpfulCount + 1)
                                recentComment.markedHelpful = recentComment.markedHelpful.not()
                                adapter.notifyDataSetChanged()
                                viewModel.replyLikeUnlike(viewModel.discussionId, commentId.toString(), it, model.markedHelpful ?: false).observe(viewLifecycleOwner) {
                                }
                            }
                        }
                    }

                    binder.commentReply.ivOptionMore.setOnClickListener {
                        recentComment.id?.let { discussionId ->
                            recentComment.user?.id?.let { userId ->
                                CommentsOptionsDialog(discussionId, userId) {
                                    getComments(viewModel.discussionId)
                                }.show(childFragmentManager, getString(R.string.add_a_link))
                            }
                        }
                    }

                    binder.commentReply.userLayout.setOnClickListener {
                        openUserProfile(Constants.DISCUSSION_COMMENT, model.user?.id.toString())
                    }
                }

                val myPost = Preferences.getValue(Constants.USER_ID, "") == model.user?.id.toString()
                binder.like.isClickable = myPost.not()
                binder.like.isEnabled = myPost.not()
                binder.like.alpha = if (myPost) 0.5F else 1F
                binder.tvYouReplied.isVisible = myPost
                binder.tvUserName.isVisible = myPost.not()
                binder.userLayout.gravity = if (myPost) Gravity.END else Gravity.START
                binder.commentBg.background = ContextCompat.getDrawable(requireContext(), if (myPost) R.drawable.bg_my_reply else R.drawable.ic_comment_bg)

                binder.ivHelpful.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (model.markedHelpful == true) R.drawable.ic_helpful else R.drawable.ic_like_24))
                binder.ivOptionMore.isVisible = myPost.not()

                binder.userLayout.setOnClickListener {
                    openUserProfile(Constants.DISCUSSION_COMMENT, model.user?.id.toString())
                }

                binder.addReply.isVisible = viewModel.isMember
                binder.divider.isVisible = viewModel.isMember
                binder.addReply.setOnClickListener {
                    CreateNewReplyDialog(model.body ?: "", viewModel.discussionId, model.id.toString(), model.user?.name.toString()) {
                        getComments(viewModel.discussionId)
                    }.show(childFragmentManager, getString(R.string.new_thread))
                }

                binder.like.setOnClickListener {
                    model.id?.let {
                        model.helpfulCount = if (model.markedHelpful!!) (model.helpfulCount - 1) else (model.helpfulCount + 1)
                        model.markedHelpful = model.markedHelpful!!.not()
                        adapter.notifyDataSetChanged()
                        viewModel.commentLikeUnlike(viewModel.discussionId, it.toString(), model.markedHelpful ?: false).observe(viewLifecycleOwner) {
                        }
                    }
                }

                binder.root.setOnClickListener {
                    openDiscussionReplies(Constants.GROUP_DISCUSSIONS_DETAILS, viewModel.discussionId, model.id.toString(), viewModel.isMember)
                }

                binder.tvDiscussionDetails.setOnClickListener {
                    openDiscussionReplies(Constants.GROUP_DISCUSSIONS_DETAILS, viewModel.discussionId, model.id.toString(), viewModel.isMember)
                }

                binder.executePendingBindings()

            }
        })
        binding?.rvComments?.adapter = adapter

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
                        getComments(viewModel.discussionId)
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
                getComments(viewModel.discussionId)
            }
        }

        binding?.paginationView?.ivForward?.setOnClickListener {
            var currentPage = viewModel.currentPage.toInt()
            if (currentPage < viewModel.totalPage) {
                currentPage += 1
                viewModel.currentPage = currentPage.toString()
                pagerAdapter.notifyDataSetChanged()
                getComments(viewModel.discussionId)
            }
        }

        binding?.ivScrollUp?.setOnClickListener {
            binding?.parent?.smoothScrollTo(0, 0)
        }

        binding?.like?.setOnClickListener {
            viewModel.discussion.value?.let { disucssion ->
                disucssion.id?.let {
                    disucssion.helpfulCount = if (disucssion.markedHelpful!!) (disucssion.helpfulCount - 1) else (disucssion.helpfulCount + 1)
                    disucssion.markedHelpful = disucssion.markedHelpful!!.not()
                    viewModel.discussion.value = disucssion
                    binding?.discussions = disucssion
                    binding?.ivHelpful?.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (disucssion.markedHelpful == true) R.drawable.ic_helpful else R.drawable.ic_like_24))

                    viewModel.discussionLikeUnlike(it, disucssion.markedHelpful ?: false).observe(viewLifecycleOwner) {

                    }
                }
            }
        }

        binding?.btnAddComment?.isVisible = viewModel.isMember
        binding?.btnAddComment?.setOnClickListener {
            viewModel.discussion.value?.let { disucssion ->
                CreateNewCommentDialog(disucssion.title ?: "", viewModel.discussionId) {
                    getComments(viewModel.discussionId)
                }.show(childFragmentManager, getString(R.string.new_thread))
            }
        }

        binding?.userLayout?.setOnClickListener {
            viewModel.discussion.value?.user?.id?.let { id ->
                openUserProfile(Constants.DISCUSSION_COMMENT, id.toString())
            }
        }

        binding?.ivFilter?.setOnClickListener {
            SortCommentsDialog {
                viewModel.currentPage = "1"
                pagerAdapter.notifyDataSetChanged()
                getComments(viewModel.discussionId)
            }.show(childFragmentManager, getString(R.string.filters_and_sort))
        }

        binding?.ivOptionMore?.setOnClickListener {
            viewModel.discussion.value?.user?.id?.let { userId ->
                DiscussionOptionsDialog(viewModel.discussionId.toInt(), userId) {
                    activity?.onBackPressed()
                }.show(childFragmentManager, getString(R.string.add_a_link))
            }
        }
    }


    private fun bindObservers() {


        viewModel.getDiscussion(viewModel.discussionId).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    it.data?.data?.discussion?.let {
                        viewModel.discussion.value = it
                        binding?.discussions = it
                        updateUI(it)
                    }
                }
            }
        }
        getComments(viewModel.discussionId)
    }

    private fun updateUI(disucssion: Discussions) {
        if (activity is MainActivity) {
            (activity as MainActivity).toolbarHome?.toolbarTitle?.text = disucssion.title
        }

        binding?.ivHelpful?.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (disucssion.markedHelpful == true) R.drawable.ic_helpful else R.drawable.ic_like_24))
        binding?.ivUser?.loadImage(disucssion.user?.name, disucssion.user?.avatar?.value)
        binding?.ivVerified?.isVisible = disucssion.user?.followed == true
        binding?.tvTime?.text = disucssion.postedAt

        binding?.tags?.setClickableTagsOrBadges(disucssion.tags, true) {
            var tagId = 0
            disucssion.tags.forEach { model ->
                if (model.text == it) {
                    tagId = model.id ?: 0
                }
            }
            openTagDetails(Constants.GROUP_DISCUSSIONS_DETAILS, it, tagId)
        }

        val myPost = Preferences.getValue(Constants.USER_ID, "") == disucssion.user?.id.toString()
        binding?.like?.isClickable = myPost.not()
        binding?.like?.isEnabled = myPost.not()
        binding?.like?.alpha = if (myPost) 0.5F else 1F

        binding?.ivOptionMore?.isVisible = myPost.not()

        disucssion.followed?.let {
            updateBookmarkIcon(it)
        }
        binding?.responses?.text = getString(R.string.responses, disucssion.commentCount.toString())
        if (disucssion.commentCount - 2 > 0) {
            binding?.tvCommenterCount?.text = "+${min(disucssion.commentCount - 2, 99)}"
            binding?.flUser3?.isVisible = true
        }
        disucssion.recentCommenters.forEachIndexed { index, element ->
            when (index) {
                0 -> {
                    binding?.ivUser1?.loadImage(element.name, element.avatar?.value)
                    binding?.ivUser1?.isVisible = true
                }
                1 -> {
                    binding?.ivUser2?.loadImage(element.name, element.avatar?.value)
                    binding?.ivUser2?.isVisible = true
                }
            }
        }

        binding?.filterLayout?.isVisible = true
        binding?.ivScrollUp?.isVisible = true
        binding?.rvComments?.isVisible = true
        binding?.parent?.isVisible = true
    }

    private fun getComments(disucssionId: String) {
        viewModel.getComments(disucssionId).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    it.data?.let {
                        it.data?.comments?.let {
                            adapter.submitList(it) {
                                if (viewModel.currentPage != "1") {
                                    binding?.llHeaders?.postDelayed({
                                        binding?.llHeaders?.height?.let {
                                            Log.e("llHeaders", "-->>$it")
                                            binding?.parent?.smoothScrollTo(0, it)
                                        }
                                    }, 100)
                                }
                            }
                        }
                        it.meta?.let { meta ->
                            meta.totalEntries?.let { total ->
                                binding?.responses?.text = getString(R.string.responses, total.toString())
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

    private fun updateBookmarkIcon(isFollowing: Boolean) {
        toolbarBinding?.ivClose?.setImageResource(if (isFollowing) R.drawable.ic_bookmark_added else R.drawable.ic_bookmark_add)
        toolbarBinding?.ivMessaging?.visibility = View.GONE
        toolbarBinding?.ivNotification?.visibility = View.GONE
        toolbarBinding?.ivClose?.visibility = View.VISIBLE
        toolbarBinding?.llRight?.visibility = View.VISIBLE

        toolbarBinding?.ivClose?.setOnClickListener {
            callFollowUnFollowAPI()
        }
    }

    private fun callFollowUnFollowAPI() {

        viewModel.discussion.value?.id?.let { disucssionId ->
            viewModel.discussion.value?.followed?.let { following ->
                if (following) {
                    viewModel.unfollowDiscussions(disucssionId.toString())
                } else {
                    viewModel.followDiscussions(disucssionId.toString())
                }.observe(viewLifecycleOwner) {
                    when {
                        it.status.isLoading() -> {
                            showProgress()
                        }
                        it.status.isSuccessful() -> {
                            hideProgress()
                            val discussion = viewModel.discussion.value
                            discussion?.followed = following.not()
                            viewModel.discussion.postValue(discussion)
                            updateBookmarkIcon(following.not())
                        }
                        it.status.isError() -> {
                            hideProgress()
                        }
                    }
                }
            }
        }
    }

    private fun removeBookmarkIcon() {
        toolbarBinding?.ivClose?.setImageResource(R.drawable.ic_close_dark_green)
        toolbarBinding?.ivMessaging?.visibility = View.VISIBLE
        toolbarBinding?.ivNotification?.visibility = View.VISIBLE
        toolbarBinding?.ivClose?.visibility = View.GONE
        toolbarBinding?.llRight?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        removeBookmarkIcon()
    }

    override fun onStop() {
        super.onStop()
        removeBookmarkIcon()
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
        updateToolbar(getString(R.string.discussions),
            showBackArrow = true,
            hideRightIcon = true)
    }
}