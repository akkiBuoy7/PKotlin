package plm.patientslikeme2.ui.main.view.dialog.conditions

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.conditions.AddSymptomRequest
import plm.patientslikeme2.data.model.conditions.AddSymptomsItem
import plm.patientslikeme2.data.model.conditions.ConditionInfo
import plm.patientslikeme2.data.model.conditions.SymptomInfosItem
import plm.patientslikeme2.databinding.DialogAddSymptomBinding
import plm.patientslikeme2.databinding.RowSymptomsSettingsBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.dialog.signup.PrivacySettingsInfoDialog
import plm.patientslikeme2.ui.main.view.fragment.conditions.ConditionDetailsFragment
import plm.patientslikeme2.ui.main.viewmodel.conditions.ConditionsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.addHintWithArray
import plm.patientslikeme2.utils.extensions.addHintWithArrayList
import plm.patientslikeme2.utils.extensions.getSeveritySelectedValue


class AddSymptomDialog : BaseBottomSheetFragment<DialogAddSymptomBinding>(), OnClickListener {

    private lateinit var conditionSymptomsAdapter: GenericListAdapter<AddSymptomsItem, RowSymptomsSettingsBinding>

    private val viewModel: ConditionsViewModel by activityViewModels()
    private lateinit var model: ConditionInfo
    private lateinit var type: String
    private var addSymptomRequestModel = AddSymptomRequest()
    private lateinit var symptomItem: SymptomInfosItem
    private var symptomsRequestItemList = ArrayList<AddSymptomsItem>()

    companion object {
        fun newInstance(conditionModel: ConditionInfo, type: String): AddSymptomDialog {
            return AddSymptomDialog().apply {
                val bundle = Bundle().apply {
                    putString(Constants.TYPE, type)
                    putString(Constants.MODEL, Gson().toJson(conditionModel))
                }
                arguments = bundle
            }
        }
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            type = args.getString(Constants.TYPE).toString()
            model = Gson().fromJson(args.getString(Constants.MODEL), ConditionInfo::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): DialogAddSymptomBinding = DialogAddSymptomBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun initView() {

        binding?.tvConditionName?.text = getString(R.string.add_symptom_dialog_title, model.name)
        conditionSymptomsAdapter = GenericListAdapter(
            R.layout.row_symptoms_settings,
            object : GenericRecyclerBindingInterface<RowSymptomsSettingsBinding, AddSymptomsItem> {
                override fun bindData(
                    binder: RowSymptomsSettingsBinding, model: AddSymptomsItem, adapterPosition: Int
                ) {
                    binder.model = model

                    when (model.severity) {
                        "1" -> binder.tbSymptoms.rbNone.isChecked = true
                        "2" -> binder.tbSymptoms.rbMild.isChecked = true
                        "3" -> binder.tbSymptoms.rbModerate.isChecked = true
                        "4" -> binder.tbSymptoms.rbSevere.isChecked = true
                    }

                    if (context != null) {
                        binder.spinnerPrivacySettings.addHintWithArray(
                            context!!, Constants.privacySettingList
                        )
                    }
                    if (model.privacy == Constants.PLM) {
                        binder.spinnerPrivacySettings.setSelection(0)
                    } else {
                        binder.spinnerPrivacySettings.setSelection(1)
                    }

                    binder.ivPrivacyInfo.setOnClickListener {
                        PrivacySettingsInfoDialog.newInstance()
                            .show(childFragmentManager, getString(R.string.privacy_settings))
                    }

                    binder.tvPrivacySettings.text = getString(R.string.privacy_settings_for_symptom, model.name)

                    binder.tbSymptoms.rgSeverity.setOnCheckedChangeListener { radioGroup, i ->
                        val index: Int = binder.tbSymptoms.rgSeverity.indexOfChild(
                            binder.tbSymptoms.rgSeverity.findViewById(binder.tbSymptoms.rgSeverity.getCheckedRadioButtonId())
                        )
                        symptomsRequestItemList[adapterPosition].severity =
                            getSeveritySelectedValue(index)
                    }

                    binder.spinnerPrivacySettings.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?, view: View?, position: Int, id: Long
                            ) {
                                var privacy = ""
                                if (binder.spinnerPrivacySettings.selectedItem.toString() == Constants.SHARE_WITH_COMMUNITY) {
                                    privacy = Constants.PLM
                                } else if (binder.spinnerPrivacySettings.selectedItem.toString() == Constants.PERSONAL) {
                                    privacy = Constants.PERSONAL_SMALL
                                }
                                symptomsRequestItemList.get(adapterPosition).privacy = privacy
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    binder.executePendingBindings()
                }
            })
        binding?.rvSymptoms?.adapter = conditionSymptomsAdapter

        binding?.acSearchSymptom?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length > 1) {
                    searchSymptom(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding?.acSearchSymptom?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, arg1, pos, id ->
                dismissDropDown()
                symptomItem = parent?.getItemAtPosition(pos) as SymptomInfosItem
            }
        binding?.ivClose?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)
        binding?.btnAddSymptom?.setOnClickListener(this)
        binding?.btnCancel?.setOnClickListener(this)

        getUserSelectedSymptoms()
    }

    private fun getUserSelectedSymptoms() {
        binding?.rvSymptoms?.adapter = conditionSymptomsAdapter
        for (item in model.symptoms!!) {
            val obj = AddSymptomsItem()
            obj.id = item.id
            obj.privacy = item.privacy
            obj.name = item.name
            obj.other = false
            obj.severity = item.history?.get(item.history.lastIndex)?.severity.toString()
            obj.historyId = item.history?.get(item.history.lastIndex)?.id
            symptomsRequestItemList.add(obj)
        }
        conditionSymptomsAdapter.submitList(symptomsRequestItemList)
    }

    private fun searchSymptom(query: String) {
        viewModel.getSearchSymptomsCondition(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        updateSearchSymptom(it.data?.data?.symptomInfos)
                    }
                }
            }
        }
    }

    private fun updateSearchSymptom(data: ArrayList<SymptomInfosItem>?) {
        activity?.let { it1 ->
            if (!data.isNullOrEmpty()) {
                binding?.acSearchSymptom?.addHintWithArrayList(it1, data)
            }
        }
    }

    private fun updateSymptomsAdapter(item: SymptomInfosItem) {
        val addItem = AddSymptomsItem(
            id = item.id, privacy = "plm", name = item.name, severity = "", other = true,
            historyId = 0
        )
        addItemInList(
            addItem
        )
        binding?.acSearchSymptom?.text = null
        symptomsRequestItemList.add(addItem)
    }

    fun addItemInList(model: AddSymptomsItem) {
        val list: MutableList<AddSymptomsItem> = mutableListOf()
        list.addAll(conditionSymptomsAdapter.currentList)
        list.add(model)
        conditionSymptomsAdapter.submitList(list)
    }

    private fun dismissDropDown() {
        if (isAdded) {
            binding?.acSearchSymptom?.post { binding?.acSearchSymptom?.dismissDropDown() }
        }
    }

    override fun onClick(view: View?) {
        if (!isAdded) {
            return
        }
        when (view?.id) {
            R.id.iv_close -> {
                dismiss()
            }
            R.id.btn_save -> {
                validateData()
            }
            R.id.btn_add_symptom -> {
                if (this::symptomItem.isInitialized) {
                    if (binding?.acSearchSymptom?.text?.isEmpty() == false){
                        updateSymptomsAdapter(symptomItem)
                    }
                }
            }
            R.id.btn_cancel -> {
                dismiss()
            }
        }
    }

    fun validateData() {

        val severityValues = symptomsRequestItemList.map { it.severity }
//        if (severityValues.contains(null)) {
//            showError(getString(R.string.must_add_a_severity_level))
//            return
//        } else {
//            addSymptomRequestModel.symptoms = symptomsRequestItemList
//        }
        addSymptomRequestModel.symptoms = symptomsRequestItemList
        if (severityValues.size == 0) {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        hideError()
        addSymptomRequestModel.id = model.id
        addSymptomRequestModel.symptoms = symptomsRequestItemList

        (parentFragment as ConditionDetailsFragment).addSymptom(addSymptomRequestModel)
        dismiss()
    }

    private fun showError(message: String?) {
        binding?.llError?.visibility = View.VISIBLE
        binding?.tvError?.text = message
    }

    private fun hideError() {
        binding?.llError?.visibility = View.GONE
    }
}