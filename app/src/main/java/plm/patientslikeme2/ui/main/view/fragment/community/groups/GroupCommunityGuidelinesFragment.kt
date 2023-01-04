package plm.patientslikeme2.ui.main.view.fragment.community.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentGroupCommunityGuidelinesBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.openHyperLinks

@AndroidEntryPoint
class GroupCommunityGuidelinesFragment : BaseFragment<FragmentGroupCommunityGuidelinesBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentGroupCommunityGuidelinesBinding =
        FragmentGroupCommunityGuidelinesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.group_community_guidelines_fragment

        binding?.tvReachOut?.setOnClickListener {
            openHyperLinks(requireContext(), Constants.CONTACT_URL)
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.community_guidelines), true)
    }
}