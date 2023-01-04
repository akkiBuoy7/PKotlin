package plm.patientslikeme2.ui.main.view.fragment.community.groups

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.community.allgroups.querygroup.Group
import plm.patientslikeme2.databinding.FragmentCommunityAllGroupsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowCommunityAllGroupsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.viewmodel.community.CommunityGroupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.enum.GroupUpdateType
import plm.patientslikeme2.utils.extensions.OnAutoCompleteItemClickListener
import plm.patientslikeme2.utils.extensions.addHintWithCustomArray
import plm.patientslikeme2.utils.extensions.openGroupDetails


@AndroidEntryPoint
class CommunityAllGroupsFragment : BaseFragment<FragmentCommunityAllGroupsBinding>() {

    private val viewModel: CommunityGroupViewModel by activityViewModels()

    private lateinit var adapter: GenericListAdapter<plm.patientslikeme2.data.model.community.allgroups.Group, RowCommunityAllGroupsBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentCommunityAllGroupsBinding =
        FragmentCommunityAllGroupsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.community_all_groups
        if (isConnected()) fetchAllGroups() else showEmptyView(getString(R.string.error_internet))
        initView()
    }

    private fun queryGroup(search: String) {
        viewModel.queryGroup(search).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data != null) {
                        binding?.acSearchGroup?.addHintWithCustomArray(
                            requireContext(), it.data!!.data.groups as ArrayList<Group>,
                            object : OnAutoCompleteItemClickListener {
                                override fun onItemClick(item: Any?) {
                                    val bundle = Bundle()
                                    bundle.putString(
                                        Constants.GROUP_ID,
                                        (item as Group).id.toString()
                                    )
                                    findNavController().navigate(
                                        R.id.action_community_all_groups_to_group_fragment,
                                        bundle
                                    )
                                    binding?.acSearchGroup?.text = null
                                }
                            }, binding?.acSearchGroup?.text.toString()
                        )
                    }
                }
                it.status.isError() -> {

                }
            }
        }
    }

    private fun initView() {
        adapter = GenericListAdapter(R.layout.row_community_all_groups, object :
            GenericRecyclerBindingInterface<RowCommunityAllGroupsBinding, plm.patientslikeme2.data.model.community.allgroups.Group> {
            override fun bindData(
                binder: RowCommunityAllGroupsBinding,
                model: plm.patientslikeme2.data.model.community.allgroups.Group,
                position: Int
            ) {
                binder.model = model
                binder.tvJoin.setOnClickListener {
                    updateGroupInfo(model.id, position, GroupUpdateType.JOIN)
                }
                binder.llAction.setOnClickListener {
                    updateGroupInfo(model.id, position, GroupUpdateType.LEAVE)
                }

                binder.root.setOnClickListener {
                    openGroupDetails(
                        Constants.COMMUNITY_ALL_GROUP,
                        model.id.toString(),
                        model.restricted
                    )
                }
                binder.executePendingBindings()
            }

        })
        binding?.rvAllGroups?.adapter = adapter

        binding?.acSearchGroup?.addTextChangedListener(object : TextWatcher {
            private val handler = Handler(Looper.getMainLooper())
            private val apiRunnable = Runnable {
                binding?.acSearchGroup?.text?.let{
                    if(it.isNotEmpty()){
                        queryGroup(it.toString())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(apiRunnable)
            }

            override fun afterTextChanged(s: Editable) {
                val sleep = when(s.length) {
                    1 -> 1000L
                    2,3 -> 700L
                    4,5 -> 500L
                    else -> 300L
                }
                handler.postDelayed(apiRunnable,sleep)
            }
        })
    }

    private fun fetchAllGroups() {
        viewModel.getAllGroupsList().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data != null) {
                        adapter.submitList(it.data?.data!!.groups)
                        binding?.parent?.visibility = View.VISIBLE
                    } else {
                        //Handle empty layout
                        showEmptyView(getString(R.string.error_result))
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    //Handle error layout
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }
    }

    private fun updateGroupInfo(id: Int, position: Int, updateType: GroupUpdateType) {
        if (updateType == GroupUpdateType.JOIN) {
            viewModel.joinGroup(id)
        } else {
            viewModel.unjoinGroup(id)
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
        updateToolbar(getString(R.string.all_groups), true, true)
    }
}