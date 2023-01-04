package plm.patientslikeme2.ui.main.view.fragment.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.settings.OwnControlled
import plm.patientslikeme2.data.model.settings.PrivacySettingsRequest
import plm.patientslikeme2.databinding.FragmentPrivacySettingsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.settings.PrivacySettingsAdapter
import plm.patientslikeme2.ui.main.viewmodel.settings.SettingsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.convertDate
import plm.patientslikeme2.utils.extensions.makeLinks
import plm.patientslikeme2.utils.extensions.openHyperLinks

@AndroidEntryPoint
class PrivacySettingsFragment : BaseFragment<FragmentPrivacySettingsBinding>(), OnClickListener {

    private val viewModel: SettingsViewModel by activityViewModels()

    private lateinit var mConditionsAdapter: PrivacySettingsAdapter
    private lateinit var mSymptomsAdapter: PrivacySettingsAdapter
    private lateinit var mTreatmentsAdapter: PrivacySettingsAdapter
    private lateinit var mVitalsLabsAdapter: PrivacySettingsAdapter
    private var checkedRadio: Int = 0
    private var previousCeckedRadio: Int = 0

    private var listOfAllControls: ArrayList<OwnControlled> = ArrayList()
    private var listOfSelectedControls: ArrayList<OwnControlled> = ArrayList()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPrivacySettingsBinding =
        FragmentPrivacySettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentFragment = R.id.privacySettingsFragment
        initView()
        if (isConnected()) bindObservers() else showEmptyView(getString(R.string.error_internet))

    }

    private fun initView() {
        binding?.btnUpdate?.setOnClickListener(this)
        binding?.btnCancel?.setOnClickListener(this)

        binding?.tvConditions?.setOnClickListener(this)
        binding?.tvSymptoms?.setOnClickListener(this)
        binding?.tvTreatments?.setOnClickListener(this)
        binding?.tvVitalsAndLabs?.setOnClickListener(this)

        binding?.rbControlData?.setOnCheckedChangeListener(checkedChangeListener)
        binding?.rbVisibleToCommunity?.setOnCheckedChangeListener(checkedChangeListener)
        binding?.rbVisibleToNonMembers?.setOnCheckedChangeListener(checkedChangeListener)

        binding?.tvSubHeader?.makeLinks(
            Pair("Privacy Policy", OnClickListener {
                openHyperLinks(activity, Constants.PRIVACY_URL)
            })
        )
        mConditionsAdapter = PrivacySettingsAdapter()
        binding?.rvConditions?.adapter = mConditionsAdapter

        mSymptomsAdapter = PrivacySettingsAdapter()
        binding?.rvSymptoms?.adapter = mSymptomsAdapter

        mTreatmentsAdapter = PrivacySettingsAdapter()
        binding?.rvTreatments?.adapter = mTreatmentsAdapter

        mVitalsLabsAdapter = PrivacySettingsAdapter()
        binding?.rvVitalsAndLabs?.adapter = mVitalsLabsAdapter

        mConditionsAdapter.onItemClicked = { model, permissionLevel ->
            setData(model, permissionLevel)
        }
        mSymptomsAdapter.onItemClicked = { model, permissionLevel ->
            setData(model, permissionLevel)
        }
        mTreatmentsAdapter.onItemClicked = { model, permissionLevel ->
            setData(model, permissionLevel)
        }
        mVitalsLabsAdapter.onItemClicked = { model, permissionLevel ->
            setData(model, permissionLevel)
        }
    }

    private fun setData(model: OwnControlled, permissionLevel: String) {
        val newModel = OwnControlled()
        newModel.controllableId = model.controllableId
        newModel.controllableType = model.controllableType
        newModel.permissionLevel = permissionLevel
        newModel.controllableName = model.controllableName
        listOfSelectedControls.add(newModel)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun bindObservers() {
        viewModel.getUserPreferences().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data != null) {
                        setLastSeen(it.data?.data?.updatedAt)
                        setDefaultRadioSelection(it.data?.data?.privacySettings?.visibleTo)

                        val privacySettings = arrayListOf<OwnControlled>()
                        val defaultPrivacySettings = arrayListOf<OwnControlled>()

                        privacySettings.addAll(it.data?.data?.privacySettings?.ownControlled!!)
                        defaultPrivacySettings.addAll(it.data?.data?.defaultPrivacySettings?.ownControlled!!)
                        for (m1 in privacySettings.toMutableList()) {
                            for (index in 0 until defaultPrivacySettings.size) {
                                if (m1.controllableId == defaultPrivacySettings[index].controllableId) {
                                    defaultPrivacySettings.removeAt(index)
                                    break
                                }
                            }
                        }
                        privacySettings.addAll(defaultPrivacySettings)
                        renderData(privacySettings)
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun renderData(ownControlled: ArrayList<OwnControlled>) {
        listOfAllControls.clear()
        listOfSelectedControls.clear()

        ownControlled.let { list ->
            listOfAllControls.addAll(list)
        }

        mConditionsAdapter.apply {
            submitList(null)
            submitList(getPreferencesBasedOnType(ownControlled, "conditions"))
        }
        mSymptomsAdapter.apply {
            submitList(null)
            submitList(getPreferencesBasedOnType(ownControlled, "symptoms"))
        }
        mTreatmentsAdapter.apply {
            submitList(null)
            submitList(getPreferencesBasedOnType(ownControlled, "treatments"))
        }
        mVitalsLabsAdapter.apply {
            submitList(null)
            submitList(getPreferencesBasedOnType(ownControlled, "labs"))
        }
    }

    private fun getPreferencesBasedOnType(
        ownControlledModels: ArrayList<OwnControlled>?,
        type: String
    ): ArrayList<OwnControlled> {
        return ownControlledModels?.filter { it.controllableType == type } as ArrayList<OwnControlled>
    }

    private fun updatePreferences() {
        val listOfSendControls: ArrayList<OwnControlled> = ArrayList()
        if (listOfSelectedControls.size > 0) {
            listOfAllControls.forEach { model ->
                listOfSelectedControls.forEach { selModel ->
                    if (model.controllableId == selModel.controllableId) {
                        model.permissionLevel = selModel.permissionLevel
                        if (!listOfSendControls.contains(model))
                            listOfSendControls.add(model)
                    } else {
                        if (!listOfSendControls.contains(model))
                            listOfSendControls.add(model)
                    }
                }
            }
        } else {
            for (model in listOfAllControls) {
                model.permissionLevel = "plm"
                listOfSendControls.add(model)
            }
        }

        val request = PrivacySettingsRequest()
        request.privacySettings?.requestFromAccountPrivacy = true
        request.privacySettings?.visibleTo = getVisibleTo()
        request.privacySettings?.ownControlled = listOfSendControls

        viewModel.updateUserPreferences(request).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data != null) {
                        var successMessage = it.data?.message.toString()
                        if (successMessage.isEmpty()) {
                            successMessage = getString(R.string.success_privacy_settings)
                        }
                        showSuccessErrorLayout(true, successMessage)
                        listOfSelectedControls.clear()
                        bindObservers()
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    var errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    if (errorMessage.isEmpty()) {
                        errorMessage = getString(R.string.failed_privacy_settings)
                    }
                    showSuccessErrorLayout(true, errorMessage)

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setLastSeen(updatedAt: String?) {
        binding?.tvLastSeen?.text = "Last updated: " + convertDate(
            "yyyy-MM-dd", "MM/dd/yyyy", updatedAt
        )
    }

    private fun setDefaultRadioSelection(visibleTo: String?) {
        when {
            visibleTo.equals(
                context?.getString(R.string.plm_community_members), true
            ) -> {
                checkedRadio = R.id.rbVisibleToCommunity
                binding?.rbVisibleToCommunity?.isChecked = true
            }
            visibleTo.equals(context?.getString(R.string.non_members), true) -> {
                checkedRadio = R.id.rbVisibleToNonMembers
                binding?.rbVisibleToNonMembers?.isChecked = true
            }
            visibleTo.equals(
                context?.getString(R.string.own_controlled),
                true
            ) -> {
                checkedRadio = R.id.rbControlData
                binding?.rbControlData?.isChecked = true
            }
            else -> {
                checkedRadio = R.id.rbVisibleToCommunity
                binding?.rbVisibleToCommunity?.isChecked = true
            }
        }
        setOption(checkedRadio)
    }

    private fun setOption(checkedRadio: Int) {
        previousCeckedRadio = checkedRadio
        when (checkedRadio) {
            R.id.rbControlData -> {
                binding?.rbControlData?.isChecked = true
                binding?.lLControlData?.visibility = View.VISIBLE
            }
            R.id.rbVisibleToCommunity -> {
                binding?.rbVisibleToCommunity?.isChecked = true
                binding?.lLControlData?.visibility = View.GONE
            }
            R.id.rbVisibleToNonMembers -> {
                binding?.rbVisibleToNonMembers?.isChecked = true
                binding?.lLControlData?.visibility = View.GONE
            }
        }
    }

    private val checkedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (R.id.rbControlData == buttonView.id) {
                    binding?.lLControlData?.visibility = View.VISIBLE
                } else {
                    binding?.lLControlData?.visibility = View.GONE
                }
                previousCeckedRadio = checkedRadio
                checkedRadio = buttonView.id
            }
            listOfSelectedControls.clear()
        }

    private fun getVisibleTo(): String? {
        when (checkedRadio) {
            R.id.rbVisibleToNonMembers -> return context?.getString(R.string.non_members)
            R.id.rbVisibleToCommunity -> return context?.getString(R.string.plm_community_members)
            R.id.rbControlData -> return context?.getString(R.string.own_controlled)
        }
        return context?.getString(R.string.plm_community_members)
    }

    fun onItemClicked() {
        if (!isAdded) {
            return
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.privacy_settings), showBackArrow = true, hideRightIcon = true
        )
    }

    private fun showEmptyView(error: String) {
        if (binding?.vsNoResult?.viewStub?.parent != null) {
            binding?.vsNoResult?.setOnInflateListener { _, _ ->
                val binding = binding?.vsNoResult?.binding as LayoutNoResultBinding
                binding.tvMessage.text = error
                binding.clEmpty.visibility = View.VISIBLE
            }
            binding?.vsNoResult?.viewStub?.inflate()
            binding?.parent?.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_update -> {
                if (isConnected()) updatePreferences() else showEmptyView(getString(R.string.error_internet))
            }
            R.id.btn_cancel -> {
                setOption(previousCeckedRadio)
                listOfSelectedControls.clear()
                mConditionsAdapter.notifyDataSetChanged()
                mTreatmentsAdapter.notifyDataSetChanged()
                mVitalsLabsAdapter.notifyDataSetChanged()
                mSymptomsAdapter.notifyDataSetChanged()
            }
            R.id.tv_conditions -> {
                if (binding?.rvConditions?.isShown == true) {
                    binding?.rvConditions?.visibility = View.GONE
                    binding?.tvConditions?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_down,
                        0
                    )
                } else {
                    binding?.rvConditions?.visibility = View.VISIBLE
                    binding?.tvConditions?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_up_small,
                        0
                    )
                }
            }
            R.id.tv_symptoms -> {
                if (binding?.rvSymptoms?.isShown == true) {
                    binding?.rvSymptoms?.visibility = View.GONE
                    binding?.tvSymptoms?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_down,
                        0
                    )
                } else {
                    binding?.rvSymptoms?.visibility = View.VISIBLE
                    binding?.tvSymptoms?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_up_small,
                        0
                    )
                }
            }
            R.id.tv_treatments -> {
                if (binding?.rvTreatments?.isShown == true) {
                    binding?.rvTreatments?.visibility = View.GONE
                    binding?.tvTreatments?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_down,
                        0
                    )
                } else {
                    binding?.rvTreatments?.visibility = View.VISIBLE
                    binding?.tvTreatments?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_up_small,
                        0
                    )
                }
            }
            R.id.tv_vitals_and_labs -> {
                if (binding?.rvVitalsAndLabs?.isShown == true) {
                    binding?.rvVitalsAndLabs?.visibility = View.GONE
                    binding?.tvVitalsAndLabs?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_down,
                        0
                    )
                } else {
                    binding?.rvVitalsAndLabs?.visibility = View.VISIBLE
                    binding?.tvVitalsAndLabs?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_up_small,
                        0
                    )
                }
            }
        }
    }
}