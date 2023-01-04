package plm.patientslikeme2.ui.main.view.dialog.treatments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.conditions.TreatmentsItem
import plm.patientslikeme2.data.model.treatments.*
import plm.patientslikeme2.databinding.DialogAddTreatmentBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.adapter.treatments.RemindersAdapter
import plm.patientslikeme2.ui.main.view.dialog.signup.PrivacySettingsInfoDialog
import plm.patientslikeme2.ui.main.view.fragment.conditions.ConditionDetailsFragment
import plm.patientslikeme2.ui.main.view.fragment.treatments.MyTreatmentsFragment
import plm.patientslikeme2.ui.main.viewmodel.treatments.TreatmentsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.AM
import plm.patientslikeme2.utils.Constants.PERSONAL
import plm.patientslikeme2.utils.Constants.PERSONAL_SMALL
import plm.patientslikeme2.utils.Constants.PLM
import plm.patientslikeme2.utils.Constants.PM
import plm.patientslikeme2.utils.Constants.PURPOSE_CONDITION
import plm.patientslikeme2.utils.Constants.PURPOSE_CONDITION_INFO
import plm.patientslikeme2.utils.Constants.PURPOSE_SYMPTOM
import plm.patientslikeme2.utils.Constants.PURPOSE_SYMPTOM_INFO
import plm.patientslikeme2.utils.Constants.PURPOSE_TREATMENT_PURPOSE
import plm.patientslikeme2.utils.Constants.SHARE_WITH_COMMUNITY
import plm.patientslikeme2.utils.Constants.privacySettingList
import plm.patientslikeme2.utils.extensions.*

class AddTreatmentDialog : BaseBottomSheetFragment<DialogAddTreatmentBinding>(), OnClickListener {

    private val viewModel: TreatmentsViewModel by activityViewModels()
    private var selectedFrequencyId: String? = ""
    private var selectedDosageId: String? = ""
    private var selectedDosageDescription: String? = ""
    lateinit var remindersAdapter: RemindersAdapter
    private var reminderItemList = ArrayList<ReminderNotificationsAttributesItem>()
    private var isDefault = false
    private var addTreatmentRequest = AddTreatmentRequest()
    private var dosageAttributesItem = DoseAttributes()
    private var treatmentDosageHistoriesAttributesItem = TreatmentDosageHistoriesAttributesItem()
    private var purposes: ArrayList<AddPurposesItem>? = null
    private var treatmentHistory = TreatmentHistory()
    private var treatmentId: Int? = null
    private var conditionInfoDetailsId: Int? = null
    private var reminderId: Int = 0
    private lateinit var editTreatmentModel: GetTreatmentResponse
    private var privacy: String = ""

    companion object {
        fun newInstance(
            model: TreatmentsItem, type: String, conditionInfo: Int? = null
        ): AddTreatmentDialog {
            return AddTreatmentDialog().apply {
                val bundle = Bundle().apply {
                    putString(Constants.TYPE, type)
                    putString(Constants.MODEL, Gson().toJson(model))
                    if (conditionInfo != null) {
                        putInt(Constants.CONDITION_INFO, conditionInfo)
                    }
                }
                arguments = bundle
            }
        }
    }

    private lateinit var model: TreatmentsItem
    private lateinit var type: String

    override fun setArguments(args: Bundle?) {
        args?.let {
            type = args.getString(Constants.TYPE).toString()
            if (args.getInt(Constants.CONDITION_INFO) != 0) {
                conditionInfoDetailsId = args.getInt(Constants.CONDITION_INFO)
            }

            model = if (type == Constants.EDIT) {
                Gson().fromJson<TreatmentsItem>(args.getString(Constants.MODEL)!!) as TreatmentsItem
            } else {
                TreatmentsItem()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): DialogAddTreatmentBinding = DialogAddTreatmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }


    private fun initView() {

        binding?.ivClose?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)
        binding?.tvDay?.setOnClickListener(this)
        binding?.tvMonth?.setOnClickListener(this)
        binding?.tvYear?.setOnClickListener(this)
        binding?.btnCancel?.setOnClickListener(this)
        binding?.tvAddAnotherReminder?.setOnClickListener(this)
        binding?.ivPrivacyInfo?.setOnClickListener(this)

        val privacySettingList = privacySettingList
        activity?.let {
            binding?.spinnerPrivacySettings?.addHintWithArray(it, privacySettingList)
        }

        if (type == Constants.EDIT) {
            if (isConnected()) bindObserver()
        } else {
            binding?.spinnerPrivacySettings?.setSelection(0)
        }

        binding?.acTreatmentName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length > 1) {
                    searchTreatment(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding?.acTreatmentName?.onItemClickListener = AdapterView.OnItemClickListener { parent, arg1, pos, id ->
            dismissTreatmentDropDown()
            val item = parent?.getItemAtPosition(pos) as SearchTreatmentItem
            treatmentId = item.id
            getTreatmentCategory(item.id.toString(), item.treatmentCategoryId.toString())
            hideSoftKeyboardInput()
        }

        binding?.acSearchPurpose?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length > 1) {
                    suggestedPurpose(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding?.acSearchPurpose?.onItemClickListener = AdapterView.OnItemClickListener { parent, arg1, pos, id ->
            dismissPurposeDropDown()
            val item = parent?.getItemAtPosition(pos) as ResultsItem
            createPurposeRequest(item.data?.type, item.data?.id)
            suggestedPurpose(item.data?.id.toString())
            hideSoftKeyboardInput()
        }

        binding?.edtFrequency?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val item = parent?.getItemAtPosition(position) as FrequencyUnitsItem
                selectedFrequencyId = item.id.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding?.edtDosage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (isDefault) {
                    val item = parent?.getItemAtPosition(position) as DefaultDosageUnitsItem
                    selectedDosageId = item.id.toString()
                    selectedDosageDescription = item.description

                } else {
                    val item = parent?.getItemAtPosition(position) as DosageUnitsItem
                    selectedDosageId = item.id.toString()
                    selectedDosageDescription = item.description
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        remindersAdapter = RemindersAdapter(context)
        binding?.rvReminders?.adapter = remindersAdapter
        remindersAdapter.onItemReminderClicked = { isnoti, type, position, time, mode ->
            var finalTime = time
            if (mode == PM) {
                var hour = time.split(":")[0].toInt()
                val minute = time.split(":")[1]
                if (hour==12){
                    hour = hour
                }else{
                    hour += 12
                }
                finalTime = "$hour:$minute"
            }
            if (mode== AM){
                var hour = time.split(":")[0].toInt()
                val minute = time.split(":")[1]
                if (hour==12){
                    hour = 0
                }
                finalTime = "$hour:$minute"
            }
            reminderItemList.get(position).isNotify = isnoti
            reminderItemList.get(position).notifyTime = finalTime

            Log.e("Reminder", "initView: $isnoti, $type, $position, $finalTime, $mode" )
        }

    }


    private fun bindObserver() {

        var id = ""
        // need to use model.id when coming from manage conditions
        if (parentFragment is MyTreatmentsFragment) {
            id = model.treatmentId.toString()
        } else {
            id = model.id.toString()
        }
        viewModel.getEditTreatments(id).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data != null) {
                        editTreatmentModel = it.data!!
                        editDataSetup()
                        binding?.parent?.visibility = View.VISIBLE
                    } else {

                    }
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun editDataSetup() {

        binding?.tvHeader?.text = model.name
        binding?.llTreatment?.visibility = View.GONE
        binding?.rvReminders?.visibility = View.VISIBLE
        binding?.btnSave?.text = getString(R.string.update)
        binding?.tvCount?.text = getString(R.string.count)

        if (editTreatmentModel.data?.treatmentHistory?.purposes?.size!! > 0) {
            binding?.acSearchPurpose?.text = editTreatmentModel.data!!.treatmentHistory?.purposes?.get(0)?.name?.toEditable()
        }

        isDefault = editTreatmentModel.data!!.treatmentHistory?.treatmentCategory?.dosageUnits?.size!! <= 0

        val dateList = editTreatmentModel.data!!.treatmentHistory?.treatmentDosageHistory?.get(0)?.startDate.toString().split("-")
        dateList.let {
            binding?.tvYear?.text = it[0]
            binding?.tvMonth?.text = getMonthName(it[1].toInt())
            binding?.tvDay?.text = it[2]
        }
        editTreatmentModel.data!!.treatmentHistory?.let { treatmentHistory ->

            if (!TextUtils.isEmpty(treatmentHistory.privacy)) {
                if (treatmentHistory.privacy == PLM) {
                    binding?.spinnerPrivacySettings?.setSelection(0)
                } else if (treatmentHistory.privacy == PERSONAL_SMALL) {
                    binding?.spinnerPrivacySettings?.setSelection(1)
                }
            }

            if (isDefault) {
                val defaultDosageList = treatmentHistory.treatmentCategory?.defaultDosageUnits?.toMutableList()
                defaultDosageList?.add(
                    DefaultDosageUnitsItem(
                        id = treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.strengthNumUnit?.id.toString(),
                        description = treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.strengthNumUnit?.description.toString()
                    )
                )
                context?.let { it1 ->
                    binding?.edtDosage?.addItemSelectedAdapter(
                        it1,
                        defaultDosageList as ArrayList<Any>,
                    )
                }

                if (!treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.strengthNumAmount.isNullOrEmpty()) {
                    binding?.edtCount?.text =
                        treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.strengthNumAmount?.toEditable()
                }
            } else {
                val dosageList = treatmentHistory.treatmentCategory?.dosageUnits?.toMutableList()
                dosageList?.add(
                    DosageUnitsItem(
                        id = treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.structuredDosage?.id.toString(),
                        description = treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.structuredDosage?.description.toString()
                    )
                )
                context?.let { it1 ->
                    binding?.edtDosage?.addItemSelectedAdapter(
                        it1,
                        dosageList as ArrayList<Any>,
                    )
                }
                treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.strengthNumUnitId.toString()

                binding?.edtCount?.text = treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.quantity?.toEditable()
            }

            val frequencyList = treatmentHistory.treatmentCategory?.frequencyUnits?.toMutableList()
            frequencyList?.add(
                FrequencyUnitsItem(
                    id = treatmentHistory.treatmentDosageHistory?.get(
                        0
                    )?.drugdbFrequencyUnit?.id, displayName = treatmentHistory.treatmentDosageHistory?.get(0)?.drugdbFrequencyUnit?.displayName
                )
            )
            context?.let { it1 ->
                binding?.edtFrequency?.addItemSelectedAdapter(
                    it1,
                    frequencyList as ArrayList<Any>,
                )
            }
            binding?.cbStatus?.isChecked = treatmentHistory.treatmentDosageHistory?.get(0)?.doses?.get(0)?.asNeeded == true

            remindersAdapter.isEdit = true
            remindersAdapter.submitList(treatmentHistory.reminderNotificationsAttributes)
            reminderItemList.addAll(treatmentHistory.reminderNotificationsAttributes!!)
        }

        selectedFrequencyId = editTreatmentModel.data!!.treatmentHistory?.treatmentDosageHistory?.get(0)?.drugdbFrequencyUnit?.id.toString()
        privacy = editTreatmentModel.data!!.treatmentHistory?.privacy.toString()
        treatmentId = editTreatmentModel.data!!.treatmentHistory?.treatmentId

        if (isDefault) {
            selectedDosageId = editTreatmentModel.data!!.treatmentHistory?.treatmentDosageHistory?.get(0)?.doses?.get(
                0
            )?.strengthNumUnit?.id.toString()
        } else {
            selectedDosageId = editTreatmentModel.data!!.treatmentHistory?.treatmentDosageHistory?.get(0)?.doses?.get(
                0
            )?.structuredDosage?.id.toString()
            selectedDosageDescription = editTreatmentModel.data!!.treatmentHistory?.treatmentDosageHistory?.get(0)?.doses?.get(
                0
            )?.structuredDosage?.description.toString()
        }

        purposes = ArrayList()
        if (editTreatmentModel.data!!.treatmentHistory?.purposes?.size!! > 0) {
            purposes?.add(
                AddPurposesItem(
                    attributionId = editTreatmentModel.data!!.treatmentHistory?.purposes?.get(
                        0
                    )?.attributionId
                )
            )
        }
    }

    private fun suggestedPurpose(query: String) {

        viewModel.getSuggestedPurpose(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        updatePurpose(it.data?.data?.results)
                    }
                }
            }
        }
    }

    private fun getTreatmentCategory(treatmentId: String, categoryId: String) {

        viewModel.getTreatmentCategory(treatmentId, categoryId).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        binding?.edtDosage?.setSelection(Adapter.NO_SELECTION, true)
                        it.data!!.data?.treatmentCategories?.frequencyUnits?.let { it1 ->
                            updateFrequency(
                                it1
                            )
                        }
                        if (it.data!!.data?.treatmentCategories?.dosageUnits?.size!! > 0) {
                            isDefault = false
                            it.data!!.data?.treatmentCategories?.dosageUnits?.let { it1 ->
                                updateDosage(
                                    it1
                                )
                            }
                        } else {
                            isDefault = true
                            it.data!!.data?.treatmentCategories?.defaultDosageUnits?.let { it1 ->
                                updateDefaultDosage(
                                    it1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateFrequency(list: ArrayList<FrequencyUnitsItem>) {
        list.add(FrequencyUnitsItem(0, ""))
        context?.let {
            binding?.edtFrequency?.addItemSelectedAdapter(it, list)
        }
    }

    private fun updateDosage(list: ArrayList<DosageUnitsItem>) {
        list.add(DosageUnitsItem("", ""))
        context?.let {
            binding?.edtDosage?.addItemSelectedAdapter(it, list)
        }
    }

    private fun updateDefaultDosage(list: ArrayList<DefaultDosageUnitsItem>) {
        list.add(DefaultDosageUnitsItem("", ""))
        context?.let {
            binding?.edtDosage?.addItemSelectedAdapter(it, list)
        }
    }

    private fun updatePurpose(data: ArrayList<ResultsItem>?) {
        activity?.let { it1 ->
            if (!data.isNullOrEmpty()) {
                binding?.acSearchPurpose?.addHintWithArrayList(it1, data)
            }
        }
    }

    private fun searchTreatment(query: String) {
        viewModel.getSearchTreatments(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        updateSearchTreatments(it.data?.data?.treatment)
                    }
                }
            }
        }
    }

    private fun updateSearchTreatments(data: ArrayList<SearchTreatmentItem>?) {
        activity?.let { it1 ->
            if (!data.isNullOrEmpty()) {
                binding?.acTreatmentName?.addHintWithArrayList(it1, data)
            }
        }
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
                if (type == Constants.ADD) {
                    validateAddData()
                } else {
                    validateEditData()
                }
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
            R.id.iv_privacy_info -> {
                PrivacySettingsInfoDialog.newInstance().show(childFragmentManager, getString(R.string.privacy_settings))
            }
            R.id.tv_add_another_reminder -> {
                remindersAdapter.isEdit = false
                reminderId = reminderId.plus(1)
                //binding?.rvReminders?.adapter = remindersAdapter
                remindersAdapter.addItemInList(
                    ReminderNotificationsAttributesItem(
                        reminder_id = reminderId, isNotify = false
                    )
                )
                reminderItemList.add(
                    ReminderNotificationsAttributesItem(
                        reminder_id = reminderId, isNotify = false
                    )
                )
            }
            R.id.btn_cancel -> {
                validateDataBeforeClosing()
            }
        }
    }

    private fun validateDataBeforeClosing() {
        if (!binding?.tvDay?.text?.isEmpty()!! || !binding?.acTreatmentName?.text.toString().isEmpty() || !binding?.acSearchPurpose?.text.toString()
                .isEmpty() || !binding?.edtCount?.text?.isEmpty()!! || binding?.edtDosage?.selectedItem.toString() != "null" || binding?.edtFrequency?.selectedItem.toString() != "null" || binding?.cbStatus?.isChecked == true
        ) {
            openCloseConfirmationDialog()
        } else {
            dismiss()
        }
    }

    private fun dismissTreatmentDropDown() {
        if (isAdded) {
            binding?.acTreatmentName?.post { binding?.acTreatmentName?.dismissDropDown() }
        }
    }

    private fun dismissPurposeDropDown() {
        if (isAdded) {
            binding?.acSearchPurpose?.post { binding?.acSearchPurpose?.dismissDropDown() }
        }
    }

    private fun showDatePicker() {
        hideSoftKeyboardInput()
        showDOBDatePicker(activity, binding?.tvDay, binding?.tvMonth, binding?.tvYear)
    }

    private fun validateAddData() {

        if (type == Constants.ADD) {
            val treatment = binding?.acTreatmentName?.text.toString()
            if (treatment.isEmpty()) {
                showError(getString(R.string.error_all_inputs_required))
                return
            }
        }

        val purpose = binding?.acSearchPurpose?.text.toString()
        if (purpose.isEmpty()) {
            showError(getString(R.string.you_must_add_a_purpose))
            return
        }
        val treatmentDate = "${binding?.tvDay?.text}-${binding?.tvMonth?.text}-${binding?.tvYear?.text}"
        if (treatmentDate == "--") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        if (futureDateValidation(treatmentDate) == -1) {
            showError(getString(R.string.date_can_not_be_in_future))
            return
        }

        val count = binding?.edtCount?.text.toString()
        if (count.isEmpty()) {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        val dosage = binding?.edtDosage?.selectedItem.toString()
        if (dosage == "") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        val frequency = binding?.edtFrequency?.selectedItem.toString()
        if (frequency == "") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        privacy = binding?.spinnerPrivacySettings?.selectedItem.toString()
        if (privacy.isEmpty()) {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        hideError()

        if (binding?.spinnerPrivacySettings?.selectedItem.toString() == SHARE_WITH_COMMUNITY) {
            privacy = PLM
        } else if (binding?.spinnerPrivacySettings?.selectedItem.toString() == PERSONAL) {
            privacy = PERSONAL_SMALL
        }

        treatmentDosageHistoriesAttributesItem.drugdbFrequencyUnitId = selectedFrequencyId
        treatmentHistory.privacy = privacy
        treatmentHistory.treatmentId = treatmentId
        treatmentHistory.conditionInfoId = conditionInfoDetailsId

        dosageAttributesItem.asNeeded = binding?.cbStatus?.isChecked
        if (isDefault) {
            dosageAttributesItem.strengthNumAmount = binding?.edtCount?.text.toString()
            dosageAttributesItem.useOtherDosage = getString(R.string.other)
            dosageAttributesItem.strengthNumUnitId = selectedDosageId
        } else {
            dosageAttributesItem.quantity = binding?.edtCount?.text.toString()
            dosageAttributesItem.useOtherDosage = getString(R.string.standard)
            dosageAttributesItem.strengthNumUnitId = selectedDosageDescription
            dosageAttributesItem.structuredDosageId = selectedDosageId
        }

        treatmentDosageHistoriesAttributesItem.doseAttributes = dosageAttributesItem
        treatmentHistory.treatmentDosageHistoriesAttributes?.add(
            treatmentDosageHistoriesAttributesItem
        )

        val dateHash = DateHash()
        dateHash.day = binding?.tvDay?.text.toString()
        dateHash.month = (getMonthNumber(binding?.tvMonth?.text.toString()) + 1).toString()
        dateHash.year = binding?.tvYear?.text.toString()

        treatmentDosageHistoriesAttributesItem.dateHash = dateHash
        treatmentHistory.purposes = purposes

        treatmentHistory.treatmentDosageHistoriesAttributes = arrayListOf(treatmentDosageHistoriesAttributesItem)
        treatmentHistory.reminderNotificationsAttributes = reminderItemList
        addTreatmentRequest.treatmentHistory = treatmentHistory
        if (parentFragment is MyTreatmentsFragment) {
            (parentFragment as MyTreatmentsFragment).addTreatment(addTreatmentRequest)
        } else {
            (parentFragment as ConditionDetailsFragment).addTreatment(addTreatmentRequest)
        }
        dismiss()
    }

    private fun validateEditData() {

        val purpose = binding?.acSearchPurpose?.text.toString()
        if (purpose.isEmpty()) {
            showError(getString(R.string.you_must_add_a_purpose))
            return
        }
        val treatmentDate = "${binding?.tvDay?.text}-${binding?.tvMonth?.text}-${binding?.tvYear?.text}"
        if (treatmentDate == "--") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        val count = binding?.edtCount?.text.toString()
        if (count.isEmpty()) {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        val dosage = binding?.edtDosage?.selectedItem.toString()
        if (dosage == "") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        val frequency = binding?.edtFrequency?.selectedItem.toString()
        if (frequency == "") {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        var privacy = binding?.spinnerPrivacySettings?.selectedItem.toString()
        if (privacy.isEmpty()) {
            showError(getString(R.string.error_all_inputs_required))
            return
        }
        hideError()

        if (binding?.spinnerPrivacySettings?.selectedItem.toString() == SHARE_WITH_COMMUNITY) {
            privacy = PLM
        } else if (binding?.spinnerPrivacySettings?.selectedItem.toString() == PERSONAL) {
            privacy = PERSONAL_SMALL
        }

        treatmentDosageHistoriesAttributesItem.drugdbFrequencyUnitId = selectedFrequencyId
        treatmentHistory.privacy = privacy
        treatmentHistory.treatmentId = treatmentId

        dosageAttributesItem.asNeeded = binding?.cbStatus?.isChecked
        if (isDefault) {
            dosageAttributesItem.strengthNumAmount = binding?.edtCount?.text.toString()
            dosageAttributesItem.useOtherDosage = getString(R.string.other)
            dosageAttributesItem.strengthNumUnitId = selectedDosageId
        } else {
            dosageAttributesItem.quantity = binding?.edtCount?.text.toString()
            dosageAttributesItem.useOtherDosage = getString(R.string.standard)
            dosageAttributesItem.strengthNumUnitId = selectedDosageDescription
            dosageAttributesItem.structuredDosageId = selectedDosageId
        }

        treatmentDosageHistoriesAttributesItem.doseAttributes = dosageAttributesItem
        treatmentHistory.treatmentDosageHistoriesAttributes?.add(
            treatmentDosageHistoriesAttributesItem
        )

        val dateHash = DateHash()
        dateHash.day = binding?.tvDay?.text.toString()
        dateHash.month = (getMonthNumber(binding?.tvMonth?.text.toString()) + 1).toString()
        dateHash.year = binding?.tvYear?.text.toString()

        treatmentDosageHistoriesAttributesItem.dateHash = dateHash
        treatmentHistory.purposes = purposes

        treatmentHistory.treatmentDosageHistoriesAttributes = arrayListOf(treatmentDosageHistoriesAttributesItem)
        treatmentHistory.reminderNotificationsAttributes = reminderItemList
        addTreatmentRequest.treatmentHistory = treatmentHistory

        if (parentFragment is MyTreatmentsFragment) {
            (parentFragment as MyTreatmentsFragment).editTreatment(

                addTreatmentRequest, editTreatmentModel.data?.treatmentHistory?.id!!
            )
        } else {
            (parentFragment as ConditionDetailsFragment).editTreatment(
                addTreatmentRequest, model.id!!
            )
        }
        dismiss()
    }

    fun createPurposeRequest(type: String?, id: Int?) {
        purposes = ArrayList()
        when (type) {
            PURPOSE_TREATMENT_PURPOSE -> {
                purposes!!.add(AddPurposesItem(id = id, type = type))
            }
            PURPOSE_CONDITION_INFO -> {
                purposes!!.add(AddPurposesItem(id = id, type = type))
            }
            PURPOSE_SYMPTOM_INFO -> {
                purposes!!.add(AddPurposesItem(id = id, type = type))
            }
            PURPOSE_CONDITION -> {
                purposes!!.add(AddPurposesItem(conditionId = id))
            }
            PURPOSE_SYMPTOM -> {
                purposes!!.add(AddPurposesItem(symptomId = id))
            }
        }
    }

    private fun openCloseConfirmationDialog() {
        activity?.let {
            showActionDialog(it,
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

    private fun showError(message: String?) {
        binding?.llError?.visibility = View.VISIBLE
        binding?.tvError?.text = message
    }

    private fun hideError() {
        binding?.llError?.visibility = View.GONE
    }
}