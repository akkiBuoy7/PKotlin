package plm.patientslikeme2.ui.main.view.fragment.community.groups

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.community.groups.about.GroupAboutModel
import plm.patientslikeme2.data.model.community.groups.suggestedgroups.SuggestedGroup
import plm.patientslikeme2.databinding.FragmentAboutTheGroupBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowCommunitySuggestedGroupsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.enum.GroupUpdateType
import plm.patientslikeme2.utils.extensions.getUpperFirstChar
import plm.patientslikeme2.utils.extensions.loadImage
import plm.patientslikeme2.utils.extensions.openGroupDetails

@AndroidEntryPoint
class AboutTheGroupFragment : BaseFragment<FragmentAboutTheGroupBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()
    private lateinit var adapter: GenericListAdapter<SuggestedGroup, RowCommunitySuggestedGroupsBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAboutTheGroupBinding =
        FragmentAboutTheGroupBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.about_the_group_fragment
        initView()
        if (isConnected()) bindObservers() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initView() {
        adapter = GenericListAdapter(R.layout.row_community_suggested_groups, object :
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
                    openGroupDetails(Constants.ABOUT_GROUP, model.id.toString(), model.restricted)
                }
                binder.executePendingBindings()
            }

        })
        binding?.rvRelatedGroups?.adapter = adapter
    }

    private fun bindObservers() {
        viewModel.aboutGroupResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    it.data?.let {
                        binding?.aboutGroupResponse = it
                        updateGroupDetails(it)
                        it.data?.suggestedGroups?.let {
                            adapter.submitList(it)
                        }
                    }
                }
            }
            checkDataStatus()
        }

        checkDataStatus()
    }

    private fun updateGroupDetails(it: GroupAboutModel) {
        binding?.tvGroupName?.text = it.data?.name.toString()
        if (!TextUtils.isEmpty(it.data?.description)) {
            binding?.tvGroupDesc?.text = it.data?.description.toString()
            binding?.tvGroupDesc?.visibility = View.VISIBLE
        } else {
            binding?.tvGroupDesc?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(it.data?.moderator?.name)) {
            binding?.ivUser?.loadImage(
                it.data?.moderator?.name.toString(),
                it.data?.moderator?.profilePic.toString()
            )
            binding?.tvUserName?.getUpperFirstChar(it.data?.moderator?.name.toString())
            binding?.llModerate?.visibility = View.VISIBLE
        } else {
            binding?.llModerate?.visibility = View.GONE
        }
    }

    private fun checkDataStatus() {
        if (viewModel.aboutGroupResponse.value?.status == Status.LOADING) {
            showProgress()
        } else if (viewModel.aboutGroupResponse.value?.status == Status.SUCCESS) {
            hideProgress()
        } else if (viewModel.aboutGroupResponse.value?.status == Status.ERROR) {
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

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.about_the_group), true, true)
    }

    private fun updateGroupInfo(id: Int, position: Int, updateType: GroupUpdateType) {
        if (updateType == GroupUpdateType.JOIN) {
            viewModel.joinGroup(id)
        } else {
            viewModel.unJoinGroup(id)
        }.observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    val listItem = adapter.currentList[position]
                    listItem.isJoined = updateType == GroupUpdateType.JOIN
                    adapter.notifyItemChanged(position)
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }
}