package plm.patientslikeme2.ui.main.view.fragment.following

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.following.FollowingDiscussionModel
import plm.patientslikeme2.data.model.following.FollowingDiscussionResponse
import plm.patientslikeme2.databinding.FragmentFollowingDiscussionBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowFollowingDiscussionsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyListAdapter
import plm.patientslikeme2.ui.main.viewmodel.following.FollowingViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.*

@AndroidEntryPoint
class FollowingDiscussionsFragment : BaseFragment<FragmentFollowingDiscussionBinding>() {

    private val viewModel: FollowingViewModel by activityViewModels()
    private lateinit var mAdapter: GenericLazyListAdapter<FollowingDiscussionModel, RowFollowingDiscussionsBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFollowingDiscussionBinding =
        FragmentFollowingDiscussionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun getFollowingDiscussion() {
        viewModel.discussionPageNo++
        if (isConnected()) {
            bindObservers()
        } else if (viewModel.discussionPageNo == 1) {
            showEmptyView(getString(R.string.error_internet))
        }
    }

    private fun initView() {
        mAdapter = GenericLazyListAdapter(
            R.layout.row_following_discussions,
            object :
                GenericLazyBindingInterface<RowFollowingDiscussionsBinding, FollowingDiscussionModel> {
                @SuppressLint("SetTextI18n")
                override fun bindData(
                    binder: RowFollowingDiscussionsBinding,
                    model: FollowingDiscussionModel,
                    position: Int
                ) {
                    binder.model = model
                    binder.parent.setOnClickListener {
                        openGroupDiscussionDetails(
                            Constants.FOLLOWING_DISCUSSION,
                            model.id.toString(), isMember = true
                        )
                    }
                    binder.cardView.setOnClickListener {
                        openGroupDiscussionDetails(
                            Constants.FOLLOWING_DISCUSSION,
                            model.id.toString(), isMember = true
                        )
                    }
                    binder.tvDescription.setOnClickListener {
                        openGroupDiscussionDetails(
                            Constants.FOLLOWING_DISCUSSION,
                            model.id.toString(), isMember = true
                        )
                    }
                    binder.ivDelete.setOnClickListener {
                        showDeleteTagDialog(model)
                    }
                    if (model.featured) {
                        binder.tvFeature.visibility = View.VISIBLE
                    } else {
                        binder.tvFeature.visibility = View.GONE
                    }
                    if (model.group_name.isEmpty()) {
                        binder.tvGroupName.visibility = View.GONE
                    } else {
                        binder.tvGroupName.text = "in ${model.group_name} group"
                        binder.tvGroupName.visibility = View.VISIBLE
                    }
                    if (model.showDelete) {
                        binder.ivDelete.visibility = View.VISIBLE
                    } else {
                        binder.ivDelete.visibility = View.GONE
                    }
                    binder.tvDescription.setHtmlText(model.subject)
                }
            })
        binding?.rvDiscussions?.adapter = mAdapter

        binding?.parent?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                getFollowingDiscussion()
            }
        })

        viewModel.discussionFragment.observe(viewLifecycleOwner) {
            when (it) {
                1 -> hideDeleteIcons()
                2 -> showDeleteIcons()
            }
        }

        viewModel.discussionResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    updateDataInList(it.data)
                }
            }
            checkDataStatus()
        }
    }

    private fun showDeleteTagDialog(model: FollowingDiscussionModel) {
        activity?.let {
            showActionDialog(it,
                getString(R.string.delete),
                getString(R.string.are_you_sure_unfollow_discussion),
                getString(R.string.yes),
                getString(R.string.no),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        removeDiscussionFromList(model)
                        callUnfollowDiscussionAPI(model)
                    }
                })
        }
    }

    private fun callUnfollowDiscussionAPI(model: FollowingDiscussionModel) {
        viewModel.postUnFollowDiscussion(model.id.toString()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    hideProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun hideDeleteIcons() {
        val itemList: ArrayList<FollowingDiscussionModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        for ((index, model) in itemList.withIndex()) {
            model.showDelete = false
            itemList[index] = model
        }
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()
    }

    private fun showDeleteIcons() {
        val itemList: ArrayList<FollowingDiscussionModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        for ((index, model) in itemList.withIndex()) {
            model.showDelete = true
            itemList[index] = model
        }
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()
    }

    private fun removeDiscussionFromList(item: FollowingDiscussionModel) {
        val itemList: ArrayList<FollowingDiscussionModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        itemList.remove(item)
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()

        checkResourceIsEmpty()
    }

    private fun checkResourceIsEmpty() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (mAdapter.itemCount <= 0) {
                showEmptyView(getString(R.string.no_following_discussions))
            }
        }, 200)
    }

    private fun bindObservers() {
        viewModel.getFollowingDiscussion(viewModel.discussionPageNo).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (viewModel.discussionPageNo == 1) {
                        showProgress()
                    }
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    updateDataInList(it.data)
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }
    }

    private fun updateDataInList(data: FollowingDiscussionResponse?) {
        if ((data?.data?.discussions?.size ?: 0) > 0) {
            if (viewModel.discussionPageNo == 1) {
                val list = data?.data?.discussions
                mAdapter.submitList(list)
            } else if ((data?.data?.discussions?.size ?: 0) > 0) {
                val itemList: ArrayList<FollowingDiscussionModel> = arrayListOf()
                itemList.addAll(mAdapter.currentList)
                itemList.addAll(data?.data?.discussions ?: arrayListOf())
                mAdapter.submitList(itemList)
            }
            binding?.parent?.visibility = View.VISIBLE
        } else if (viewModel.discussionPageNo == 1) {
            showEmptyView(getString(R.string.no_following_discussions))
        }
    }

    private fun checkDataStatus() {
        when (viewModel.discussionResponse.value?.status) {
            Status.LOADING -> {
                showProgress()
            }
            Status.SUCCESS -> {
                hideProgress()
            }
            else -> {
                hideProgress()
                showEmptyView(getString(R.string.error_api))
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

    override fun onResume() {
        super.onResume()

        toolbarBinding?.btnAction?.text = getString(R.string.edit)
        if (mAdapter.itemCount > 0) {
            hideDeleteIcons()
        }
    }
}