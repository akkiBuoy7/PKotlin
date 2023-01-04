package plm.patientslikeme2.ui.main.view.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentBlockedMembersBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.base.BaseFragment

@AndroidEntryPoint
class BlockedMembersFragment : BaseFragment<FragmentBlockedMembersBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentBlockedMembersBinding =
        FragmentBlockedMembersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.blockedMembersFragment
        binding?.handlers = this
        binding?.model = featureFlag
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
        when (view.id) {
            R.id.tv_hidden_post -> {
                findNavController().navigate(R.id.blockedMembersFragment_to_hiddenPostsFragment)
            }
            R.id.tv_blocked_private_messages -> {
                findNavController().navigate(R.id.blockedMembersFragment_to_blockedPrivateMessagesFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.blocked_members),
            showBackArrow = true,
            hideRightIcon = true
        )
    }
}