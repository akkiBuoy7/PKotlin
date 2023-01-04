package plm.patientslikeme2.ui.main.view.fragment.community.groups

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.community.groups.mygroups.MyGroup
import plm.patientslikeme2.data.model.community.groups.mygroups.MyGroupsResponse
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.SuggestedGroup
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.SuggestedGroupResponse
import plm.patientslikeme2.databinding.FragmentCommunityGroupsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowCommunityMyGroupsBinding
import plm.patientslikeme2.databinding.RowCommunitySuggestedGroupsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericHorizontalRecyclerBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericListHorizontalAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.viewmodel.community.CommunityViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.enum.GroupUpdateType
import plm.patientslikeme2.utils.extensions.openGroupDetails

@AndroidEntryPoint
class CommunityGroupsFragment : BaseFragment<FragmentCommunityGroupsBinding>() {

    private val viewModel: CommunityViewModel by activityViewModels()
    private lateinit var suggestedGroupAdapter: GenericListAdapter<SuggestedGroup, RowCommunitySuggestedGroupsBinding>
    private lateinit var myGroupAdapter: GenericListHorizontalAdapter<MyGroup, RowCommunityMyGroupsBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCommunityGroupsBinding =
        FragmentCommunityGroupsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.myGroupsResponse = MyGroupsResponse()
        binding?.suggestedGroupResponse = SuggestedGroupResponse()

        initView()
        bindObservers()
    }

    private fun initView() {
        myGroupAdapter = GenericListHorizontalAdapter(
            R.layout.row_community_my_groups,
            object :
                GenericHorizontalRecyclerBindingInterface<RowCommunityMyGroupsBinding, MyGroup> {
                override fun bindData(
                    binder: RowCommunityMyGroupsBinding,
                    model: MyGroup,
                    position: Int
                ) {
                    binder.model = model
                    binder.root.setOnClickListener {
                        navigateToGroup(model.id.toString(), model.restricted)
                    }
                    binder.executePendingBindings()
                }
            },
            (Resources.getSystem().displayMetrics.widthPixels * 0.7F)
        )
        binding?.rvMyGroups?.adapter = myGroupAdapter

        suggestedGroupAdapter = GenericListAdapter(R.layout.row_community_suggested_groups,
            object :
                GenericRecyclerBindingInterface<RowCommunitySuggestedGroupsBinding, SuggestedGroup> {
                override fun bindData(
                    binder: RowCommunitySuggestedGroupsBinding,
                    model: SuggestedGroup,
                    position: Int
                ) {
                    binder.model = model
                    binder.tvJoin.setOnClickListener {
                        model.id?.let {
                            updateGroupInfo(it, position, GroupUpdateType.JOIN)
                        }
                    }
                    binder.llAction.setOnClickListener {
                        model.id?.let {
                            updateGroupInfo(it, position, GroupUpdateType.LEAVE)
                        }
                    }
                    binder.root.setOnClickListener {
                        navigateToGroup(model.id.toString(), model.restricted)
                    }
                    binder.executePendingBindings()
                }
            })

        binding?.rvSuggestedGroups?.adapter = suggestedGroupAdapter

        binding?.buttonBrowseAllGroups?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_community_to_community_all_groups)
        }
    }

    private fun bindObservers() {
        viewModel.myGroupsResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    binding?.myGroupsResponse = it.data
                    it.data?.data?.myGroups?.let {
                        myGroupAdapter.submitList(it) {
                            myGroupAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            checkDataStatus()
        }

        viewModel.suggestedGroupResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    binding?.suggestedGroupResponse = it.data
                    it.data?.data?.suggestedGroups?.let {
                        suggestedGroupAdapter.submitList(it) {
                            suggestedGroupAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            checkDataStatus()
        }

        checkDataStatus()
    }

    private fun checkDataStatus() {
        if (viewModel.myGroupsResponse.value?.status == Status.LOADING && viewModel.myGroupsResponse.value?.status == Status.LOADING) {
            showProgress()
        } else if (viewModel.myGroupsResponse.value?.status == Status.SUCCESS || viewModel.myGroupsResponse.value?.status == Status.SUCCESS) {
            hideProgress()
        } else if (viewModel.myGroupsResponse.value?.status == Status.ERROR && viewModel.myGroupsResponse.value?.status == Status.ERROR) {
            hideProgress()
            showEmptyView(getString(R.string.error_api))
        }
    }

    private fun updateGroupInfo(id: Int, position: Int, updateType: GroupUpdateType) {
        if (updateType == GroupUpdateType.JOIN) {
            viewModel.joinGroup(id)
        } else {
            viewModel.leaveGroup(id)
        }.observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    val listItem = suggestedGroupAdapter.currentList[position]

                    val updatedSuggestedGroupList: MutableList<SuggestedGroup> = mutableListOf()

                    updatedSuggestedGroupList.addAll(suggestedGroupAdapter.currentList)

                    updatedSuggestedGroupList.find { it.id == listItem.id }?.apply {
                        isJoined = updateType == GroupUpdateType.JOIN
                        stats.members = if (updateType == GroupUpdateType.JOIN) stats.members + 1 else stats.members - 1
                    }
                    val suggestedGroupResponse = viewModel.suggestedGroupResponse.value
                    suggestedGroupResponse?.data?.data?.suggestedGroups = updatedSuggestedGroupList
                    viewModel.suggestedGroupResponse.postValue(suggestedGroupResponse)
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun navigateToGroup(groupId: String, isPrivate: Boolean?) {
        openGroupDetails(Constants.COMMUNITY_GROUP, groupId, isPrivate ?: false)
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