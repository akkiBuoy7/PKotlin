package plm.patientslikeme2.ui.main.view.dialog.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.signup.Conditions
import plm.patientslikeme2.data.model.signup.ConditionsStage
import plm.patientslikeme2.databinding.DialogAddConditionBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.view.fragment.signup.SignupConditionsFragment
import plm.patientslikeme2.ui.main.viewmodel.signup.SignupViewModel
import plm.patientslikeme2.utils.Constants.EDIT
import plm.patientslikeme2.utils.Constants.MODEL
import plm.patientslikeme2.utils.Constants.PERSONAL
import plm.patientslikeme2.utils.Constants.SHARE_WITH_COMMUNITY
import plm.patientslikeme2.utils.Constants.TYPE
import plm.patientslikeme2.utils.extensions.addHintWithArray
import plm.patientslikeme2.utils.extensions.addHintWithArrayList

class AddSignupConditionDialog : BaseBottomSheetFragment<DialogAddConditionBinding>(), OnClickListener,
    AdapterView.OnItemClickListener {

    companion object {

        fun newInstance(model: Conditions, type: String): AddSignupConditionDialog {
            return AddSignupConditionDialog().apply {
                val bundle = Bundle().apply {
                    putString(TYPE, type)
                    putString(MODEL, Gson().toJson(model))
                }
                arguments = bundle
            }
        }
    }

    private val viewModel: SignupViewModel by activityViewModels()

    private var model = Conditions()
    private lateinit var type: String

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DialogAddConditionBinding = DialogAddConditionBinding.inflate(inflater, container, false)

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            type = args.getString(TYPE).toString()
            if (type == EDIT) {
                model = Gson().fromJson(args.getString(MODEL), Conditions::class.java)
            } else {
                model = Conditions()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        binding?.ivClose?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.ivInfo?.setOnClickListener(this)
        binding?.btnCancel?.setOnClickListener(this)
        binding?.acConditions?.onItemClickListener = this
    }

    private fun initView() {
        val privacySettingList = arrayOf(
            SHARE_WITH_COMMUNITY,
            PERSONAL,
            getString(R.string.hint_please_select)
        )
        activity?.let {
            binding?.spinnerPrivacySettings?.addHintWithArray(it, privacySettingList)
        }
        if (type == EDIT) {
            binding?.acConditions?.setText(model.name)
            binding?.acConditions?.isEnabled = false
            binding?.btnAdd?.text = getString(R.string.save)
            if (!TextUtils.isEmpty(model.stage_id) && !TextUtils.isEmpty(model.stage_name)) {
                val stageList: ArrayList<ConditionsStage> = arrayListOf()
                val defaultSelectModel = ConditionsStage(0, getString(R.string.hint_please_select))
                val selectedStageModel = ConditionsStage(model.stage_id?.toInt(), model.stage_name)
                stageList.add(defaultSelectModel)
                stageList.add(selectedStageModel)
                activity?.let { binding?.spinnerStage?.addHintWithArrayList(it, stageList) }
                binding?.spinnerStage?.isEnabled = false
                binding?.tvStage?.visibility = View.VISIBLE
                binding?.spinnerStage?.visibility = View.VISIBLE
            }

            if (!TextUtils.isEmpty(model.privacy)) {
                if (model.privacy == "plm") {
                    binding?.spinnerPrivacySettings?.setSelection(0)
                } else if (model.privacy == "personal") {
                    binding?.spinnerPrivacySettings?.setSelection(1)
                }
            }
        } else {
            binding?.spinnerPrivacySettings?.setSelection(1)
        }

        binding?.acConditions?.addTextChangedListener(object : TextWatcher {
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
    }

    override fun onClick(view: View?) {
        if (!isAdded) {
            return
        }
        when (view?.id) {
            R.id.iv_close,
            R.id.btn_cancel -> {
                dismiss()
            }
            R.id.iv_info -> {
                PrivacySettingsInfoDialog.newInstance()
                    .show(childFragmentManager, getString(R.string.privacy_settings))
            }
            R.id.btn_add -> {
                validatedDetails()
            }
        }
    }

    private fun validatedDetails() {
        val conditions = binding?.acConditions?.text.toString()
        val stage = binding?.spinnerStage?.selectedItem.toString()
        val privacy = binding?.spinnerPrivacySettings?.selectedItem.toString()
        if (conditions.isEmpty()) {
            binding?.llErrorCondition?.visibility = View.VISIBLE
            binding?.tvConditionError?.text = getString(R.string.empty_condition)
            binding?.acConditions?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            return
        }
        binding?.llErrorCondition?.visibility = View.GONE
        binding?.acConditions?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        if (model.condition_id == null) {
            binding?.llErrorCondition?.visibility = View.VISIBLE
            binding?.tvConditionError?.text = getString(R.string.select_condition)
            binding?.acConditions?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            return
        }
        binding?.llErrorCondition?.visibility = View.GONE
        binding?.spinnerPrivacySettings?.setBackgroundResource(R.drawable.bg_spinner_down_gray)
        if (privacy == getString(R.string.please_select)) {
            binding?.spinnerPrivacySettings?.setBackgroundResource(R.drawable.bg_spinner_down_red)
            return
        }
        dismiss()

        if (binding?.spinnerStage?.selectedItem != null && binding?.spinnerStage?.selectedItem.toString() != "Please select") {
            val selectedStage = binding?.spinnerStage?.selectedItem as ConditionsStage
            model.stage_id = selectedStage.id.toString()
            model.stage_name = selectedStage.name
        }
        val privacyName = binding?.spinnerPrivacySettings?.selectedItem.toString()
        if (privacyName == SHARE_WITH_COMMUNITY) {
            model.privacy = "plm"
        } else if (privacyName == PERSONAL) {
            model.privacy = "personal"
        }
        if (type == EDIT) {
            (parentFragment as SignupConditionsFragment).updateCondition(model)
        } else {
            (parentFragment as SignupConditionsFragment).addCondition(model)
        }
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

    private fun updateSearchConditions(data: ArrayList<Conditions>?) {
        activity?.let { it1 ->
            if (!data.isNullOrEmpty()) {
                binding?.acConditions?.addHintWithArrayList(it1, data)
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        dismissDropDown()
        val item = parent?.getItemAtPosition(position) as Conditions
        model.condition_id = item.id
        model.name = item.name.toString()
        getConditionStage(item)
    }

    private fun getConditionStage(item: Conditions) {
        viewModel.getConditionStages(item.id).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        if (it.data?.data.isNullOrEmpty()) {
                            binding?.tvStage?.visibility = View.GONE
                            binding?.spinnerStage?.visibility = View.GONE
                        } else {
                            binding?.tvStage?.visibility = View.VISIBLE
                            binding?.spinnerStage?.visibility = View.VISIBLE
                            updateStageSpinner(it.data?.data)
                        }
                    }
                }
            }
        }
    }

    private fun updateStageSpinner(data: ArrayList<ConditionsStage>?) {
        val defaultSelectModel = ConditionsStage(0, getString(R.string.hint_please_select))
        data?.add(defaultSelectModel)
        activity?.let { binding?.spinnerStage?.addHintWithArrayList(it, data ?: arrayListOf()) }
    }

    private fun dismissDropDown() {
        if (isAdded) {
            binding?.acConditions?.post { binding?.acConditions?.dismissDropDown() }
        }
    }
}