package plm.patientslikeme2.ui.main.view.fragment.community.dailyme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.databinding.FragmentCommunityDailyMeBinding
import plm.patientslikeme2.ui.base.BaseFragment

@AndroidEntryPoint
class CommunityDailyMeFragment : BaseFragment<FragmentCommunityDailyMeBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCommunityDailyMeBinding =
        FragmentCommunityDailyMeBinding.inflate(inflater, container, false)
}