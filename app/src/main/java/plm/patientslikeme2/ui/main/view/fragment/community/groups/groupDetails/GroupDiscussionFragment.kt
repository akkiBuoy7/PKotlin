package plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.Discussions
import plm.patientslikeme2.databinding.FragmentGroupDiscussionBinding
import plm.patientslikeme2.databinding.RowGroupDiscussionsBinding
import plm.patientslikeme2.databinding.RowPaginationBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.adapter.community.DiscussionsAutoCompleteAdapter
import plm.patientslikeme2.ui.main.adapter.community.OnItemClickListener
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.DiscussionOptionsDialog
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.FilterCommunityDiscussionsDialog
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.*

@AndroidEntryPoint
class GroupDiscussionFragment : BaseFragment<FragmentGroupDiscussionBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()
    private lateinit var adapter: GenericListAdapter<Discussions, RowGroupDiscussionsBinding>
    private lateinit var searchAdapter: DiscussionsAutoCompleteAdapter
    private lateinit var pagerAdapter: GenericListAdapter<String, RowPaginationBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentGroupDiscussionBinding =
        FragmentGroupDiscussionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.group_fragment

        initView()
        bindObservers()
    }

    private fun initView() {
        adapter = GenericListAdapter(R.layout.row_group_discussions, object :
            GenericRecyclerBindingInterface<RowGroupDiscussionsBinding, Discussions> {
            override fun bindData(
                binder: RowGroupDiscussionsBinding,
                model: Discussions,
                position: Int
            ) {
                binder.discussions = model
                binder.ivUser.loadImage(model.user?.name, model.user?.avatar?.value)
                binder.featuredDiscussions.isVisible = model.featured == true
                binder.tags.setClickableTagsOrBadges(model.tags, true) {
                    var tagId = 0
                    model.tags.forEach { item ->
                        if (item.text == it) {
                            tagId = item.id ?: 0
                        }
                    }
                    openTagDetails(Constants.GROUP_DISCUSSIONS, it, tagId)
                }

                binder.ivUser.setOnClickListener {
                    openUserProfile(Constants.GROUP_MEMBER, model.user?.id.toString())
                }

                binder.ivOptionMore.setOnClickListener {
                    model.id?.let { discussionId ->
                        model.user?.id?.let { userId ->
                            DiscussionOptionsDialog(discussionId, userId) {
                                fetchDiscussions(viewModel.groupDiscussionsCurrentPage)
                            }.show(childFragmentManager, getString(R.string.add_a_link))
                        }
                    }
                }

                binder.root.setOnClickListener {
                    model.id?.let { discussionId ->
                        openGroupDiscussionDetails(Constants.GROUP_DISCUSSIONS, discussionId.toString(), viewModel.aboutGroupResponse.value?.data?.data?.name, model, (viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember == true))
                    }
                }

                binder.tvDiscussionDetails.setOnClickListener {
                    model.id?.let { discussionId ->
                        openGroupDiscussionDetails(Constants.GROUP_DISCUSSIONS, discussionId.toString(), viewModel.aboutGroupResponse.value?.data?.data?.name, model, (viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember == true))
                    }
                }

                binder.tvRecentComment.setOnClickListener {
                    model.id?.let { discussionId ->
                        openGroupDiscussionDetails(Constants.GROUP_DISCUSSIONS, discussionId.toString(), viewModel.aboutGroupResponse.value?.data?.data?.name, model, (viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember == true))
                    }
                }

                binder.userLayout.setOnClickListener {
                    model.user?.id?.let { Id ->
                        openUserProfile(Constants.GROUP_MEMBER, Id.toString())
                    }
                }

                binder.like.setOnClickListener {
                    if (model.id != null && model.markedHelpful != null) {
                        model.helpfulCount = if (model.markedHelpful!!) (model.helpfulCount - 1) else (model.helpfulCount + 1)
                        model.markedHelpful = model.markedHelpful!!.not()
                        adapter.notifyDataSetChanged()
                        viewModel.discussionLikeUnlike(model.id!!, model.markedHelpful!!).observe(viewLifecycleOwner) {

                        }
                    }
                }

                val myPost = Preferences.getValue(Constants.USER_ID, "") == model.user?.id.toString()
                binder.like.isClickable = myPost.not()
                binder.like.isEnabled = myPost.not()
                binder.like.alpha = if (myPost) 0.5F else 1F

                binder.ivOptionMore.isVisible = myPost.not()
                binder.ivHelpful.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (model.markedHelpful == true) R.drawable.ic_helpful else R.drawable.ic_like_24))
                binder.executePendingBindings()
            }
        })
        binding?.rvDiscussions?.adapter = adapter

        pagerAdapter = GenericListAdapter(
            R.layout.row_pagination,
            object : GenericRecyclerBindingInterface<RowPaginationBinding, String> {
                override fun bindData(binder: RowPaginationBinding, model: String, position: Int) {
                    binder.page = model
                    binder.tvPage.setTextColor(
                        if (model == viewModel.groupDiscussionsCurrentPage) ContextCompat.getColor(
                            requireContext(),
                            R.color.teal
                        )
                        else ContextCompat.getColor(requireContext(), R.color.black)
                    )
                    binder.root.setOnClickListener {
                        fetchDiscussions(model)
                    }
                    binder.executePendingBindings()
                }
            })
        binding?.paginationView?.rvPagination?.adapter = pagerAdapter

        binding?.ivFilter?.setOnClickListener {
            FilterCommunityDiscussionsDialog {
                fetchDiscussions("1")
            }.show(childFragmentManager, getString(R.string.filters_and_sort))
        }
        searchAdapter = DiscussionsAutoCompleteAdapter(
            requireContext(),
            R.layout.row_suggestions_discussions_drop_down,
            object : OnItemClickListener {
                override fun onItemClick(model: Discussions?) {
                    model?.let {
                        openGroupDiscussionDetails(Constants.GROUP_DISCUSSIONS, it.id.toString(), viewModel.aboutGroupResponse.value?.data?.data?.name, model, (viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember == true))
                    }
                }
            })
        binding?.tvSearch?.threshold = 3
        binding?.tvSearch?.setAdapter(searchAdapter)
        binding?.tvSearch?.setDropDownBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bg_autocomplete_dropdown
            )
        )

        binding?.tvSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty().not()) {
                    viewModel.searchGroupDiscussions(s.toString()).observe(viewLifecycleOwner) {
                        when {
                            it.status.isSuccessful() -> {
                                it.data?.let {
                                    searchAdapter.setData(it)
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private fun bindObservers() {
        viewModel.groupDiscussions.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    it.data?.let {
                        it.data?.discussions?.let {
                            showDataView()
                            adapter.submitList(it)
                        }
                        it.meta?.let { meta ->
                            meta.totalEntries?.let { total ->
                                meta.perPage?.let { onPage ->
                                    val pageList = ArrayList<String>()
                                    var pages =  (total / onPage)
                                    pages = if (total % onPage == 0) pages else (pages + 1)
                                    viewModel.groupDiscussionsTotalPage = pages
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
            }
            checkDataStatus()
        }
        checkDataStatus()
    }

    private fun fetchDiscussions(currentPage: String) {
        viewModel.groupDiscussionsCurrentPage = currentPage
        pagerAdapter.notifyDataSetChanged()

        viewModel.groupDiscussions().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isError() -> {
                    hideProgress()
                    it.errorMessage?.let {
                        showEmptyView(it)
                    }
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    viewModel.groupDiscussions.postValue(it)
                }
            }
            updateFilterText()
        }

        binding?.parent?.smoothScrollTo(0, 0)
        if (this.parentFragment is GroupFragment) {
            (this.parentFragment as GroupFragment).binding?.parent?.smoothScrollTo(0, 0)
        }
    }

    private fun updateFilterText() {
        var filterText = ""
        if (viewModel.groupDiscussionsFilterTagsConditions) {
            filterText = getString(R.string.tag_followings) + ", "
        }
        if (viewModel.groupDiscussionsFilterMyFollowings) {
            filterText = getString(R.string.my_followings) + ", "
        }

        if (!viewModel.groupDiscussionsFilterMyFollowings && !viewModel.groupDiscussionsFilterTagsConditions) {
            filterText = getString(R.string.filters_by_all) + ", "
        }
        if (viewModel.groupDiscussionsFilterMyFollowings && viewModel.groupDiscussionsFilterTagsConditions) {
            filterText = getString(R.string.filters_by_all) + ", "
        }

        if (viewModel.groupDiscussionsSortBy == Constants.MOST_HELPFUL) {
            filterText += getString(R.string.most_helpful)
        } else {
            filterText += getString(R.string.sort_by_newest)
        }

        binding?.tvFilter?.text = filterText
    }

    private fun checkDataStatus() {
        if (viewModel.groupOverviewResponse.value?.status == Status.LOADING) {
            showProgress()
        } else if (viewModel.groupOverviewResponse.value?.status == Status.SUCCESS) {
            hideProgress()
            if (viewModel.groupDiscussions.value?.data?.data?.discussions.isNullOrEmpty()) {
                showEmptyView(getString(R.string.error_result))
            }
        } else if (viewModel.groupOverviewResponse.value?.status == Status.ERROR) {
            hideProgress()
            showEmptyView(getString(R.string.error_api))
        }
    }

    private fun showEmptyView(error: String) {
        binding?.rvDiscussions?.visibility = View.GONE
        binding?.paginationView?.paginationView?.visibility = View.GONE
        binding?.tvDiscussionsNotFound?.text = error
        binding?.tvDiscussionsNotFound?.visibility = View.VISIBLE
    }

    private fun showDataView() {
        binding?.rvDiscussions?.visibility = View.VISIBLE
        binding?.paginationView?.paginationView?.visibility = View.VISIBLE
        binding?.tvDiscussionsNotFound?.visibility = View.GONE
    }
}