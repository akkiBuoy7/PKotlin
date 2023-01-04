package plm.patientslikeme2.ui.main.view.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentUnitsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.view.dialog.settings.SettingUnitsDialog

@AndroidEntryPoint
class UnitsFragment : BaseFragment<FragmentUnitsBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentUnitsBinding = FragmentUnitsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.unitsFragment
        binding?.handlers = this
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
        var type = ""
        when (view.id) {
            R.id.ll_temperature -> {
                type = getString(R.string.temperature)
            }
            R.id.ll_weight -> {
                type = getString(R.string.weight_measure)
            }
            R.id.ll_date -> {
                type = getString(R.string.date_format)
            }
        }
        SettingUnitsDialog.newInstance(type, "")
            .show(childFragmentManager, getString(R.string.units))
    }

    fun updateTemperature(value: String) {
        binding?.tvTemperature?.text = value
        showSuccessErrorLayout(true, "Temperature unit has been changed.")
    }

    fun updateWeightType(value: String) {
        binding?.tvWeightMeasure?.text = value
        showSuccessErrorLayout(true, "Weight and measures unit has been changed.")
    }

    fun updateDateFormat(value: String) {
        binding?.tvDateFormat?.text = value
        showSuccessErrorLayout(true, "Date format has been changed.")
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.units),
            showBackArrow = true,
            hideRightIcon = true
        )
    }
}