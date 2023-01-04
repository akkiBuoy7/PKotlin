package plm.patientslikeme2.ui.main.view.fragment.conditions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.conditions.*
import plm.patientslikeme2.data.model.treatments.AddTreatmentRequest
import plm.patientslikeme2.databinding.FragmentConditionDetailsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowSymptomBinding
import plm.patientslikeme2.databinding.RowTreatmentBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.dialog.conditions.AddConditionDialog
import plm.patientslikeme2.ui.main.view.dialog.conditions.AddSymptomDialog
import plm.patientslikeme2.ui.main.view.dialog.treatments.AddTreatmentDialog
import plm.patientslikeme2.ui.main.viewmodel.conditions.ConditionsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.ActionDialogListener
import plm.patientslikeme2.utils.extensions.fromJson
import plm.patientslikeme2.utils.extensions.showActionDialog
import java.util.*

@AndroidEntryPoint
class ConditionDetailsFragment : BaseFragment<FragmentConditionDetailsBinding>() {

    private val viewModel: ConditionsViewModel by activityViewModels()

    private lateinit var symptomsAdapter: GenericListAdapter<SymptomsDetailsItem, RowSymptomBinding>
    private lateinit var treatmentsAdapter: GenericListAdapter<TreatmentsItem, RowTreatmentBinding>

    private var conditionId: String = ""
    private var conditionName: String = ""
    private lateinit var modelCondition: ConditionInfo

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentConditionDetailsBinding =
        FragmentConditionDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.navigation_condition_details
        binding?.handlers = this
        initView()
        if (isConnected()) bindObservers() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initView() {

        arguments?.getString(Constants.CONDITION_ID)?.let {
            viewModel.conditionId = it
            conditionId = it
        }

        arguments?.getString(Constants.CONDITION_NAME)?.let {
            conditionName = it
        }

        toolbarBinding?.btnAction?.setOnClickListener {
            AddConditionDialog.newInstance(modelCondition, Constants.EDIT)
                .show(childFragmentManager, getString(R.string.edit_a_conditions))
        }

        treatmentsAdapter = GenericListAdapter(R.layout.row_treatment, object :
            GenericRecyclerBindingInterface<RowTreatmentBinding, TreatmentsItem> {
            override fun bindData(
                binder: RowTreatmentBinding,
                model: TreatmentsItem,
                position: Int
            ) {
                binder.model = model
                if (position == treatmentsAdapter.itemCount - 1) {
                    binder.viewDivider.isVisible = false
                }
                binder.ivEdit.setOnClickListener {
                    AddTreatmentDialog.newInstance(model, Constants.EDIT, modelCondition.id)
                        .show(childFragmentManager, getString(R.string.edit_treatment))
                }
                binder.ivDelete.setOnClickListener {
                    openTreatmentDeleteConfirmationDialog(model)
                }
                binder.executePendingBindings()
            }

        })
        binding?.rvTreatments?.adapter = treatmentsAdapter

        symptomsAdapter = GenericListAdapter(R.layout.row_symptom, object :
            GenericRecyclerBindingInterface<RowSymptomBinding, SymptomsDetailsItem> {
            override fun bindData(
                binder: RowSymptomBinding,
                model: SymptomsDetailsItem,
                position: Int
            ) {
                binder.model = model
                if (position == symptomsAdapter.itemCount - 1) {
                    binder.viewDivider.isVisible = false
                }

                binder.ivDelete.setOnClickListener {
                    openSymptomDeleteConfirmationDialog(model)
                }
                binder.executePendingBindings()
            }

        })
        binding?.rvSymptoms?.adapter = symptomsAdapter
    }

    private fun openTreatmentDeleteConfirmationDialog(model: TreatmentsItem) {
        activity?.let {
            showActionDialog(
                it,
                getString(R.string.remove),
                "${getString(R.string.remove_prompt)}  ${model.name}",
                getString(R.string.remove),
                getString(R.string.cancel),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        deleteTreatment(model)
                    }
                })
        }
    }

    private fun openSymptomDeleteConfirmationDialog(model: SymptomsDetailsItem) {
        activity?.let {
            showActionDialog(
                it,
                getString(R.string.remove),
                "${getString(R.string.remove_prompt)}  ${model.name}",
                getString(R.string.remove),
                getString(R.string.cancel),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        deleteSymptom(model)
                    }
                })
        }
    }

    private fun openConditionDeleteConfirmationDialog(model: ConditionInfo) {
        activity?.let {
            showActionDialog(
                it,
                getString(R.string.remove),
                "${getString(R.string.remove_prompt)}  ${model.name}",
                getString(R.string.remove),
                getString(R.string.cancel),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        deleteCondition()
                    }
                })
        }
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

    private fun bindObservers() {
        viewModel.getConditionDetails().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data != null) {
                        modelCondition = it.data?.data!!.conditionInfo!!
                        binding?.model = modelCondition
                        if (it.data?.data!!.conditionInfo?.treatments?.size!! > 0) {
                            binding?.rvTreatments?.visibility = View.VISIBLE
                            binding?.noResultTreatments?.visibility = View.GONE
                            treatmentsAdapter.submitList(it.data?.data!!.conditionInfo?.treatments)
                        } else {
                            binding?.rvTreatments?.visibility = View.GONE
                            binding?.noResultTreatments?.visibility = View.VISIBLE
                        }
                        if (it.data?.data!!.conditionInfo?.symptoms?.size!! > 0) {
                            binding?.rvSymptoms?.visibility = View.VISIBLE
                            binding?.noResultSymptoms?.visibility = View.GONE
                            symptomsAdapter.submitList(it.data?.data!!.conditionInfo?.symptoms)
                        } else {
                            binding?.rvSymptoms?.visibility = View.GONE
                            binding?.noResultSymptoms?.visibility = View.VISIBLE
                        }
                    } else {
                        showEmptyView(getString(R.string.error_result))
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
        when (view.id) {
            R.id.btn_treatments_add -> {
                AddTreatmentDialog.newInstance(TreatmentsItem(), Constants.ADD, modelCondition.id)
                    .show(childFragmentManager, getString(R.string.add_a_treatment))
            }
            R.id.btn_add_symptoms -> {
                modelCondition.symptoms = symptomsAdapter.currentList
                AddSymptomDialog.newInstance(modelCondition, Constants.ADD)
                    .show(childFragmentManager, getString(R.string.add_symptoms))
            }
            R.id.btn_remove_condition -> {
                openConditionDeleteConfirmationDialog(modelCondition)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            conditionName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            showBackArrow = true,
            hideRightIcon = true,
            showCloseRightIcon = false,
            showRightAction = true,
            actionButtonText = getString(R.string.edit)
        )
    }

    fun editCondition(model: AddConditionRequest) {
        viewModel.putEditCondition(model).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        bindObservers()
                        showSuccessErrorLayout(
                            it.data?.success!!, it.data?.message.toString()
                        )
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error =
                        Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(error.success!!, error.message.toString())
                }
            }
        }
    }

    private fun deleteTreatment(model: TreatmentsItem) {
        viewModel.deleteTreatments(model.id.toString()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        removeItem(model)
                        showSuccessErrorLayout(it.data?.success!!, it.data?.message.toString())
                        if (treatmentsAdapter.itemCount==1){
                            binding?.rvTreatments?.visibility = View.GONE
                            binding?.noResultTreatments?.visibility = View.VISIBLE
                        }
                    }else {
                        showEmptyView(getString(R.string.error_result))
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error =
                        Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(error.success!!, error.message.toString())
                }
            }
        }
    }

    private fun removeItem(model: TreatmentsItem) {
        val position = treatmentsAdapter.currentList.indexOf(model)
        val currentList = treatmentsAdapter.currentList.toMutableList()
        currentList.removeAt(position)
        treatmentsAdapter.submitList(currentList)
    }

    private fun deleteSymptom(model: SymptomsDetailsItem) {
        viewModel.deleteSymptoms(model.symptomInfoId.toString()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        removeSymptomItem(model)
                        showSuccessErrorLayout(it.data?.success!!, it.data?.message.toString())
                        if (symptomsAdapter.itemCount==1){
                            binding?.rvSymptoms?.visibility = View.GONE
                            binding?.noResultSymptoms?.visibility = View.VISIBLE
                        }
                    } else {
                        showEmptyView(getString(R.string.error_result))
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error =
                        Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(error.success!!, error.message.toString())
                }
            }
        }
    }

    private fun removeSymptomItem(model: SymptomsDetailsItem) {
        val position = symptomsAdapter.currentList.indexOf(model)
        val currentList = symptomsAdapter.currentList.toMutableList()
        currentList.removeAt(position)
        symptomsAdapter.submitList(currentList)
        symptomsAdapter.notifyDataSetChanged()
    }

    private fun deleteCondition() {
        viewModel.deleteCondition().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        showSuccessErrorLayout(it.data?.success!!, it.data?.message.toString())
                        findNavController().popBackStack(currentFragment, true)
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error =
                        Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(error.success!!, error.message.toString())
                }
            }
        }
    }

    fun addTreatment(model: AddTreatmentRequest) {
        viewModel.postAddTreatment(model).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        addItemInList(
                            TreatmentsItem(
                                id = it.data!!.data?.treatmentHistory?.id,
                                name = it.data!!.data?.treatmentHistory?.name
                            )
                        )
                        binding?.rvTreatments?.visibility = View.VISIBLE
                        binding?.noResultTreatments?.visibility = View.GONE
                        showSuccessErrorLayout(it.data?.success!!, it.data?.message.toString())
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error =
                        Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(error.success!!, error.message.toString())
                }
            }
        }
    }

    private fun addItemInList(model: TreatmentsItem) {
        val list: MutableList<TreatmentsItem> = mutableListOf()
        list.addAll(treatmentsAdapter.currentList)
        list.add(model)
        treatmentsAdapter.submitList(list)
    }

    fun editTreatment(model: AddTreatmentRequest, id: Int) {
        viewModel.putEditTreatments(model, id).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        bindObservers()
                        showSuccessErrorLayout(it.data?.success!!, it.data?.message.toString())
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error =
                        Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(error.success!!, error.message.toString())
                }
            }
        }
    }


    fun addSymptom(model: AddSymptomRequest) {

        viewModel.postAddSymptom(model).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        bindObservers()
                        showSuccessErrorLayout(it.data?.success!!, it.data?.message.toString())
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error =
                        Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(error.success!!, error.message.toString())
                }
            }
        }
    }
}