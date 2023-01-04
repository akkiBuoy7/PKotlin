package plm.patientslikeme2.ui.main.view.fragment.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentLearningBinding
import plm.patientslikeme2.ui.base.BaseFragment

@AndroidEntryPoint
class LearningFragment : BaseFragment<FragmentLearningBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLearningBinding = FragmentLearningBinding.inflate(inflater, container, false)

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.title_learning), false)
    }
}