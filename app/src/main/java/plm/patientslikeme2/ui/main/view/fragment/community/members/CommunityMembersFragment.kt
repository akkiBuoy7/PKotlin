package plm.patientslikeme2.ui.main.view.fragment.community.members

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.community.members.MemberResponse
import plm.patientslikeme2.data.model.community.members.Users
import plm.patientslikeme2.databinding.*
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericHorizontalRecyclerBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericListHorizontalAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.adapter.community.UserAutoCompleteAdapter
import plm.patientslikeme2.ui.main.view.dialog.community.members.FilterCommunityMembersDialog
import plm.patientslikeme2.ui.main.view.dialog.messages.NewMessageDialog
import plm.patientslikeme2.ui.main.viewmodel.community.CommunityViewModel
import plm.patientslikeme2.ui.main.viewmodel.messages.MessagesViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.OnAutoCompleteItemClickListener
import plm.patientslikeme2.utils.extensions.loadImage
import plm.patientslikeme2.utils.extensions.openPrivateMessageConversation
import plm.patientslikeme2.utils.extensions.openUserProfile

@AndroidEntryPoint
class CommunityMembersFragment : BaseFragment<FragmentCommunityMembersBinding>() {

    private val viewModel: CommunityViewModel by activityViewModels()
    private val messageViewModel: MessagesViewModel by activityViewModels()
    private lateinit var memberLikeMeAdapter: GenericListHorizontalAdapter<Users, RowCommunityMembersLikeMeBinding>
    private lateinit var allMemberAdapter: GenericListAdapter<Users, RowCommunityAllMembersBinding>
    private lateinit var pagerAdapter: GenericListAdapter<String, RowPaginationBinding>
    var adapter: UserAutoCompleteAdapter? = null
    private val onItemClick = object : OnAutoCompleteItemClickListener {
        override fun onItemClick(item: Any?) {
            binding?.acSearchMember?.text = null
            openUserProfile(Constants.COMMUNITY_MEMBER, (item as Users).id.toString())
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCommunityMembersBinding =
        FragmentCommunityMembersBinding.inflate(inflater, container, false)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.membersLikeMeResponse = MemberResponse()
        initView()
        bindObservers()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initView() {
        memberLikeMeAdapter = GenericListHorizontalAdapter(
            R.layout.row_community_members_like_me,
            object :
                GenericHorizontalRecyclerBindingInterface<RowCommunityMembersLikeMeBinding, Users> {
                override fun bindData(
                    binder: RowCommunityMembersLikeMeBinding,
                    model: Users,
                    position: Int
                ) {
                    binder.user = model
                    binder.ivDelete.setOnClickListener {
                        excludedUser(model.id)
                    }
                    binder.btnFollow.setOnClickListener {
                        followMember(model.id.toString(), follow = true)
                    }
                    binder.btnUnfollow.setOnClickListener {
                        followMember(model.id.toString(), follow = false)
                    }
                    binder.root.setOnClickListener {
                        openUserProfile(Constants.COMMUNITY_MEMBER, model.id.toString())
                    }
                    binder.tvConditions.setOnClickListener {
                        openUserProfile(Constants.COMMUNITY_MEMBER, model.id.toString())
                    }
                    binder.btnSendMessage.setOnClickListener {
                        messageViewModel.otherParticipantId = model.id.toString()
                        messageViewModel.otherParticipantName = model.login
                        openPrivateMessageConversation(Constants.NEW_MESSAGE_FROM_COMMUNITY)
                    }
                    binder.ivUser.loadImage(model.name, model.profilePic)
                    binder.executePendingBindings()
                }
            },
            (Resources.getSystem().displayMetrics.widthPixels * 0.88F)
        )
        binding?.rvMembersLikeMe?.adapter = memberLikeMeAdapter

        allMemberAdapter = GenericListAdapter(
            R.layout.row_community_all_members,
            object : GenericRecyclerBindingInterface<RowCommunityAllMembersBinding, Users> {
                override fun bindData(
                    binder: RowCommunityAllMembersBinding,
                    model: Users,
                    position: Int
                ) {
                    binder.user = model
                    binder.root.setOnClickListener {
                        openUserProfile(Constants.COMMUNITY_MEMBER, model.id.toString())
                    }
                    binder.tvConditions.setOnClickListener {
                        openUserProfile(Constants.COMMUNITY_MEMBER, model.id.toString())
                    }
                    binder.ivUser.loadImage(model.name, model.profilePic)
                    binder.executePendingBindings()
                }
            })
        binding?.rvAllMembers?.adapter = allMemberAdapter

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
                        fetchUsers(false, model)
                    }
                    binder.executePendingBindings()
                }
            })
        binding?.paginationView?.rvPagination?.adapter = pagerAdapter

        binding?.ivFilter?.setOnClickListener {
            FilterCommunityMembersDialog {
                fetchUsers(true, "1")
            }.show(childFragmentManager, getString(R.string.filters_and_sort))
        }

//        binding?.tvSearchMember?.setOnClickListener {
//            SearchCommunityMembersDialog().show(
//                childFragmentManager,
//                getString(R.string.looking_for_someone_specific)
//            )
//        }

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

        setListeners()
    }

    private fun setListeners() {

        adapter =
            UserAutoCompleteAdapter(requireContext(), R.layout.row_user_drop_down, onItemClick)
        binding?.acSearchMember?.setAdapter(adapter)
        binding?.acSearchMember?.threshold = 2
        binding?.acSearchMember?.addTextChangedListener(object : TextWatcher {
            private val handler = Handler(Looper.getMainLooper())
            private val apiRunnable = Runnable {
                binding?.acSearchMember?.text?.let {
                    if (it.isNotEmpty()) {
                        searchUsers(it.toString())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val sleep = when (s.length) {
                    1 -> 1000L
                    2, 3 -> 700L
                    4, 5 -> 500L
                    else -> 300L
                }
                handler.postDelayed(apiRunnable, sleep)
            }
        })
    }

    private fun searchUsers(query: String) {
        viewModel.searchUsers(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        it.data?.data?.users?.let {
                            adapter?.setData(it)
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }


    private fun bindObservers() {
        viewModel.membersLikeMeResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    binding?.membersLikeMeResponse = it.data
                    it.data?.data?.users?.let {
                        memberLikeMeAdapter.submitList(it)
                    }
                }
            }
            checkDataStatus()
        }

        viewModel.allMembersResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    it.data?.data?.users?.let {
                        allMemberAdapter.submitList(it)
                    }
                    binding?.tvMembersNotFound?.isVisible =
                        (viewModel.allMemberFilterMyConditions || viewModel.allMemberFilterMyFollowings) && it.data?.data?.users.isNullOrEmpty()
                    binding?.allMemberLayout?.isVisible =
                        if (it.data?.data?.users.isNullOrEmpty().not()) true
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
        if (viewModel.membersLikeMeResponse.value?.status == Status.LOADING && viewModel.allMembersResponse.value?.status == Status.LOADING) {
            showProgress()
        } else if (viewModel.membersLikeMeResponse.value?.status == Status.SUCCESS || viewModel.allMembersResponse.value?.status == Status.SUCCESS) {
            hideProgress()
            if (viewModel.membersLikeMeResponse.value?.status == Status.SUCCESS && viewModel.allMembersResponse.value?.status == Status.SUCCESS) {
                if (viewModel.membersLikeMeResponse.value?.data?.data?.users.isNullOrEmpty() && viewModel.allMembersResponse.value?.data?.data?.users.isNullOrEmpty()) {
                    binding?.acSearchMember?.isVisible = false
                    binding?.tvSearchMemberTitle?.isVisible = false
                    binding?.dotedLine?.isVisible = false
                    showEmptyView(getString(R.string.error_result))
                }
            }
        } else if (viewModel.membersLikeMeResponse.value?.status == Status.ERROR && viewModel.allMembersResponse.value?.status == Status.ERROR) {
            hideProgress()
            showEmptyView(getString(R.string.error_api))
        }
    }

    private fun followMember(id: String, follow: Boolean) {
        if (follow) {
            viewModel.followMember(id)
        } else {
            viewModel.unFollowMember(id)
        }.observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isError() -> {
                    hideProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    val updatedList = ArrayList<Users>().apply {
                        for (user in memberLikeMeAdapter.currentList) {
                            if (user.id == id.toInt()) {
                                user.followed = follow
                            }
                            add(user)
                        }
                    }
                    updateMembersLikeMe(updatedList)
                }
            }
        }
    }

    private fun excludedUser(id: Int) {
        val updatedList = ArrayList<Users>().apply {
            for (user in memberLikeMeAdapter.currentList) {
                if (user.id != id) {
                    add(user)
                }
            }
        }
        updateMembersLikeMe(updatedList)
        viewModel.excludedUser(id).observe(viewLifecycleOwner) {

        }
    }

    private fun fetchUsers(isFiltered: Boolean, currentPage: String) {
        binding?.rvMembersLikeMe?.isVisible = currentPage == "1"
        binding?.tvMembersLikeMe?.isVisible = currentPage == "1"
        viewModel.allMemberCurrentPage = currentPage
        pagerAdapter.notifyDataSetChanged()

        if (isFiltered) {
            viewModel.fetchFilteredUsers()
        } else {
            viewModel.fetchPaginatedUsers()
        }.observe(viewLifecycleOwner) {
            viewModel.allMembersResponse.postValue(it)
        }

        binding?.parent?.smoothScrollTo(0, 0)
    }

    private fun updateMembersLikeMe(updatedList: ArrayList<Users>) {
        memberLikeMeAdapter.submitList(updatedList) {
            memberLikeMeAdapter.notifyDataSetChanged()
        }
        val memberResponse = viewModel.membersLikeMeResponse.value?.data
        memberResponse?.data?.users = updatedList
        binding?.membersLikeMeResponse = memberResponse
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

    fun showSuccessErrorView(message: String?, status: Boolean?) {
        if (status != null) {
            if (message != null) {
                showSuccessErrorLayout(status, message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adjustKeyBoard(true)
    }

    override fun onPause() {
        adjustKeyBoard(false)
        super.onPause()
    }
}