package plm.patientslikeme2.ui.main.view.dialog.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.global.Data
import plm.patientslikeme2.data.model.global.Details
import plm.patientslikeme2.data.model.global.Jobs
import plm.patientslikeme2.data.model.global.Ranks
import plm.patientslikeme2.data.model.profile.UpdatePersonalInfoRequest
import plm.patientslikeme2.data.model.signup.User
import plm.patientslikeme2.data.model.signup.ZipCode
import plm.patientslikeme2.databinding.DialogUpdatePersonalInformationBinding
import plm.patientslikeme2.databinding.RowProfileInterestBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.fragment.profile.PersonalInfoFragment
import plm.patientslikeme2.ui.main.viewmodel.profile.ProfileViewModel
import plm.patientslikeme2.ui.main.viewmodel.signup.SignupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.*
import plm.patientslikeme2.utils.file.URIPathHelper
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.DateFormatSymbols
import java.util.*

class UpdatePersonalInformationDialog :
    BaseBottomSheetFragment<DialogUpdatePersonalInformationBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val viewModel: ProfileViewModel by activityViewModels()
    private val signUpViewModel: SignupViewModel by activityViewModels()
    private lateinit var secondaryInterestsAdapter: GenericListAdapter<Details, RowProfileInterestBinding>
    private var selectedImageUri: Uri? = null
    private var userInfo: User? = null
    private var selSecondaryInterests: ArrayList<Int> = ArrayList()

    companion object {
        fun newInstance(): UpdatePersonalInformationDialog {
            return UpdatePersonalInformationDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        dialog?.setOnShowListener { dialog ->
            val layout: FrameLayout? =
                (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)
            layout?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): DialogUpdatePersonalInformationBinding =
        DialogUpdatePersonalInformationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.handlers = this
        initViews()
        if (isConnected()) bindGetPersonalInfoObserver() else showSuccessErrorLayout(
            binding?.llErrorSuccess, false, getString(R.string.error_internet)
        )
    }

    private fun initViews() {
        binding?.etProfileHeadline?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int,
            ) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val textLength = s.length
                binding?.tvProfileHeadlineCount?.text = "$textLength/160 characters"
            }
        })
        binding?.etZipCode?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 4) getAddressFromZipCode(s.toString()) else binding?.tvCity?.text =
                    ""
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun bindStaticMasterDataObserver(user: User) {
        viewModel.getStaticMasterData().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data != null) {
                        Preferences.setValue(Constants.MASTER_DATA, Gson().toJson(it?.data?.data))
                        renderData(it?.data?.data, user)
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showSuccessErrorLayout(binding?.llErrorSuccess, false, errorMessage)
                }
            }
        }
    }

    private fun renderData(data: Data?, user: User) {

        if (user.birth_date != null) {
            binding?.tvYear?.text = convertDate("yyyy-MM-dd", "yyyy", user.birth_date)
            binding?.tvMonth?.text = convertDate("yyyy-MM-dd", "MMM", user.birth_date)
            binding?.tvDay?.text = convertDate("yyyy-MM-dd", "dd", user.birth_date)
        }

        prepareSexData(data?.sexes, user)
        prepareGendersData(data?.genders, user)
        prepareEthnicityData(data?.ethinicity, user)
        prepareEducationLevelsData(data?.education_levels, user)
        prepareInsurancesData(data?.health_insurance_types, user)
        prepareMilitaryStatusesData(data?.military?.statuses, user)

        binding?.spinnerMilitaryStatus?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long,
                ) {
                    val status = binding?.spinnerMilitaryStatus?.selectedItem as Details
                    if (status.id.equals("0")) {
                        setMilitaryViewsByStatus("none")
                    } else {
                        setMilitaryViewsByStatus(status.id)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        prepareStartServiceYearsMonths(user)
        prepareEndServiceYearsMonths(user)

        prepareMilitaryBranchesData(data?.military?.branches, user)

        binding?.spinnerMilitaryBranch?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long,
                ) {
                    val branch = binding?.spinnerMilitaryBranch?.selectedItem as Details
                    if (branch.id.equals("0")) {
                        setSpinnerDisableEnable(binding?.spinnerMilitaryRank, false)
                        setSpinnerDisableEnable(binding?.spinnerMilitaryJob, false)
                        setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, false)
                        setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
                    } else {
                        prepareRanksData(data?.military?.ranks, branch.id, user)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding?.spinnerMilitaryRank?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long,
                ) {
                    val rank = binding?.spinnerMilitaryRank?.selectedItem as String
                    if (rank == "Please Select..." || rank == "I prefer not to answer") {
                        setSpinnerDisableEnable(binding?.spinnerMilitaryJob, false)
                        setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, false)
                        setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
                    } else {
                        val branch = binding?.spinnerMilitaryBranch?.selectedItem as Details
                        prepareJobsData(data?.military?.jobs, branch.id, user)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding?.spinnerMilitaryJob?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long,
                ) {
                    val job = binding?.spinnerMilitaryJob?.selectedItem as String
                    if (job == "Please Select..." || job == "I prefer not to answer" || job == "My job is not listed") {
                        setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, false)
                        setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
                    } else {
                        prepareCombatZonesData(data?.military?.combat_zones, user)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding?.spinnerMilitaryCombatZone?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long,
                ) {
                    val zone = binding?.spinnerMilitaryCombatZone?.selectedItem as Details
                    if (zone.id.equals("0")) {
                        setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
                    } else {
                        prepareVAHealthCareSystemData(user)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        val countriesList = arrayListOf<String>()
        data?.country.let { countries -> countries?.let { countriesList.addAll(it) } }
        binding?.etCountry?.addHintWithArrayList(requireContext(), countriesList)

        val interestsList = arrayListOf<Details>()
        interestsList.add(Details("0", "Please Select..."))
        data?.interests?.let { interests -> interestsList.addAll(interests) }
        binding?.spinnerPrimaryInterest?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            interestsList
        )
        for ((index, interest) in interestsList.withIndex()) {
            if (interest.name == user.primary_interest?.name) {
                binding?.spinnerPrimaryInterest?.setSelection(index)
                break
            }
        }

        selSecondaryInterests.clear()
        secondaryInterestsAdapter = GenericListAdapter(R.layout.row_profile_interest,
            object : GenericRecyclerBindingInterface<RowProfileInterestBinding, Details> {
                override fun bindData(
                    binder: RowProfileInterestBinding,
                    model: Details,
                    position: Int,
                ) {
                    binder.cbInterest.text = model.name
                    user.secondary_interests?.forEach { primaryInterest ->
                        if (model.name.equals(primaryInterest.name)) {
                            binder.cbInterest.isChecked = true
                            model.id?.toInt()?.let { selSecondaryInterests.add(it) }
                        }
                    }
                    binder.cbInterest.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            model.id?.toInt()?.let { selSecondaryInterests.add(it) }
                        } else {
                            model.id?.toInt()?.let { selSecondaryInterests.remove(it) }
                        }
                    }
                }
            })
        binding?.rvSecondaryInterests?.adapter = secondaryInterestsAdapter
        secondaryInterestsAdapter.submitList(data?.interests)
    }

    private fun prepareStartServiceYearsMonths(user: User) {
        val years = getLast100Years()
        binding?.spinnerStartYear?.addFirstPositionAsHintWithArrayList(requireContext(), years)
        for ((index, year) in years.withIndex()) {
            if (user.military_service_start_date_hash?.year.toString() == year) {
                binding?.spinnerStartYear?.setSelection(index)
                break
            }
        }
        val months = getMonthsData()
        binding?.spinnerStartMonth?.addFirstPositionAsHintWithArrayList(requireContext(), months)
        for ((index, month) in months.withIndex()) {
            if (user.military_service_start_date_hash?.month.toString() == month.id) {
                binding?.spinnerStartMonth?.setSelection(index)
                break
            }
        }
    }

    private fun prepareEndServiceYearsMonths(user: User) {
        val years = getLast100Years()
        binding?.spinnerEndYear?.addFirstPositionAsHintWithArrayList(requireContext(), years)
        for ((index, year) in years.withIndex()) {
            if (user.military_service_end_date_hash?.year.toString() == year) {
                binding?.spinnerEndYear?.setSelection(index)
                break
            }
        }
        val months = getMonthsData()
        binding?.spinnerEndMonth?.addFirstPositionAsHintWithArrayList(requireContext(), months)
        for ((index, month) in months.withIndex()) {
            if (user.military_service_end_date_hash?.month.toString() == month.id) {
                binding?.spinnerEndMonth?.setSelection(index)
                break
            }
        }
    }

    private fun prepareMilitaryBranchesData(branches: ArrayList<Details>?, user: User) {
        val militaryBranches = arrayListOf<Details>()
        militaryBranches.add(Details("0", "Please Select..."))
        branches?.let { it -> militaryBranches.addAll(it) }
        binding?.spinnerMilitaryBranch?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            militaryBranches
        )
        for ((index, branch) in militaryBranches.withIndex()) {
            if (user.military_branch_id != null && branch.id == user.military_branch_id) {
                binding?.spinnerMilitaryBranch?.setSelection(index)
                break
            }
        }
    }

    private fun prepareInsurancesData(healthInsuranceTypes: ArrayList<Details>?, user: User) {
        val healthInsuranceTypesList = arrayListOf<Details>()
        healthInsuranceTypesList.add(Details("0", "Please Select..."))
        healthInsuranceTypes?.let { healthInsuranceTypesList.addAll(it) }
        binding?.spinnerInsurance?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            healthInsuranceTypesList
        )
        for ((index, insurance) in healthInsuranceTypesList.withIndex()) {
            if (insurance.name == user.health_insurance_type) {
                binding?.spinnerInsurance?.setSelection(index)
                break
            }
        }
    }

    private fun prepareEducationLevelsData(educationLevels: ArrayList<Details>?, user: User) {
        val educationLevelsList = arrayListOf<Details>()
        educationLevelsList.add(Details("0", "Please Select..."))
        educationLevels?.let { educationLevelsList.addAll(it) }
        binding?.spinnerHighestLevelOfEducation?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            educationLevelsList
        )
        for ((index, education) in educationLevelsList.withIndex()) {
            if (education.name == user.education_level) {
                binding?.spinnerHighestLevelOfEducation?.setSelection(index)
                break
            }
        }
    }

    private fun prepareEthnicityData(ethnicities: ArrayList<Details>?, user: User) {
        val ethnicityList = arrayListOf<Details>()
        ethnicityList.add(Details("0", "Please Select..."))
        ethnicities?.let { ethnicityList.addAll(it) }
        binding?.spinnerEthnicity?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            ethnicityList
        )
        for ((index, ethnicity) in ethnicityList.withIndex()) {
            if (ethnicity.name == user.ethnicity) {
                binding?.spinnerEthnicity?.setSelection(index)
                break
            }
        }
    }

    private fun prepareGendersData(genders: ArrayList<Details>?, user: User) {
        val gendersList = arrayListOf<Details>()
        gendersList.add(Details("0", "Please Select..."))
        genders?.let { gendersList.addAll(it) }
        binding?.spinnerGenderIdentity?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            gendersList
        )
        for ((index, gender) in gendersList.withIndex()) {
            if (user.canned_genders?.size!! > 0 && gender.name == user.canned_genders?.get(0)) {
                binding?.spinnerGenderIdentity?.setSelection(index)
                break
            }
        }
    }

    private fun prepareSexData(sexes: ArrayList<Details>?, user: User) {
        val sexesList = arrayListOf<Details>()
        sexesList.add(Details("0", "Please Select..."))
        sexes?.let { sexesList.addAll(it) }
        binding?.spinnerSex?.addFirstPositionAsHintWithArrayList(requireContext(), sexesList)
        for ((index, item) in sexesList.withIndex()) {
            if (item.id == user.sex) {
                binding?.spinnerSex?.setSelection(index)
                break
            }
        }
    }

    private fun getMonthsData(): ArrayList<Details> {
        val monthsList = ArrayList<Details>()
        monthsList.add(Details("0", ""))
        val months: Array<String> = DateFormatSymbols().months
        for ((index, month) in months.withIndex()) {
            monthsList.add(Details((index + 1).toString(), month))
        }
        return monthsList
    }

    private fun getLast100Years(): ArrayList<String> {
        val yearsList = ArrayList<String>()
        yearsList.add("")
        val years = ArrayList<String>()
        val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        years.run {
            for (year in currentYear - 100..currentYear) {
                add(year.toString())
            }
            sortDescending()
        }
        yearsList.addAll(years)
        return yearsList
    }

    private fun prepareVAHealthCareSystemData(user: User) {
        setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, true)
        val eligibilitiesList = arrayListOf<Details>()
        eligibilitiesList.add(Details("0", "Please Select..."))
        eligibilitiesList.add(Details("ineligible", "Not eligible"))
        eligibilitiesList.add(Details("eligible_but_unenrolled", "Eligible but not enrolled"))
        eligibilitiesList.add(Details("enrolled", "Enrolled"))
        binding?.spinnerMilitaryEligibility?.addFirstPositionAsHintWithArrayList(
            requireContext(), eligibilitiesList
        )
        for ((index, va) in eligibilitiesList.withIndex()) {
            if (user.military_va_healthcare_eligibility == va.id) {
                binding?.spinnerMilitaryEligibility?.setSelection(index)
                break
            }
        }
    }

    private fun prepareCombatZonesData(combatZones: ArrayList<Details>?, user: User) {
        setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, true)
        setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
        val combatZonesList = arrayListOf<Details>()
        combatZonesList.add(Details("0", "Please Select..."))
        combatZones?.let { zones -> combatZonesList.addAll(zones) }
        binding?.spinnerMilitaryCombatZone?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            combatZonesList
        )
        for ((index, zone) in combatZonesList.withIndex()) {
            if (user.military_deployed_to_combat_zone == zone.id) {
                binding?.spinnerMilitaryCombatZone?.setSelection(index)
                break
            }
        }
    }

    private fun prepareJobsData(jobs: ArrayList<Jobs>?, branch_id: String?, user: User) {
        setSpinnerDisableEnable(binding?.spinnerMilitaryJob, true)
        setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, false)
        setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
        val militaryJobs = arrayListOf<String>()
        militaryJobs.add("Please Select...")
        militaryJobs.add("I prefer not to answer")
        militaryJobs.add("My job is not listed")
        if (jobs != null) {
            for (job in jobs) {
                if (job.id == branch_id) {
                    job.data?.enlisted?.let { enlisted -> militaryJobs.addAll(enlisted) }
                    job.data?.officer?.let { officer -> militaryJobs.addAll(officer) }
                    job.data?.warrant?.let { warrant -> militaryJobs.addAll(warrant) }
                    break
                }
            }
        }
        binding?.spinnerMilitaryJob?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            militaryJobs
        )
        for ((index, job) in militaryJobs.withIndex()) {
            if (job == user.military_job) {
                binding?.spinnerMilitaryJob?.setSelection(index)
                break
            }
        }
    }

    private fun prepareRanksData(ranks: ArrayList<Ranks>?, branch_id: String?, user: User) {
        setSpinnerDisableEnable(binding?.spinnerMilitaryRank, true)
        setSpinnerDisableEnable(binding?.spinnerMilitaryJob, false)
        setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, false)
        setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
        val militaryRanks = arrayListOf<String>()
        militaryRanks.add("Please Select...")
        militaryRanks.add("I prefer not to answer")
        if (ranks != null) {
            for (rank in ranks) {
                if (rank.id == branch_id) {
                    rank.data?.enlisted?.let { enlisted -> militaryRanks.addAll(enlisted) }
                    rank.data?.officer?.let { officer -> militaryRanks.addAll(officer) }
                    break
                }
            }
        }
        binding?.spinnerMilitaryRank?.addFirstPositionAsHintWithArrayList(
            requireContext(),
            militaryRanks
        )
        for ((index, rank) in militaryRanks.withIndex()) {
            if (rank == user.military_rank) {
                binding?.spinnerMilitaryRank?.setSelection(index)
                break
            }
        }
    }

    private fun setMilitaryViewsByStatus(status: String?) {
        when (status) {
            "none", "no_answer" -> {
                binding?.llStartOfService?.visibility = View.GONE
                binding?.llEndOfService?.visibility = View.GONE
                binding?.spinnerMilitaryCombatZone?.visibility = View.GONE
                binding?.tvMilitaryCombatZone?.visibility = View.GONE
                binding?.spinnerMilitaryEligibility?.visibility = View.GONE
                binding?.tvVaHealthCareSystem?.visibility = View.GONE
                setSpinnerDisableEnable(binding?.spinnerMilitaryBranch, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryRank, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryJob, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
            }
            "currently_serving" -> {
                binding?.llStartOfService?.visibility = View.VISIBLE
                binding?.llEndOfService?.visibility = View.GONE
                binding?.spinnerMilitaryCombatZone?.visibility = View.VISIBLE
                binding?.tvMilitaryCombatZone?.visibility = View.VISIBLE
                binding?.spinnerMilitaryEligibility?.visibility = View.VISIBLE
                binding?.tvVaHealthCareSystem?.visibility = View.VISIBLE
                setSpinnerDisableEnable(binding?.spinnerMilitaryBranch, true)
                setSpinnerDisableEnable(binding?.spinnerMilitaryRank, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryJob, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
            }
            "previously_served" -> {
                binding?.llStartOfService?.visibility = View.VISIBLE
                binding?.llEndOfService?.visibility = View.VISIBLE
                binding?.spinnerMilitaryCombatZone?.visibility = View.VISIBLE
                binding?.tvMilitaryCombatZone?.visibility = View.VISIBLE
                binding?.spinnerMilitaryEligibility?.visibility = View.VISIBLE
                binding?.tvVaHealthCareSystem?.visibility = View.VISIBLE
                setSpinnerDisableEnable(binding?.spinnerMilitaryBranch, true)
                setSpinnerDisableEnable(binding?.spinnerMilitaryRank, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryJob, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryCombatZone, false)
                setSpinnerDisableEnable(binding?.spinnerMilitaryEligibility, false)
            }
        }
    }

    private fun prepareMilitaryStatusesData(statuses: ArrayList<Details>?, user: User) {
        val militaryStatuses = arrayListOf<Details>()
        militaryStatuses.add(Details("0", "Please Select..."))
        statuses?.let { militaryStatuses.addAll(it) }
        binding?.spinnerMilitaryStatus?.addFirstPositionAsHintWithArrayList(requireContext(),
            militaryStatuses)
        for ((index, status) in militaryStatuses.withIndex()) {
            if (status.id == user.military_status) {
                binding?.spinnerMilitaryStatus?.setSelection(index)
                break
            }
        }
    }

    private fun bindUpdatePersonalInfoObserver() {
        updateRequest().let { request ->
            viewModel.updatePersonalInfo(request).observe(viewLifecycleOwner) {
                when {
                    it.status.isLoading() -> {
                        showProgress()
                    }
                    it.status.isSuccessful() -> {
                        hideProgress()
                        if (it.data != null) {
                            userInfo = it?.data?.user
                            dismiss()
                            (parentFragment as PersonalInfoFragment).showSuccessErrorView(
                                getString(R.string.success_personal_info_update), true
                            )
                        }
                    }
                    it.status.isError() -> {
                        hideProgress()
                        val errorMessage =
                            if (ErrorResponse().stringToObject(it.errorMessage).message is ArrayList<*>) {
                                (ErrorResponse().stringToObject(it.errorMessage).message as ArrayList<String>)[0]
                            } else {
                                ErrorResponse().stringToObject(it.errorMessage).message.toString()
                            }
                        showSuccessErrorLayout(binding?.llErrorSuccess, false, errorMessage)
                        binding?.parent?.fullScroll(ScrollView.FOCUS_UP)
                    }
                }
            }
        }
    }

    private fun updateRequest(): UpdatePersonalInfoRequest {
        val month = binding?.tvMonth?.text.toString()
        val day = binding?.tvDay?.text.toString()
        val year = binding?.tvYear?.text.toString()

        var city = ""
        var state = ""
        val cityState = binding?.tvCity?.text.toString()
        if (cityState.contains(",")) {
            val list = cityState.split(",")
            if (list.size > 1) {
                city = list[0]
                state = list[1]
            }
        }

        val selSex = binding?.spinnerSex?.selectedItem as? Details
        val selGenderIdentity = binding?.spinnerGenderIdentity?.selectedItem as? Details
        val selEthnicity = binding?.spinnerEthnicity?.selectedItem as? Details
        val selInsurance = binding?.spinnerInsurance?.selectedItem as? Details
        val selPrimaryInterest = binding?.spinnerPrimaryInterest?.selectedItem as? Details
        val selHighestLevelOfEducation =
            binding?.spinnerHighestLevelOfEducation?.selectedItem as? Details

        val selMilitaryStatus = binding?.spinnerMilitaryStatus?.selectedItem as? Details
        val selMilitaryBranch = binding?.spinnerMilitaryBranch?.selectedItem as? Details
        val selMilitaryRank = binding?.spinnerMilitaryRank?.selectedItem.toString()
        val selMilitaryJob = binding?.spinnerMilitaryJob?.selectedItem.toString()
        val selMilitaryZone = binding?.spinnerMilitaryCombatZone?.selectedItem as? Details
        val selMilitaryEligibility = binding?.spinnerMilitaryEligibility?.selectedItem as? Details
        val selServiceStartYear = binding?.spinnerStartYear?.selectedItem.toString()
        val selServiceStartMonth = binding?.spinnerStartMonth?.selectedItem as? Details
        val selServiceEndYear = binding?.spinnerEndYear?.selectedItem.toString()
        val selServiceEndMonth = binding?.spinnerEndMonth?.selectedItem as? Details

        val request = UpdatePersonalInfoRequest()
        request.user?.birth_date =
            if (month.isNotEmpty() && day.isNotEmpty() && year.isNotEmpty()) convertDate(
                "dd-MMM-yyyy", "dd/MM/yyyy", "$day-$month-$year"
            ) else ""
        request.user?.city = city
        request.user?.state = state
        request.user?.country = if (!binding?.etCountry?.text.toString().isNullOrEmpty()
        ) binding?.etCountry?.text.toString() else ""
        request.user?.postcode = if (!binding?.etZipCode?.text.toString().isNullOrEmpty()
        ) binding?.etZipCode?.text.toString() else ""
        request.user?.sex = if (selSex?.id != "0") selSex?.id else ""

        val cannedGenders: ArrayList<String> = arrayListOf()
        if (selGenderIdentity?.id != "0")
            selGenderIdentity?.id?.let { cannedGenders.add(it) }
        request.user?.canned_genders = cannedGenders.toTypedArray()

        request.user?.ethnicity_id =
            if (selEthnicity?.id != "0") selEthnicity?.id else ""
        request.user?.education_level_id =
            if (selHighestLevelOfEducation?.id != "0") selHighestLevelOfEducation?.id else ""
        request.user?.primary_interest_id =
            if (selPrimaryInterest?.id != "0") selPrimaryInterest?.id else ""
        request.user?.health_insurance_type_id =
            if (selInsurance?.id != "0") selInsurance?.id else ""
        request.user?.bio_attrs?.brief_bio = binding?.etProfileHeadline?.text.toString()
        request.user?.bio_attrs?.content = binding?.etMyStory?.text.toString()
        request.user?.secondary_interest_ids = selSecondaryInterests.toTypedArray()
        request.user?.user_profile_attrs?.profile_color = userInfo?.profile_colour

        if (selMilitaryStatus?.id.equals("0")) {
            request.user?.military_service_attributes?.status = ""
        } else if (selMilitaryStatus?.id.equals("none") || selMilitaryStatus?.id.equals("no_answer")) {
            request.user?.military_service_attributes?.status = selMilitaryStatus?.id
        } else {
            request.user?.military_service_attributes?.status =
                if (selMilitaryStatus != null && selMilitaryStatus.id != "0") selMilitaryStatus.id else ""
            request.user?.military_service_attributes?.branch_id =
                if (selMilitaryBranch != null && selMilitaryBranch.id != "0") selMilitaryBranch.id else ""
            request.user?.military_service_attributes?.job =
                if (selMilitaryJob == "Please Select..." || selMilitaryJob == "I prefer not to answer" || selMilitaryJob == "My job is not listed") null else selMilitaryJob
            request.user?.military_service_attributes?.rank =
                if (selMilitaryRank == "Please Select..." || selMilitaryRank == "I prefer not to answer") null else selMilitaryRank
            request.user?.military_service_attributes?.service_start_date_hash?.year =
                if (selServiceStartYear != "") selServiceStartYear.toInt() else null
            request.user?.military_service_attributes?.service_start_date_hash?.month =
                if (selServiceStartMonth != null && selServiceStartMonth.id != "0") selServiceStartMonth.id?.toInt() else 0
            if (selMilitaryStatus?.id.equals("previously_served")) {
                request.user?.military_service_attributes?.service_end_date_hash?.year =
                    if (selServiceEndYear != "") selServiceEndYear.toInt() else null
                request.user?.military_service_attributes?.service_end_date_hash?.month =
                    if (selServiceEndMonth != null && selServiceEndMonth.id != "0") selServiceEndMonth.id?.toInt() else 0
            }
            request.user?.military_service_attributes?.deployed_to_combat_zone =
                if (selMilitaryZone != null && selMilitaryZone.id != "0") selMilitaryZone.id else ""
            request.user?.military_service_attributes?.va_healthcare_eligibility =
                if (selMilitaryEligibility != null && selMilitaryEligibility.id != "0") selMilitaryEligibility.id else ""
        }
        return request
    }

    private fun bindGetPersonalInfoObserver() {
        viewModel.getUserDetails().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.user != null) {
                        userInfo = it?.data?.user
                        it.data?.user.let { user ->
                            user?.let { userInfo ->
                                Preferences.setValue(Constants.USER, Gson().toJson(userInfo))
                                Preferences.setValue(
                                    Constants.PROFILE_COLOR,
                                    userInfo.profile_colour
                                )
                                bindUserData(userInfo)
                            }
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showSuccessErrorLayout(binding?.llErrorSuccess, false, errorMessage)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindUserData(user: User) {

        binding?.ivUser?.loadImage(user.full_name, user.avatar_url)

        binding?.etMyStory?.setHtmlText(if (user.bio?.content != null) user.bio?.content else "")
        binding?.etProfileHeadline?.setHtmlText(if (user.bio?.brief_bio != null) user.bio?.brief_bio else "")

        binding?.etZipCode?.setText(if (!user.postcode.isNullOrEmpty()) user.postcode.toString() else "")
        binding?.etCountry?.setText(if (!user.country.isNullOrEmpty()) user.country.toString() else "")

        val cityState = when {
            !user.city.isNullOrEmpty() && !user.state.isNullOrEmpty() -> "${user.city}, ${user.state}"
            !user.city.isNullOrEmpty() && user.state.isNullOrEmpty() -> "${user.city}"
            user.city.isNullOrEmpty() && !user.state.isNullOrEmpty() -> "${user.state}"
            else -> ""
        }
        binding?.tvCity?.text = cityState

        if (Preferences.getValue(Constants.MASTER_DATA, null) != null) {
            val masterData = Data().stringToObject(Preferences.getValue(Constants.MASTER_DATA, ""))
            renderData(masterData, user)
        } else {
            bindStaticMasterDataObserver(user)
        }
    }

    private fun showDatePicker() {
        hideSoftKeyboardInput()
        showDOBDatePicker(context, binding?.tvDay, binding?.tvMonth, binding?.tvYear)
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
        when (view.id) {
            R.id.tv_day -> showDatePicker()
            R.id.tv_month -> showDatePicker()
            R.id.tv_year -> showDatePicker()
            R.id.iv_close -> dismiss()
            R.id.btn_change_photo -> AvatarImageOptionsDialog.newInstance()
                .show(childFragmentManager, getString(R.string.change_photo))
            R.id.btn_save -> bindUpdatePersonalInfoObserver()
        }
    }

    @SuppressLint("IntentReset")
    private fun selectImage() {
        val pickImageIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        pickImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        resultLauncher.launch(pickImageIntent)
    }

    fun showPickerDialog() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(requireContext(), *perms)) {
            selectImage()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.msg_runtime_permission), 1, *perms
            )
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    binding?.ivUser?.setImageURI(it)
                    uploadUserAvatar(it)
                }
            }
        }

    private fun uploadUserAvatar(selectedImageUri: Uri) {
        val filePath = URIPathHelper().getPath(requireContext(), selectedImageUri)
        if (filePath.isNullOrEmpty()) {
            return
        }
        val file = File(filePath)
        if (!file.exists()) {
            return
        }
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multiPartBody =
            MultipartBody.Part.createFormData("avatar[image]", file.name, requestFile)
        signUpViewModel.postUserAvatars(multiPartBody).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
                        val avatar = it?.data?.data?.image
                        binding?.ivUser?.loadImage(user.full_name, avatar)
                        avatar?.let { image ->
                            (parentFragment as PersonalInfoFragment).loadImageData(image)
                        }
                        showSuccessErrorLayout(binding?.llErrorSuccess,
                            it.data?.success!!, it.data?.message.toString())
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showSuccessErrorLayout(binding?.llErrorSuccess, false, errorMessage)
                }
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        selectImage()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showSuccessErrorLayout(
            binding?.llErrorSuccess, false, getString(R.string.msg_runtime_permission)
        )
    }

    private fun getAddressFromZipCode(query: String) {
        signUpViewModel.getSearchZipCode(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        if (!it.data?.data.isNullOrEmpty()) {
                            updateCityState(it.data?.data?.get(0))
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateCityState(data: ZipCode?) {
        binding?.etZipCode?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        if (data != null && isAdded) {
            binding?.tvCity?.text = "${data.city}, ${data.state}"
        }
    }

    private fun setSpinnerDisableEnable(spinner: AppCompatSpinner?, status: Boolean) {
        spinner?.isEnabled = status
        val drawable = if (status) ContextCompat.getDrawable(
            requireContext(), R.drawable.bg_spinner_down_gray
        ) else ContextCompat.getDrawable(
            requireContext(), R.drawable.bg_spinner_disable
        )
        if (!status && (spinner?.adapter != null) && (spinner.adapter?.count!! > 0)
        ) {
            spinner.setSelection(0)
        }
        spinner?.background = drawable
    }

    fun removeUserAvatars() {
        signUpViewModel.removeUserAvatars().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        it?.data?.message?.let { message ->
                            val user =
                                User().stringToObject(Preferences.getValue(Constants.USER, ""))
                            binding?.ivUser?.loadImage(user.full_name, "")
                            (parentFragment as PersonalInfoFragment).loadImageData("")
                            showSuccessErrorLayout(binding?.llErrorSuccess, true, message)
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showSuccessErrorLayout(binding?.llErrorSuccess, false, errorMessage)
                }
            }
        }
    }
}
