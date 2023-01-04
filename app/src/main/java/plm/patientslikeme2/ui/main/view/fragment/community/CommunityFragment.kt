package plm.patientslikeme2.ui.main.view.fragment.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentCommunityBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.BaseFragmentStateAdapter
import plm.patientslikeme2.ui.main.view.fragment.community.dailyme.CommunityDailyMeFragment
import plm.patientslikeme2.ui.main.view.fragment.community.events.CommunityEventsFragment
import plm.patientslikeme2.ui.main.view.fragment.community.groups.CommunityGroupsFragment
import plm.patientslikeme2.ui.main.view.fragment.community.members.CommunityMembersFragment
import plm.patientslikeme2.ui.main.viewmodel.community.CommunityViewModel

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>() {

    private val viewModel: CommunityViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCommunityBinding = FragmentCommunityBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCommunityDetails()
        initTabLayout()
    }

    private fun getCommunityDetails() {
        getMyGroupList()
        getSuggestedGroupList()
        getMembersLikeMe()
        getAllMembers()
        getCommunityEvents()
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
    }

    private fun getTabs(): ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>> {
        val tabsArray = ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>>()
        if (featureFlag.android_community_groups) {
            tabsArray.add(Pair("Groups", CommunityGroupsFragment()))
        }
        tabsArray.add(Pair("DailyMe stories", CommunityDailyMeFragment()))
        if (featureFlag.android_members) {
            tabsArray.add(Pair("Members", CommunityMembersFragment()))
        }
        if (featureFlag.android_events) {
            tabsArray.add(Pair("Events", CommunityEventsFragment()))
        }

        return tabsArray
    }

    private fun getMyGroupList() {
        viewModel.getMyGroupList().observe(viewLifecycleOwner) {
            viewModel.myGroupsResponse.postValue(it)
        }
    }

    private fun getSuggestedGroupList() {
        viewModel.getSuggestedGroupList().observe(viewLifecycleOwner) {
            viewModel.suggestedGroupResponse.postValue(it)
        }
    }

    private fun getMembersLikeMe() {
        viewModel.getMembersLikeMe().observe(viewLifecycleOwner) {
            viewModel.membersLikeMeResponse.postValue(it)
        }
    }

    private fun getAllMembers() {
        viewModel.getAllMembers().observe(viewLifecycleOwner) {
            viewModel.allMembersResponse.postValue(it)
        }
    }

    private fun getCommunityEvents() {
        viewModel.getCommunityEvents().observe(viewLifecycleOwner) {
            viewModel.eventResponse.postValue(it)
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.title_community), false)
    }
}