package plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.community.events.EventModel
import plm.patientslikeme2.databinding.FragmentGroupEventsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowEventsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.openEventsDetails

@AndroidEntryPoint
class GroupEventsFragment : BaseFragment<FragmentGroupEventsBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()
    private lateinit var mAdapter: GenericListAdapter<EventModel, RowEventsBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentGroupEventsBinding =
        FragmentGroupEventsBinding.inflate(inflater, container, false)

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
        mAdapter = GenericListAdapter(R.layout.row_events,
            object : GenericRecyclerBindingInterface<RowEventsBinding, EventModel> {
                override fun bindData(
                    binder: RowEventsBinding, model: EventModel, position: Int
                ) {
                    binder.model = model
                    binder.parent.setOnClickListener {
                        openEventsDetails(Constants.GROUP_EVENT, Gson().toJson(model))
                    }
                }
            })
        binding?.rvEvents?.adapter = mAdapter
    }

    private fun bindObservers() {
        viewModel.eventResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.data?.events?.size == 0) {
                        showEmptyView(getString(R.string.no_events_found))
                    } else {
                        mAdapter.submitList(it.data?.data?.events)
                        binding?.rvEvents?.visibility = View.VISIBLE
                    }
                }
            }
            checkDataStatus()
        }
    }

    private fun checkDataStatus() {
        if (viewModel.eventResponse.value?.status == Status.LOADING) {
            showProgress()
        } else if (viewModel.eventResponse.value?.status == Status.SUCCESS) {
            hideProgress()
        } else if (viewModel.eventResponse.value?.status == Status.ERROR) {
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
            binding?.rvEvents?.visibility = View.GONE
        }
    }
}