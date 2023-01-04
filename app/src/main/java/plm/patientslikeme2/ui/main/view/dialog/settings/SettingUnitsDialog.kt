package plm.patientslikeme2.ui.main.view.dialog.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.DialogSettingUnitsBinding
import plm.patientslikeme2.ui.main.view.fragment.settings.UnitsFragment
import plm.patientslikeme2.utils.Constants.TYPE
import plm.patientslikeme2.utils.Constants.VALUE
import plm.patientslikeme2.utils.usercontrol.UserRadioButton


class SettingUnitsDialog : BottomSheetDialogFragment(), OnClickListener {

    companion object {

        fun newInstance(type: String, selected: String): SettingUnitsDialog {
            return SettingUnitsDialog().apply {
                val bundle = Bundle().apply {
                    putString(TYPE, type)
                    putString(VALUE, selected)
                }
                arguments = bundle
            }
        }
    }

    private lateinit var binding: DialogSettingUnitsBinding
    private lateinit var type: String
    private lateinit var selectedValue: String

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            type = arguments?.getString(TYPE).toString()
            selectedValue = arguments?.getString(VALUE).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        binding = DialogSettingUnitsBinding.inflate(inflater, group, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.text = type

        when (type) {
            getString(R.string.temperature) -> {
                binding.radioFirst.text = getString(R.string.fahrenheit)
                binding.radioSecond.text = getString(R.string.celsius)
            }
            getString(R.string.weight_measure) -> {
                binding.radioFirst.text = getString(R.string.imperial)
                binding.radioSecond.text = getString(R.string.metric)
            }
            getString(R.string.date_format) -> {
                binding.radioFirst.text = getString(R.string.mm_dd_yyyy)
                binding.radioSecond.text = getString(R.string.dd_mm_yyyy)
            }
        }
        binding.ivClose.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (!isAdded) {
            return
        }
        when (view?.id) {
            R.id.iv_close -> {
                dismiss()
            }
            R.id.btn_apply -> {
                validatedDetails()
            }
        }
    }

    private fun validatedDetails() {
        dismiss()
        val parent = (parentFragment as UnitsFragment)
        val selectedOption: Int = binding.radioGroup.checkedRadioButtonId
        val radioButton: UserRadioButton = binding.root.findViewById(selectedOption)
        when (type) {
            getString(R.string.temperature) -> {
                parent.updateTemperature(radioButton.text.toString())
            }
            getString(R.string.weight_measure) -> {
                parent.updateWeightType(radioButton.text.toString())
            }
            getString(R.string.date_format) -> {
                parent.updateDateFormat(radioButton.text.toString())
            }
        }
    }
}