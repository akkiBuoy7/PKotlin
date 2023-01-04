package plm.patientslikeme2.ui.main.view.fragment.myhealth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentMyHealthBinding
import plm.patientslikeme2.ui.base.BaseFragment

@AndroidEntryPoint
class MyHealthFragment : BaseFragment<FragmentMyHealthBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMyHealthBinding = FragmentMyHealthBinding.inflate(inflater, container, false)

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.title_my_health), false)
    }
}