package plm.patientslikeme2.ui.main.view.fragment.following

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.community.resource.ResourceModel
import plm.patientslikeme2.data.model.following.FollowingResourceResponse
import plm.patientslikeme2.databinding.FragmentFollowingResourcesBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowFollowingResourcesBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyListAdapter
import plm.patientslikeme2.ui.main.viewmodel.following.FollowingViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.ActionDialogListener
import plm.patientslikeme2.utils.extensions.openResourceDetails
import plm.patientslikeme2.utils.extensions.showActionDialog
import java.util.*

@AndroidEntryPoint
class FollowingResourcesFragment : BaseFragment<FragmentFollowingResourcesBinding>() {

    private val viewModel: FollowingViewModel by activityViewModels()
    private lateinit var mAdapter: GenericLazyListAdapter<ResourceModel, RowFollowingResourcesBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFollowingResourcesBinding =
        FragmentFollowingResourcesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun getFollowingResourceDiscussion() {
        viewModel.resourcePageNo++
        if (isConnected()) {
            bindObservers()
        } else if (viewModel.resourcePageNo == 1) {
            showEmptyView(getString(R.string.error_internet))
        }
    }

    private fun initView() {
        mAdapter = GenericLazyListAdapter(R.layout.row_following_resources, object :
            GenericLazyBindingInterface<RowFollowingResourcesBinding, ResourceModel> {
            @SuppressLint("SetTextI18n")
            override fun bindData(
                binder: RowFollowingResourcesBinding, model: ResourceModel, position: Int
            ) {
                binder.model = model
                if (TextUtils.isEmpty(model.promo_image)) {
                    binder.ivBanner.visibility = View.GONE
                } else {
                    binder.ivBanner.visibility = View.VISIBLE
                }
                if (model.showDelete) {
                    binder.ivDelete.visibility = View.VISIBLE
                } else {
                    binder.ivDelete.visibility = View.GONE
                }
                if (model.content_type.equals("video", true)) {
                    binder.ivPlay.visibility = View.VISIBLE
                } else {
                    binder.ivPlay.visibility = View.GONE
                }
                when (model.content_type) {
                    "Video" -> {
                        binder.tvContentType.text = model.content_type
                    }
                    "Article" -> binder.tvContentType.text =
                        "Short ${model.content_type.lowercase(Locale.getDefault())}"
                    else -> binder.tvContentType.text = model.content_type
                }
                binder.ivDelete.setOnClickListener {
                    showDeleteTagDialog(model)
                }
                binder.cardView.setOnClickListener {
                    openResourceDetails(Constants.FOLLOWING_RESOURCE, model.id, model.headline)
                }
                binder.executePendingBindings()
            }
        })
        binding?.rvResources?.adapter = mAdapter

        binding?.parent?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                getFollowingResourceDiscussion()
            }
        })

        viewModel.resourceFragment.observe(viewLifecycleOwner) {
            when (it) {
                1 -> hideDeleteIcons()
                2 -> showDeleteIcons()
            }
        }

        viewModel.resourceResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    updateDataInList(it.data)
                }
            }
            checkDataStatus()
        }
    }

    private fun showDeleteTagDialog(model: ResourceModel) {
        activity?.let {
            showActionDialog(it,
                getString(R.string.delete),
                getString(R.string.are_you_sure_delete),
                getString(R.string.yes),
                getString(R.string.no),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        removeDiscussionFromList(model)
                        callUnfollowResourceAPI(model)
                    }
                })
        }
    }

    private fun callUnfollowResourceAPI(model: ResourceModel) {
        viewModel.postUnFollowResource(model.id.toString()).observe(viewLifecycleOwner) {
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
        val itemList: ArrayList<ResourceModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        for ((index, model) in itemList.withIndex()) {
            model.showDelete = false
            itemList[index] = model
        }
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()
    }

    private fun showDeleteIcons() {
        val itemList: ArrayList<ResourceModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        for ((index, model) in itemList.withIndex()) {
            model.showDelete = true
            itemList[index] = model
        }
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()
    }

    private fun removeDiscussionFromList(item: ResourceModel) {
        val itemList: ArrayList<ResourceModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        itemList.remove(item)
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()

        checkResourceIsEmpty()
    }

    private fun checkResourceIsEmpty() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (mAdapter.itemCount <= 0) {
                showEmptyView(getString(R.string.no_following_resources))
            }
        }, 200)
    }

    private fun bindObservers() {
        viewModel.getFollowingResources(viewModel.resourcePageNo).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (viewModel.resourcePageNo == 1) {
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

    private fun updateDataInList(data: FollowingResourceResponse?) {
        if ((data?.data?.resources?.size ?: 0) > 0) {
            if (viewModel.resourcePageNo == 1) {
                val list = data?.data?.resources
                mAdapter.submitList(list)
            } else if ((data?.data?.resources?.size ?: 0) > 0) {
                val itemList: ArrayList<ResourceModel> = arrayListOf()
                itemList.addAll(mAdapter.currentList)
                itemList.addAll(data?.data?.resources ?: arrayListOf())
                mAdapter.submitList(itemList)
            }
            binding?.parent?.visibility = View.VISIBLE
        } else if (viewModel.resourcePageNo == 1) {
            showEmptyView(getString(R.string.no_following_resources))
        }
    }

    private fun checkDataStatus() {
        when (viewModel.resourceResponse.value?.status) {
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