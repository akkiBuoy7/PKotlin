package plm.patientslikeme2.ui.main.view.fragment.following

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.following.TagDetailsModel
import plm.patientslikeme2.databinding.*
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyListAdapter
import plm.patientslikeme2.ui.main.viewmodel.following.FollowingViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.openGroupDiscussionDetails
import plm.patientslikeme2.utils.extensions.setHtmlText

@AndroidEntryPoint
class TagDetailsFragment : BaseFragment<FragmentTagDetailsBinding>() {

    private val viewModel: FollowingViewModel by activityViewModels()
    private lateinit var mAdapter: GenericLazyListAdapter<TagDetailsModel, RowTagDetailsBinding>
    private lateinit var tagName: String
    private var tagId: Int = 0

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentTagDetailsBinding =
        FragmentTagDetailsBinding.inflate(inflater, container, false)

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        args?.let {
            tagName = args.getString(Constants.NAME).toString()
            tagId = args.getInt(Constants.ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.tag_details_fragment

        initView()
        getTagDetailsList()
    }

    private fun getTagDetailsList() {
        mAdapter.pageNo++
        if (isConnected()) {
            bindObservers()
        } else if (mAdapter.pageNo == 1) {
            showEmptyView(getString(R.string.error_internet))
        }
    }

    private fun initView() {
        mAdapter = GenericLazyListAdapter(
            R.layout.row_tag_details,
            object :
                GenericLazyBindingInterface<RowTagDetailsBinding, TagDetailsModel> {
                @SuppressLint("SetTextI18n")
                override fun bindData(
                    binder: RowTagDetailsBinding,
                    model: TagDetailsModel,
                    position: Int,
                ) {
                    binder.model = model
                    binder.cardView.setOnClickListener {
                        openGroupDiscussionDetails(Constants.TAG_DETAILS, model.id.toString())
                    }
                    binder.tvFeature.setOnClickListener {
                        openGroupDiscussionDetails(Constants.TAG_DETAILS, model.id.toString())
                    }
                    binder.tvGroupName.setOnClickListener {
                        openGroupDiscussionDetails(Constants.TAG_DETAILS, model.id.toString())
                    }
                    binder.tvDescription.setOnClickListener {
                        openGroupDiscussionDetails(Constants.TAG_DETAILS, model.id.toString())
                    }
                    if (model.featured) {
                        binder.tvFeature.visibility = View.VISIBLE
                    } else {
                        binder.tvFeature.visibility = View.GONE
                    }
                    if (model.followed) {
                        binder.llFollowing.visibility = View.VISIBLE
                        binder.llFollow.visibility = View.GONE
                    } else {
                        binder.llFollowing.visibility = View.GONE
                        binder.llFollow.visibility = View.VISIBLE
                    }
                    if (model.group_name.isEmpty()) {
                        binder.tvGroupName.visibility = View.GONE
                    } else {
                        binder.tvGroupName.text = "in ${model.group_name} group"
                        binder.tvGroupName.visibility = View.VISIBLE
                    }
                    binder.tvDescription.setHtmlText(model.title)

                    binder.llFollow.setOnClickListener {
                        binder.llFollowing.visibility = View.VISIBLE
                        binder.llFollow.visibility = View.GONE
                        followTagDiscussion(model)
                    }
                }
            })
        binding?.rvDiscussions?.adapter = mAdapter

        binding?.parent?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                getTagDetailsList()
            }
        })
    }

    private fun followTagDiscussion(model: TagDetailsModel) {
        viewModel.postFollowTagDetailsDiscussion(model.id.toString()).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    hideProgress()
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun bindObservers() {
        viewModel.getTagDetailsList(tagName, mAdapter.pageNo).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (mAdapter.pageNo == 1) {
                        showProgress()
                    }
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    updateBookmarkIcon(it.data?.data?.followed)
                    if ((it.data?.data?.discussions?.size ?: 0) > 0) {
                        if (mAdapter.pageNo == 1) {
                            val list = it.data?.data?.discussions
                            mAdapter.submitList(list)
                        } else if ((it.data?.data?.discussions?.size ?: 0) > 0) {
                            val itemList: ArrayList<TagDetailsModel> = arrayListOf()
                            itemList.addAll(mAdapter.currentList)
                            itemList.addAll(it.data?.data?.discussions ?: arrayListOf())
                            mAdapter.submitList(itemList)
                        }
                        binding?.parent?.visibility = View.VISIBLE
                    } else if (mAdapter.pageNo == 1) {
                        showEmptyView(getString(R.string.no_following_discussions))
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }
    }

    private fun callFollowTagAPI() {
        viewModel.postFollowTag(tagId.toString()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    showSuccessErrorLayout(true, it.data?.message.toString())
                    updateBookmarkIcon(true)
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun callUnFollowTagAPI() {
        viewModel.postUnFollowTag(tagId.toString()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    showSuccessErrorLayout(true, it.data?.message.toString())
                    updateBookmarkIcon(false)
                }
                it.status.isError() -> {
                    hideProgress()
                }
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

    private fun updateBookmarkIcon(followed: Boolean?) {
        followed?.let { follow ->
            if (follow) {
                toolbarBinding?.ivClose?.setImageResource(R.drawable.ic_bookmark_added)
            } else {
                toolbarBinding?.ivClose?.setImageResource(R.drawable.ic_bookmark_add)
            }
            toolbarBinding?.ivMessaging?.visibility = View.GONE
            toolbarBinding?.ivNotification?.visibility = View.GONE
            toolbarBinding?.ivClose?.visibility = View.VISIBLE
            toolbarBinding?.llRight?.visibility = View.VISIBLE

            toolbarBinding?.ivClose?.setOnClickListener {
                if (follow) {
                    callUnFollowTagAPI()
                } else {
                    callFollowTagAPI()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar("Tagged “$tagName”", showBackArrow = true, hideRightIcon = true)
    }

    override fun onPause() {
        super.onPause()
        removeOptionMoreIcon()
    }

    override fun onStop() {
        super.onStop()
        removeOptionMoreIcon()
    }
}