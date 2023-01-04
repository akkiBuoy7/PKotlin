package plm.patientslikeme2.ui.main.view.fragment.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentFollowingBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.BaseFragmentStateAdapter
import plm.patientslikeme2.ui.main.viewmodel.following.FollowingViewModel


@AndroidEntryPoint
class FollowingFragment : BaseFragment<FragmentFollowingBinding>() {

    private val viewModel: FollowingViewModel by activityViewModels()
    private var activeFragment = 0

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFollowingBinding = FragmentFollowingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.getFollowingDiscussion(1).observe(viewLifecycleOwner) {
            viewModel.discussionResponse.postValue(it)
        }
        viewModel.getFollowingMembers(1).observe(viewLifecycleOwner) {
            viewModel.memberResponse.postValue(it)
        }
        viewModel.getFollowingTags(viewModel.tagPageNo).observe(viewLifecycleOwner) {
            viewModel.tagResponse.postValue(it)
        }
        viewModel.getFollowingResources(1).observe(viewLifecycleOwner) {
            viewModel.resourceResponse.postValue(it)
        }
    }

    private fun initTabLayout() {
        val tabsArray = getTabs()
        binding?.pager?.adapter =
            BaseFragmentStateAdapter(childFragmentManager, lifecycle, tabsArray)
        binding?.pager?.isUserInputEnabled = false

        binding?.tabLayout?.let {
            binding?.pager?.let { it1 ->
                TabLayoutMediator(it, it1) { tab, position ->
                    tab.text = tabsArray[position].first
                }.attach()
            }
        }

        binding?.tabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                activeFragment = tab.position
                updateDoneAction()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        toolbarBinding?.btnAction?.setOnClickListener {
            if (toolbarBinding?.btnAction?.text.toString() == getString(R.string.edit)) {
                toolbarBinding?.btnAction?.text = getString(R.string.done)
                updateEditAction()
            } else if (toolbarBinding?.btnAction?.text.toString() == getString(R.string.done)) {
                toolbarBinding?.btnAction?.text = getString(R.string.edit)
                updateDoneAction()
            }
        }
    }

    private fun updateDoneAction() {
        when (activeFragment) {
            0 -> viewModel.discussionFragment.postValue(1)
            1 -> viewModel.memberFragment.postValue(1)
            2 -> viewModel.tagFragment.postValue(1)
            3 -> viewModel.resourceFragment.postValue(1)
        }
    }

    private fun updateEditAction() {
        when (activeFragment) {
            0 -> viewModel.discussionFragment.postValue(2)
            1 -> viewModel.memberFragment.postValue(2)
            2 -> viewModel.tagFragment.postValue(2)
            3 -> viewModel.resourceFragment.postValue(2)
        }
    }

    private fun getTabs(): ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>> {
        val tabsArray = ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>>()
        if (featureFlag.android_discussions) {
            tabsArray.add(Pair("Discussions", FollowingDiscussionsFragment()))
        }
        if (featureFlag.android_members) {
            tabsArray.add(Pair("Members", FollowingMembersFragment()))
        }
        tabsArray.add(Pair("Tags", FollowingTagsFragment()))
        if (featureFlag.android_resources) {
            tabsArray.add(Pair("Resources", FollowingResourcesFragment()))
        }
        return tabsArray
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.nav_menu_following),
            showBackArrow = false,
            hideRightIcon = true,
            showCloseRightIcon = true,
            showRightAction = true,
            actionButtonText = "Edit"
        )
    }
}