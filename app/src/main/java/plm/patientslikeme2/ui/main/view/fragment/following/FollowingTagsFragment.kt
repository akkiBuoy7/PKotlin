package plm.patientslikeme2.ui.main.view.fragment.following

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
import plm.patientslikeme2.data.model.following.FollowingTagModel
import plm.patientslikeme2.data.model.following.FollowingTagResponse
import plm.patientslikeme2.databinding.FragmentFollowingTagsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowFollowingTagsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyListAdapter
import plm.patientslikeme2.ui.main.viewmodel.following.FollowingViewModel
import plm.patientslikeme2.utils.Constants.FOLLOWING_TAG
import plm.patientslikeme2.utils.extensions.ActionDialogListener
import plm.patientslikeme2.utils.extensions.openTagDetails
import plm.patientslikeme2.utils.extensions.showActionDialog

@AndroidEntryPoint
class FollowingTagsFragment : BaseFragment<FragmentFollowingTagsBinding>() {

    private val viewModel: FollowingViewModel by activityViewModels()
    private lateinit var mAdapter: GenericLazyListAdapter<FollowingTagModel, RowFollowingTagsBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFollowingTagsBinding =
        FragmentFollowingTagsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun getFollowingTags() {
        viewModel.tagPageNo++
        if (isConnected()) {
            bindObservers()
        } else if (viewModel.tagPageNo == 1) {
            showEmptyView(getString(R.string.error_internet))
        }
    }

    private fun initView() {
        mAdapter = GenericLazyListAdapter(
            R.layout.row_following_tags, object :
                GenericLazyBindingInterface<RowFollowingTagsBinding, FollowingTagModel> {
                override fun bindData(
                    binder: RowFollowingTagsBinding, model: FollowingTagModel, position: Int
                ) {
                    binder.model = model

                    if (model.showDelete) {
                        binder.ivNext.visibility = View.GONE
                        binder.ivDelete.visibility = View.VISIBLE
                    } else {
                        binder.ivNext.visibility = View.VISIBLE
                        binder.ivDelete.visibility = View.GONE
                    }
                    binder.ivDelete.setOnClickListener {
                        showDeleteTagDialog(model)
                    }
                    binder.parent.setOnClickListener {
                        openTagDetails(FOLLOWING_TAG, model.text, model.id)
                    }
                    binder.executePendingBindings()
                }
            })
        binding?.rvTags?.adapter = mAdapter

        binding?.parent?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                getFollowingTags()
            }
        })

        viewModel.tagFragment.observe(viewLifecycleOwner) {
            when (it) {
                1 -> hideDeleteIcons()
                2 -> showDeleteIcons()
            }
        }

        viewModel.tagResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    updateDataInList(it.data)
                }
            }
            checkDataStatus()
        }
    }

    private fun showDeleteTagDialog(model: FollowingTagModel) {
        activity?.let {
            showActionDialog(it,
                getString(R.string.delete),
                getString(R.string.are_you_sure_delete) + " " + model.text,
                getString(R.string.yes),
                getString(R.string.no),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        removeTagFromList(model)
                        callUnfollowTagAPI(model)
                    }
                })
        }
    }

    private fun callUnfollowTagAPI(model: FollowingTagModel) {
        viewModel.postUnFollowTag(model.id.toString()).observe(viewLifecycleOwner) {
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
        val itemList: ArrayList<FollowingTagModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        for ((index, model) in itemList.withIndex()) {
            model.showDelete = false
            itemList[index] = model
        }
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()
    }

    private fun showDeleteIcons() {
        val itemList: ArrayList<FollowingTagModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        for ((index, model) in itemList.withIndex()) {
            model.showDelete = true
            itemList[index] = model
        }
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()
    }

    private fun removeTagFromList(tagModel: FollowingTagModel) {
        val itemList: ArrayList<FollowingTagModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        itemList.remove(tagModel)
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()

        checkResourceIsEmpty()
    }

    private fun checkResourceIsEmpty() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (mAdapter.itemCount <= 0) {
                showEmptyView(getString(R.string.no_following_tag))
            }
        }, 200)
    }

    private fun bindObservers() {
        viewModel.getFollowingTags(viewModel.tagPageNo).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (viewModel.tagPageNo == 1) {
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

    private fun checkDataStatus() {
        when (viewModel.tagResponse.value?.status) {
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

    private fun updateDataInList(data: FollowingTagResponse?) {
        if ((data?.data?.tags?.size ?: 0) > 0) {
            if (viewModel.tagPageNo == 1) {
                val list = data?.data?.tags
                mAdapter.submitList(list)
            } else if ((data?.data?.tags?.size ?: 0) > 0) {
                val itemList: ArrayList<FollowingTagModel> = arrayListOf()
                itemList.addAll(mAdapter.currentList)
                itemList.addAll(data?.data?.tags ?: arrayListOf())
                mAdapter.submitList(itemList)
            }
            binding?.parent?.visibility = View.VISIBLE
        } else if (viewModel.tagPageNo == 1) {
            showEmptyView(getString(R.string.no_following_tag))
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