package plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.community.resource.ResourceModel
import plm.patientslikeme2.databinding.FragmentGroupResourcesBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowGroupResourcesBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.dialog.community.group.FilterGroupResourceDialog
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.openResourceDetails
import java.util.*


@AndroidEntryPoint
class GroupResourcesFragment : BaseFragment<FragmentGroupResourcesBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()
    private lateinit var mAdapter: GenericListAdapter<ResourceModel, RowGroupResourcesBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentGroupResourcesBinding =
        FragmentGroupResourcesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.group_fragment

        initView()
        if (isConnected()) {
            bindObservers()
        } else {
            showEmptyView(getString(R.string.error_internet))
        }
    }

    private fun initView() {
        mAdapter = GenericListAdapter(R.layout.row_group_resources,
            object : GenericRecyclerBindingInterface<RowGroupResourcesBinding, ResourceModel> {
                @SuppressLint("SetTextI18n")
                override fun bindData(
                    binder: RowGroupResourcesBinding, model: ResourceModel, position: Int
                ) {
                    binder.model = model
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
                    binder.cardView.setOnClickListener {
                        openResourceDetails(
                            Constants.GROUP_RESOURCE,
                            model.id,
                            model.headline
                        )
                    }
                }
            })
        binding?.rvResources?.adapter = mAdapter

        binding?.ivFilter?.setOnClickListener {
            FilterGroupResourceDialog {
                if (viewModel.filterAllResources) {
                    fetchAllGroupResources()
                } else if (viewModel.filterResourcesImFollowing) {
                    fetchImFollowingResources()
                }
            }.show(childFragmentManager, getString(R.string.filters_and_sort))
        }
    }

    private fun fetchAllGroupResources() {
        viewModel.getGroupResource().observe(viewLifecycleOwner) {
            viewModel.resourceResponse.postValue(it)
        }
    }

    private fun fetchImFollowingResources() {
        viewModel.getGroupFilterResource().observe(viewLifecycleOwner) {
            viewModel.resourceResponse.postValue(it)
        }
    }

    private fun bindObservers() {
        viewModel.resourceResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.data?.resources?.size == 0) {
                        showEmptyView(getString(R.string.no_resources_found))
                    } else {
                        mAdapter.submitList(it.data?.data?.resources)
                        binding?.rlFilter?.visibility = View.VISIBLE
                        binding?.rvResources?.visibility = View.VISIBLE
                    }
                }
            }
            checkDataStatus()
        }
    }

    private fun checkDataStatus() {
        if (viewModel.resourceResponse.value?.status == Status.LOADING) {
            showProgress()
        } else if (viewModel.resourceResponse.value?.status == Status.SUCCESS) {
            hideProgress()
        } else if (viewModel.resourceResponse.value?.status == Status.ERROR) {
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
            binding?.rlFilter?.visibility = View.GONE
            binding?.rvResources?.visibility = View.GONE
        }
    }
}