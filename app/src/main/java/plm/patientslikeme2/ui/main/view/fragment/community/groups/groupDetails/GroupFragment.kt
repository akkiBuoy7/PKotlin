package plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.community.groups.groupOverview.GroupOverviewResponse
import plm.patientslikeme2.databinding.FragmentGroupBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.BaseFragmentStateAdapter
import plm.patientslikeme2.ui.main.view.activity.home.MainActivity
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.CreateNewThreadDialog
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.loadImage

@AndroidEntryPoint
class GroupFragment : BaseFragment<FragmentGroupBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentGroupBinding = FragmentGroupBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.group_fragment

        arguments?.getString(Constants.GROUP_ID)?.let {
            viewModel.groupId = it
        }
        arguments?.getBoolean(Constants.GROUP_PRIVATE, false)?.let {
            viewModel.isPrivateGroup = it
        }

        bindObservers()
        initListener()
        initUI()
    }

    private fun bindObservers() {
        viewModel.groupOverview().observe(viewLifecycleOwner) {
            viewModel.groupOverviewResponse.postValue(it)
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    it.data?.let {
                        it.data?.group?.isMember?.let {
                            binding?.btnJoinGroup?.isVisible = it.not()
                        }
                        viewModel.moderatorResponse.postValue(it.data?.group?.moderator)
                        binding?.groupOverviewResponse = it
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }

        viewModel.getAboutGroup().observe(viewLifecycleOwner) {
            viewModel.aboutGroupResponse.postValue(it)
        }

        viewModel.moderatorResponse.observe(viewLifecycleOwner) {
            if (it != null && !TextUtils.isEmpty(it.name)) {
                binding?.llModerate?.visibility = View.VISIBLE
                binding?.tvModeratedName?.text = it.name
                binding?.ivUser?.loadImage(it.name.toString(), it.profilePic.toString())
            } else {
                binding?.llModerate?.visibility = View.GONE
            }
        }

        viewModel.getNewMembers().observe(viewLifecycleOwner) {
            viewModel.newMemberResponse.postValue(it)
        }

        viewModel.getMembers().observe(viewLifecycleOwner) {
            viewModel.membersResponse.postValue(it)
        }

        viewModel.getGroupEvents().observe(viewLifecycleOwner) {
            viewModel.eventResponse.postValue(it)
        }

        viewModel.getGroupResource().observe(viewLifecycleOwner) {
            viewModel.resourceResponse.postValue(it)
        }

        viewModel.groupDiscussions().observe(viewLifecycleOwner) {
            viewModel.groupDiscussions.postValue(it)
        }

        viewModel.getRecommendedTags().observe(viewLifecycleOwner) {
            viewModel.recommendedTags.postValue(it)
        }
    }

    private fun initListener() {
        binding?.tvAboutTheGroup?.setOnClickListener {
            findNavController().navigate(R.id.group_fragment_to_about_the_group_fragment)
        }

        binding?.tvCommunityGuidelines?.setOnClickListener {
            findNavController().navigate(R.id.group_fragment_to_group_community_guidelines_fragment)
        }

        binding?.btnJoinGroup?.setOnClickListener {
            viewModel.joinGroup(viewModel.groupId.toInt()).observe(viewLifecycleOwner) {
                when {
                    it.status.isLoading() -> {
                        showProgress()
                    }
                    it.status.isSuccessful() -> {
                        hideProgress()
                        showSuccessErrorLayout(true, it.data?.message.toString())
                        binding?.btnJoinGroup?.isVisible = false
                    }
                    it.status.isError() -> {
                        hideProgress()
                    }
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

    private fun initUI() {
        if (viewModel.isPrivateGroup) {
            binding?.btnJoinGroup?.isVisible = false
            binding?.groupInfo?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.private_group_bg
                )
            )
            binding?.bar?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding?.tvGroupName?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.white
                )
            )
            binding?.tvModeratedBy?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.white
                )
            )
            binding?.tvModeratedName?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.white
                )
            )
            binding?.tvAboutTheGroup?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.blue
                )
            )
            binding?.tvCommunityGuidelines?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.blue
                )
            )
            binding?.tabLayout?.tabTextColors = colorStateListOf(
                intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(
                    requireContext(), R.color.white
                ),
                intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(
                    requireContext(), R.color.gray_dark_7
                ),
            )
            if (activity is MainActivity) {
                (activity as MainActivity).toolbarHome?.activityToolbar?.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.private_group_bg)
                )
                (activity as MainActivity).toolbarHome?.toolbarTitle?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                setMessagingIcon(true)
                (activity as MainActivity).toolbarHome?.ivNotification?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                (activity as MainActivity).toolbarHome?.ivClose?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                (activity as MainActivity).toolbarHome?.ivMenu?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                (activity as MainActivity).toolbarHome?.ivBack?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
            }
        } else {
            viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember?.let {
                binding?.btnJoinGroup?.isVisible = it.not()
            } ?: run {
                binding?.btnJoinGroup?.isVisible = false
            }
            binding?.groupInfo?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.white
                )
            )
            binding?.bar?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding?.tvGroupName?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.black
                )
            )
            binding?.tvModeratedBy?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.gray_dark_5
                )
            )
            binding?.tvModeratedName?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.gray_dark_5
                )
            )
            binding?.tvAboutTheGroup?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.blue_darker
                )
            )
            binding?.tvCommunityGuidelines?.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.blue_darker
                )
            )
            binding?.tabLayout?.tabTextColors = colorStateListOf(
                intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(
                    requireContext(), R.color.gray_dark_7
                ),
                intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(
                    requireContext(), R.color.white
                ),
            )
        }

        binding?.groupOverviewResponse = GroupOverviewResponse()
        val tabsArray = getTabs()
        binding?.pager?.adapter =
            BaseFragmentStateAdapter(childFragmentManager, lifecycle, tabsArray)
        binding?.pager?.isUserInputEnabled = false

        binding?.tabLayout?.let {
            it.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.view?.background =
                        if (viewModel.isPrivateGroup) ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.selector_tab_layout_private_group,
                            null
                        )
                        else ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.selector_tab_layout,
                            null
                        )

                    viewModel.groupOverviewResponse.value?.data?.data?.group?.isMember?.let {
                        if (it) {
                            binding?.btnStartDiscussion?.isVisible = tab?.text == "Discussion"
                            binding?.btnJoinGroup?.isVisible = false
                        } else {
                            binding?.btnStartDiscussion?.isVisible = false
                            binding?.btnJoinGroup?.isVisible = true
                        }
                    } ?: run {
                        binding?.btnStartDiscussion?.isVisible = false
                        binding?.btnJoinGroup?.isVisible = false
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.view?.background = ResourcesCompat.getDrawable(
                        requireContext().resources, R.drawable.bg_tab_layout_transparent, null
                    )
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
            binding?.pager?.let { it1 ->
                TabLayoutMediator(it, it1) { tab, position ->
                    tab.text = tabsArray[position].first
                }.attach()
            }
        }

        binding?.btnStartDiscussion?.setOnClickListener {
            CreateNewThreadDialog.newInstance(
                Constants.ADD
            ) { shouldRefresh, msg ->
                if (shouldRefresh) {
                    showSuccessErrorLayout(
                        shouldRefresh, msg
                    )
                    viewModel.groupDiscussions().observe(viewLifecycleOwner) {
                        viewModel.groupDiscussions.postValue(it)
                    }
                }
            }.show(childFragmentManager, getString(R.string.new_thread))
        }
    }

    private fun getTabs(): ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>> {
        val tabsArray = ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>>()
        tabsArray.add(Pair("Overview", GroupOverviewFragment()))
        if (viewModel.isPrivateGroup.not()) {
            if (featureFlag.android_discussions) {
                tabsArray.add(Pair("Discussion", GroupDiscussionFragment()))
            }
            if (featureFlag.android_members) {
                tabsArray.add(Pair("Members", GroupMembersFragment()))
            }
        }
        if (featureFlag.android_events) {
            tabsArray.add(Pair("Events", GroupEventsFragment()))
        }
        if (featureFlag.android_resources) {
            tabsArray.add(Pair("Resources", GroupResourcesFragment()))
        }
        return tabsArray
    }

    private fun colorStateListOf(vararg mapping: Pair<IntArray, Int>): ColorStateList {
        val (states, colors) = mapping.unzip()
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.title_community),
            showBackArrow = true,
            showCloseRightIcon = false,
            hideRightIcon = false
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetInitialValues()
        toolbarBinding?.activityToolbar?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        toolbarBinding?.toolbarTitle?.setTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.black
            )
        )
        setMessagingIcon(false)
        toolbarBinding?.ivNotification?.setColorFilter(
            ContextCompat.getColor(
                requireContext(), R.color.black
            )
        )
        toolbarBinding?.ivClose?.setColorFilter(
            ContextCompat.getColor(
                requireContext(), R.color.black
            )
        )
        toolbarBinding?.ivMenu?.setColorFilter(
            ContextCompat.getColor(
                requireContext(), R.color.black
            )
        )
        toolbarBinding?.ivBack?.setColorFilter(
            ContextCompat.getColor(
                requireContext(), R.color.black
            )
        )
    }
}
