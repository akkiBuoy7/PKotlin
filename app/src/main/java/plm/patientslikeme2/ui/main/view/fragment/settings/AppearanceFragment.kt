package plm.patientslikeme2.ui.main.view.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentAppearanceBinding
import plm.patientslikeme2.ui.base.BaseFragment

@AndroidEntryPoint
class AppearanceFragment : BaseFragment<FragmentAppearanceBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAppearanceBinding = FragmentAppearanceBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.appearanceFragment
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.appearance),
            showBackArrow = true,
            hideRightIcon = true
        )
    }
}