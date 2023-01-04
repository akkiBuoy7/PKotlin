package plm.patientslikeme2.ui.main.view.dialog.conditions

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.conditions.*
import plm.patientslikeme2.data.model.signup.Conditions
import plm.patientslikeme2.data.model.signup.ConditionsStage
import plm.patientslikeme2.databinding.DialogAddAConditionBinding
import plm.patientslikeme2.databinding.RowConditionSymptomsBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.dialog.signup.PrivacySettingsInfoDialog
import plm.patientslikeme2.ui.main.view.fragment.conditions.ConditionDetailsFragment
import plm.patientslikeme2.ui.main.view.fragment.conditions.MyConditionsFragment
import plm.patientslikeme2.ui.main.viewmodel.conditions.ConditionsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.PERSONAL
import plm.patientslikeme2.utils.Constants.PERSONAL_SMALL
import plm.patientslikeme2.utils.Constants.PLM
import plm.patientslikeme2.utils.Constants.SHARE_WITH_COMMUNITY
import plm.patientslikeme2.utils.Constants.privacySettingList
import plm.patientslikeme2.utils.extensions.*
import java.util.*

class AddConditionDialog : BaseBottomSheetFragment<DialogAddAConditionBinding>(), OnClickListener {

    private lateinit var conditionSymptomsAdapter: GenericListAdapter<SuggestedSymptomItem, RowConditionSymptomsBinding>

    private val viewModel: ConditionsViewModel by activityViewModels()

    private var conditionRequestModel = AddConditionRequest()
    private var symptomsRequestItemList = ArrayList<AddSymptomsItem>()
    private lateinit var otherSymptomItem: SymptomInfosItem
    private var stageId = ""

    companion object {
        fun newInstance(model: ConditionInfo, type: String): AddConditionDialog {
            return AddConditionDialog().apply {
                val bundle = Bundle().apply {
                    putString(Constants.TYPE, type)
                    putString(Constants.MODEL, Gson().toJson(model))
                }
                arguments = bundle
            }
        }
    }

    private lateinit var model: ConditionInfo
    private lateinit var type: String

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            type = args.getString(Constants.TYPE).toString()
            model = if (type == Constants.EDIT) {
                Gson().fromJson(args.getString(Constants.MODEL), ConditionInfo::class.java)
            } else {
                ConditionInfo()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): DialogAddAConditionBinding = DialogAddAConditionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding?.ivClose?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)
        binding?.btnCancel?.setOnClickListener(this)
        binding?.tvSymptomsDay?.setOnClickListener(this)
        binding?.tvSymptomsMonth?.setOnClickListener(this)
        binding?.tvSymptomsYear?.setOnClickListener(this)
        binding?.cbStatus?.setOnClickListener(this)
        binding?.tvDay?.setOnClickListener(this)
        binding?.tvMonth?.setOnClickListener(this)
        binding?.tvYear?.setOnClickListener(this)
        binding?.btnAddSymptom?.setOnClickListener(this)
        binding?.ivPrivacyInfo?.setOnClickListener(this)

        val privacySettingList = privacySettingList
        activity?.let {
            binding?.spinnerPrivacySettings?.addHintWithArray(it, privacySettingList)
        }

        conditionSymptomsAdapter = GenericListAdapter(R.layout.row_condition_symptoms, object :
            GenericRecyclerBindingInterface<RowConditionSymptomsBinding, SuggestedSymptomItem> {
            override fun bindData(
                binder: RowConditionSymptomsBinding,
                model: SuggestedSymptomItem,
                position: Int
            ) {
                binder.model = model

                binder.tbSymptoms.rgSeverity.setOnCheckedChangeListener { radioGroup, i ->
                    val index: Int = binder.tbSymptoms.rgSeverity.indexOfChild(
                        binder.tbSymptoms.rgSeverity.findViewById(binder.tbSymptoms.rgSeverity.checkedRadioButtonId)
                    )
                    symptomsRequestItemList[position].severity =
                        getSeveritySelectedValue(index)
                }

                binder.executePendingBindings()
            }

        })
        binding?.rvSymptoms?.adapter = conditionSymptomsAdapter

        if (type == Constants.EDIT) {
            editDataSetup()
        } else {
            binding?.spinnerPrivacySettings?.setSelection(0)
        }



        binding?.edtStage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val item = parent?.getItemAtPosition(position) as ConditionsStage
                stageId = item.id.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding?.acSearchCondition?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length > 1) {
                    searchCondition(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

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

        binding?.acSearchCondition?.onItemClickListener =
            OnItemClickListener { parent, arg1, pos, id ->
                dismissConditionDropDown()
                val item = parent?.getItemAtPosition(pos) as Conditions
                conditionRequestModel.conditionId = item.id
                suggestedSymptoms(item.id.toString())
                relatedStages(item.id.toString())
            }

        binding?.acSearchSymptom?.onItemClickListener =
            OnItemClickListener { parent, arg1, pos, id ->
                dismissSymptomDropDown()
                otherSymptomItem = parent?.getItemAtPosition(pos) as SymptomInfosItem
            }

        if (!TextUtils.isEmpty(model.privacy)) {
            if (model.privacy == PLM) {
                binding?.spinnerPrivacySettings?.setSelection(0)
            } else if (model.privacy == PERSONAL_SMALL) {
                binding?.spinnerPrivacySettings?.setSelection(1)
            }
        }
    }

    private fun editDataSetup() {

        binding?.tvHeader?.text = model.name?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }

        if (model.diagnosedSince != null) {
            binding?.cbStatus?.isChecked = true
            binding?.rlDateOfDisgnosis?.visibility = VISIBLE
        } else {
            binding?.cbStatus?.isChecked = false
            binding?.rlDateOfDisgnosis?.visibility = GONE
        }

        val dateList = model.firstSymptomDate?.toString()?.split("-")
        dateList?.let {
            binding?.tvSymptomsYear?.text = it[0]
            binding?.tvSymptomsMonth?.text = getMonthName(it[1].toInt())
            binding?.tvSymptomsDay?.text = it[2]
        }

        val dateList1 = model.diagnosedSince?.toString()?.split("-")
        dateList1?.let {
            binding?.tvYear?.text = it[0]
            binding?.tvMonth?.text = getMonthName(it[1].toInt())
            binding?.tvDay?.text = it[2]
        }

        if (!TextUtils.isEmpty(model.privacy)) {
            if (model.privacy == PLM) {
                binding?.spinnerPrivacySettings?.setSelection(0)
            } else if (model.privacy == PERSONAL_SMALL) {
                binding?.spinnerPrivacySettings?.setSelection(1)
            }
        }
        binding?.llCondition?.visibility = GONE
        binding?.llListSymptoms?.visibility = GONE
        binding?.llStage?.visibility = GONE
        binding?.viewDivider1?.visibility = GONE
        binding?.btnSave?.text = getString(R.string.update)
    }

    private fun searchCondition(query: String) {
        viewModel.getSearchCondition(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        updateSearchConditions(it.data?.data)
                    }
                }
            }
        }
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

    private fun updateSearchConditions(data: ArrayList<Conditions>?) {
        activity?.let { it1 ->
            if (!data.isNullOrEmpty()) {
                binding?.acSearchCondition?.addHintWithArrayList(it1, data)
            }
        }
    }

    private fun addOtherSymptoms(item: SymptomInfosItem) {
        addItemInList(SuggestedSymptomItem(id = item.id, name = item.name))
        symptomsRequestItemList.add(AddSymptomsItem(severity = "", other = true, privacy = "plm", id = item.id))
        binding?.acSearchSymptom?.text = null
    }

    fun addItemInList(model: SuggestedSymptomItem) {
        val list: MutableList<SuggestedSymptomItem> = mutableListOf()
        list.addAll(conditionSymptomsAdapter.currentList)
        list.add(model)
        conditionSymptomsAdapter.submitList(list)
    }

    private fun suggestedSymptoms(query: String) {
        viewModel.getSuggestedSymptoms(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        updateSymptoms(it.data?.data?.symptoms)
                    }
                }
            }
        }
    }

    private fun updateSymptoms(data: List<SuggestedSymptomItem>?) {
        binding?.rvSymptoms?.adapter = conditionSymptomsAdapter
        conditionSymptomsAdapter.submitList(data)
        symptomsRequestItemList = ArrayList()
        data?.forEach {
            symptomsRequestItemList.add(AddSymptomsItem(other = false, privacy = "plm", id = it.id, severity = ""))
        }
    }

    private fun relatedStages(query: String) {
        viewModel.getRelatedStages(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        binding?.llStage?.visibility = VISIBLE
                        updateStages(it.data?.data!!)
                    }
                }
                else -> {
                    binding?.edtStage?.adapter = null
                    binding?.llStage?.visibility = GONE
                }
            }
        }
    }

    private fun updateStages(data: ArrayList<ConditionsStage>) {
        if (data.size > 0) {
            binding?.llStage?.visibility = VISIBLE
            data.add(ConditionsStage(0, getString(R.string.please_select)))
        }
        context?.let {
            binding?.edtStage?.addHintWithArrayList(it, data)
        }
    }

    private fun showDatePicker() {
        showDOBDatePicker(activity, binding?.tvDay, binding?.tvMonth, binding?.tvYear)
    }

    private fun showDatePickerSymptoms() {
        showDOBDatePicker(
            activity, binding?.tvSymptomsDay, binding?.tvSymptomsMonth, binding?.tvSymptomsYear
        )
    }

    override fun onClick(view: View?) {
        if (!isAdded) {
            return
        }
        when (view?.id) {
            R.id.iv_close -> {
                validateDataBeforeClosing()
            }
            R.id.btn_save -> {
                if (type == Constants.EDIT) {
                    validateEditData()
                } else {
                    validateAddData()
                }
            }
            R.id.tv_symptoms_day -> {
                showDatePickerSymptoms()
            }
            R.id.tv_symptoms_month -> {
                showDatePickerSymptoms()
            }
            R.id.tv_symptoms_year -> {
                showDatePickerSymptoms()
            }
            R.id.tv_day -> {
                showDatePicker()
            }
            R.id.tv_month -> {
                showDatePicker()
            }
            R.id.tv_year -> {
                showDatePicker()
            }
            R.id.cb_status -> {
                binding?.rlDateOfDisgnosis?.isVisible = !binding?.rlDateOfDisgnosis?.isVisible!!
            }
            R.id.iv_privacy_info -> {
                PrivacySettingsInfoDialog.newInstance()
                    .show(childFragmentManager, getString(R.string.privacy_settings))
            }
            R.id.btn_add_symptom -> {
                if (this::otherSymptomItem.isInitialized) {
                    if (binding?.acSearchSymptom?.text?.isEmpty() == false){
                        addOtherSymptoms(otherSymptomItem)
                    }
                }
            }
            R.id.btn_cancel -> {
                validateDataBeforeClosing()
            }
        }
    }


    private fun validateAddData() {

        val condition = binding?.acSearchCondition?.text.toString()
        val symptomDate =
            "${binding?.tvSymptomsDay?.text}-${binding?.tvSymptomsMonth?.text}-${binding?.tvSymptomsYear?.text}"
        var diagnosisDate: String? = null
        if (binding?.cbStatus?.isChecked == true) {
            diagnosisDate =
                "${binding?.tvDay?.text}-${binding?.tvMonth?.text}-${binding?.tvYear?.text}"
        }
        val privacy = binding?.spinnerPrivacySettings?.selectedItem.toString()

        val severityValues = symptomsRequestItemList.map { it.severity }
//        if (severityValues.contains(null)) {
//            showError(getString(R.string.must_add_a_severity_level))
//            return
//        } else {
//            conditionRequestModel.symptoms = symptomsRequestItemList
//        }
        conditionRequestModel.symptoms = symptomsRequestItemList

        if (condition == "") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        if (binding?.cbStatus?.isChecked == true && diagnosisDate == "--") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }

//        val stage = binding?.edtStage?.selectedItem.toString()
//        if ((stage == getString(R.string.please_select))) {
//            showError(getString(R.string.error_all_inputs_required))
//            return
//        }

        diagnosisDate?.let {
            if (futureDateValidation(diagnosisDate) == -1) {
                showError(getString(R.string.date_can_not_be_in_future))
                return
            } else {
                conditionRequestModel.diagnosisDate = diagnosisDate
            }
        }

        if (symptomDate == "--") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        if (futureDateValidation(symptomDate) == -1) {
            showError(getString(R.string.date_can_not_be_in_future))
            return
        } else {
            conditionRequestModel.symptomDate = symptomDate
        }
        if (privacy.isEmpty()) {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        if (privacy == SHARE_WITH_COMMUNITY) {
            conditionRequestModel.privacy = PLM
        } else if (privacy == PERSONAL) {
            conditionRequestModel.privacy = PERSONAL_SMALL
        }

        val stage = binding?.edtStage?.selectedItem.toString()
        if ((stage == getString(R.string.please_select)) || stage.isEmpty()) {
            conditionRequestModel.stageId = null
        }else{
            conditionRequestModel.stageId = stageId
        }

        hideError()
        (parentFragment as MyConditionsFragment).addCondition(conditionRequestModel)
        dismiss()
    }

    private fun validateEditData() {

        var diagnosisDate: String? = null
        if (binding?.cbStatus?.isChecked == true) {
            diagnosisDate =
                "${binding?.tvDay?.text}-${binding?.tvMonth?.text}-${binding?.tvYear?.text}"
        }
        if (binding?.cbStatus?.isChecked == true && diagnosisDate == "--") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        diagnosisDate?.let {
            if (futureDateValidation(diagnosisDate) == -1) {
                showError(getString(R.string.date_can_not_be_in_future))
                return
            }
        }
        val symptomDate =
            "${binding?.tvSymptomsDay?.text}-${binding?.tvSymptomsMonth?.text}-${binding?.tvSymptomsYear?.text}"
        if (symptomDate == "--") {
            showError(getString(R.string.error_all_inputs_required))
            return
        } else {
            if (futureDateValidation(symptomDate) == -1) {
                showError(getString(R.string.date_can_not_be_in_future))
                return
            } else {
                conditionRequestModel.symptomDate = symptomDate
            }
        }
        val privacy = binding?.spinnerPrivacySettings?.selectedItem.toString()

        if (privacy.isEmpty()) {
            showError(getString(R.string.error_all_inputs_required))
            return
        }

        if (privacy == SHARE_WITH_COMMUNITY) {
            conditionRequestModel.privacy = PLM
        } else if (privacy == PERSONAL) {
            conditionRequestModel.privacy = PERSONAL_SMALL
        }
        conditionRequestModel.conditionId = model.conditionId
        conditionRequestModel.symptomDate = symptomDate
        if (binding?.cbStatus?.isChecked == true) {
            conditionRequestModel.diagnosisDate = diagnosisDate
        } else {
            conditionRequestModel.diagnosisDate = null
        }

        hideError()
        (parentFragment as ConditionDetailsFragment).editCondition(conditionRequestModel)
        dismiss()
    }

    private fun validateDataBeforeClosing() {
        if (!binding?.tvDay?.text?.isEmpty()!! || !binding?.acSearchCondition?.text.toString()
                .isEmpty() || !binding?.acSearchSymptom?.text.toString()
                .isEmpty() || !binding?.tvSymptomsDay?.text?.isEmpty()!! || binding?.cbStatus?.isChecked == true
        ) {
            openCloseConfirmationDialog()
        } else {
            dismiss()
        }
    }

    private fun dismissConditionDropDown() {
        if (isAdded) {
            binding?.acSearchCondition?.post { binding?.acSearchCondition?.dismissDropDown() }
        }
    }

    private fun dismissSymptomDropDown() {
        if (isAdded) {
            binding?.acSearchSymptom?.post { binding?.acSearchSymptom?.dismissDropDown() }
        }
    }

    private fun showError(message: String?) {
        binding?.llError?.visibility = VISIBLE
        binding?.tvError?.text = message
    }

    private fun hideError() {
        binding?.llError?.visibility = GONE
    }

    private fun openCloseConfirmationDialog() {
        activity?.let {
            showActionDialog(
                it,
                getString(R.string.discard_changes),
                getString(R.string.close_prompt),
                getString(R.string.ok),
                getString(R.string.cancel),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        dismiss()
                    }
                })
        }
    }
}