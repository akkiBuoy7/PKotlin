package plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.community.groups.members.MemberResponse
import plm.patientslikeme2.data.model.community.groups.members.NewMembers
import plm.patientslikeme2.databinding.*
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericHorizontalRecyclerBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericListHorizontalAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.dialog.community.groups.members.FilterGroupMembersDialog
import plm.patientslikeme2.ui.main.view.dialog.community.groups.members.SearchGroupMembersDialog
import plm.patientslikeme2.ui.main.view.dialog.messages.NewMessageDialog
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.ui.main.viewmodel.messages.MessagesViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.loadImage
import plm.patientslikeme2.utils.extensions.openPrivateMessageConversation
import plm.patientslikeme2.utils.extensions.openUserProfile

@AndroidEntryPoint
class GroupMembersFragment : BaseFragment<FragmentGroupMembersBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()
    private val messageViewModel: MessagesViewModel by activityViewModels()

    private lateinit var newMemberAdapter: GenericListHorizontalAdapter<NewMembers, RowGroupNewMembersBinding>
    private lateinit var membersAdapter: GenericListAdapter<NewMembers, RowGroupMembersBinding>
    private lateinit var pagerAdapter: GenericListAdapter<String, RowPaginationBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentGroupMembersBinding =
        FragmentGroupMembersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.group_fragment

        binding?.newMemberResponse = MemberResponse()

        initView()
        bindObservers()
    }

    private fun initView() {
        newMemberAdapter = GenericListHorizontalAdapter(
            R.layout.row_group_new_members,
            object :
                GenericHorizontalRecyclerBindingInterface<RowGroupNewMembersBinding, NewMembers> {
                override fun bindData(
                    binder: RowGroupNewMembersBinding,
                    model: NewMembers,
                    position: Int
                ) {
                    binder.user = model
                    binder.root.setOnClickListener {
                        openUserProfile(Constants.GROUP_MEMBER, model.id.toString())
                    }
                    binder.tvConditions.setOnClickListener {
                        openUserProfile(Constants.GROUP_MEMBER, model.id.toString())
                    }
                    binder.tvStatus.setOnClickListener {
                        openUserProfile(Constants.GROUP_MEMBER, model.id.toString())
                    }
                    binder.btnSendMessage.setOnClickListener {
                        messageViewModel.otherParticipantId = model.id.toString()
                        messageViewModel.otherParticipantName = model.login
                        openPrivateMessageConversation(Constants.NEW_MESSAGE_FROM_GROUP)
                    }
                    binder.ivUser.loadImage(model.name, model.profilePic)
                    binder.executePendingBindings()
                }
            },
            (Resources.getSystem().displayMetrics.widthPixels * 0.85F)
        )
        binding?.rvNewMembers?.adapter = newMemberAdapter

        membersAdapter = GenericListAdapter(
            R.layout.row_group_members,
            object : GenericRecyclerBindingInterface<RowGroupMembersBinding, NewMembers> {
                override fun bindData(
                    binder: RowGroupMembersBinding,
                    model: NewMembers,
                    position: Int
                ) {
                    binder.user = model
                    binder.root.setOnClickListener {
                        openUserProfile(Constants.GROUP_MEMBER, model.id.toString())
                    }
                    binder.tvConditions.setOnClickListener {
                        openUserProfile(Constants.GROUP_MEMBER, model.id.toString())
                    }
                    binder.ivUser.loadImage(model.name, model.profilePic)
                    binder.executePendingBindings()
                }
            })
        binding?.rvMembers?.adapter = membersAdapter

        pagerAdapter = GenericListAdapter(
            R.layout.row_pagination,
            object : GenericRecyclerBindingInterface<RowPaginationBinding, String> {
                override fun bindData(binder: RowPaginationBinding, model: String, position: Int) {
                    binder.page = model
                    binder.tvPage.setTextColor(
                        if (model == viewModel.allMemberCurrentPage) ContextCompat.getColor(
                            requireContext(),
                            R.color.teal
                        )
                        else ContextCompat.getColor(requireContext(), R.color.black)
                    )
                    binder.root.setOnClickListener {
                        viewModel.allMemberCurrentPage = model
                        fetchUsers(false, model)
                    }
                    binder.executePendingBindings()
                }
            })
        binding?.paginationView?.rvPagination?.adapter = pagerAdapter

        binding?.ivFilter?.setOnClickListener {
            FilterGroupMembersDialog {
                fetchUsers(true, "1")
            }.show(childFragmentManager, getString(R.string.filters_and_sort))
        }

        binding?.tvSearchMember?.setOnClickListener {
            SearchGroupMembersDialog().show(
                childFragmentManager,
                getString(R.string.search_for_members)
            )
        }

        binding?.paginationView?.ivBack?.setOnClickListener {
            var currentPage = viewModel.allMemberCurrentPage.toInt()
            if (currentPage > 1) {
                currentPage -= 1
                fetchUsers(false, currentPage.toString())
            }
        }

        binding?.paginationView?.ivForward?.setOnClickListener {
            var currentPage = viewModel.allMemberCurrentPage.toInt()
            if (currentPage < viewModel.allMemberTotalPage) {
                currentPage += 1
                fetchUsers(false, currentPage.toString())
            }
        }
    }

    private fun bindObservers() {
        viewModel.newMemberResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    binding?.newMemberResponse = it.data
                    it.data?.data?.newMembers?.let {
                        newMemberAdapter.submitList(it)
                    }
                }
            }
            checkDataStatus()
        }

        viewModel.membersResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    it.data?.data?.newMembers?.let {
                        membersAdapter.submitList(it)
                    }
                    binding?.tvMembersNotFound?.isVisible =
                        (viewModel.allMemberFilterMyConditions || viewModel.allMemberFilterMyFollowings) && it.data?.data?.newMembers.isNullOrEmpty()
                    binding?.filterLayout?.isVisible =
                        if (it.data?.data?.newMembers.isNullOrEmpty().not()) true
                        else (viewModel.allMemberFilterMyConditions || viewModel.allMemberFilterMyFollowings)

                    it.data?.meta?.let { meta ->
                        meta.totalEntries?.let { total ->
                            meta.perPage?.let { onPage ->
                                val pageList = ArrayList<String>()
                                var pages = (total / onPage)
                                pages = if (total % onPage == 0) pages else (pages + 1)
                                viewModel.allMemberTotalPage = pages
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
            checkDataStatus()
        }

        checkDataStatus()
    }

    private fun checkDataStatus() {
        if (viewModel.newMemberResponse.value?.status == Status.LOADING && viewModel.membersResponse.value?.status == Status.LOADING) {
            showProgress()
        } else if (viewModel.newMemberResponse.value?.status == Status.SUCCESS || viewModel.membersResponse.value?.status == Status.SUCCESS) {
            hideProgress()
        } else if (viewModel.newMemberResponse.value?.status == Status.ERROR && viewModel.membersResponse.value?.status == Status.ERROR) {
            hideProgress()
            showEmptyView(getString(R.string.error_api))
        }
    }

    private fun fetchUsers(isFiltered: Boolean, currentPage: String) {
        binding?.rvNewMembers?.isVisible = currentPage == "1"
        binding?.tvNewMembers?.isVisible = currentPage == "1"
        viewModel.allMemberCurrentPage = currentPage
        pagerAdapter.notifyDataSetChanged()

        if (isFiltered) {
            viewModel.fetchFilteredUsers()
        } else {
            viewModel.fetchPaginatedUsers()
        }.observe(viewLifecycleOwner) {
            viewModel.membersResponse.postValue(it)
        }

        binding?.parent?.smoothScrollTo(0, 0)
        if (this.parentFragment is GroupFragment) {
            (this.parentFragment as GroupFragment).binding?.parent?.smoothScrollTo(0, 0)
        }
    }

    fun showSuccessErrorView(message: String?, status: Boolean?) {
        if (status != null) {
            if (message != null) {
                showSuccessErrorLayout(status, message)
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
}