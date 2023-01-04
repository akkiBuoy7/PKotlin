package plm.patientslikeme2.ui.main.view.fragment.treatments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.conditions.TreatmentsItem
import plm.patientslikeme2.data.model.treatments.AddTreatmentRequest
import plm.patientslikeme2.databinding.FragmentMyTreatmentsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.treatments.TreatmentsParentAdapter
import plm.patientslikeme2.ui.main.view.dialog.treatments.AddTreatmentDialog
import plm.patientslikeme2.ui.main.viewmodel.treatments.TreatmentsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.ActionDialogListener
import plm.patientslikeme2.utils.extensions.fromJson
import plm.patientslikeme2.utils.extensions.showActionDialog

@AndroidEntryPoint
class MyTreatmentsFragment : BaseFragment<FragmentMyTreatmentsBinding>(),
    TreatmentsParentAdapter.RemoveTreatmentListener {

    private val viewModel: TreatmentsViewModel by activityViewModels()

    private lateinit var myTreatmentAdapter: TreatmentsParentAdapter

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentMyTreatmentsBinding = FragmentMyTreatmentsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.navigation_my_treatments
        initView()
        if (isConnected()) bindObservers() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initView() {
        toolbarBinding?.btnAction?.setOnClickListener {
            AddTreatmentDialog.newInstance(TreatmentsItem(), Constants.ADD)
                .show(childFragmentManager, getString(R.string.add_a_treatment))
        }
    }

    private fun bindObservers() {
        viewModel.getMyTreatments().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.treatments?.size!! > 0) {
                        binding?.parent?.visibility = View.VISIBLE
                        binding?.rvTreatment?.visibility = View.VISIBLE

                        myTreatmentAdapter =
                            TreatmentsParentAdapter(it.data?.data!!.treatments, this)
                        binding?.rvTreatment?.adapter = myTreatmentAdapter
                    } else {
                        binding?.rvTreatment?.visibility = View.GONE
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

    private fun deleteTreatment(position: Int, parentPosition: Int) {
        viewModel.deleteTreatments().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data != null) {
//                        myTreatmentAdapter.delete(parentPosition, position)
                        bindObservers()
                        showSuccessErrorLayout(it.data?.success!!, it.data?.message.toString())
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

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.my_treatments),
            showBackArrow = false,
            hideRightIcon = true,
            showCloseRightIcon = true,
            showRightAction = true,
            actionButtonText = "Add"
        )
    }

    override fun onRemoveTreatment(id: String, position: Int, name: String, parentPosition: Int) {
        viewModel.treatmentId = id
        openTreatmentDeleteConfirmationDialog(name, position, parentPosition)
    }

    override fun OnEditTreatment(
        conditionId: Int?, treatmentName: String, treatmentId: Int, position: Int
    ) {
        AddTreatmentDialog.newInstance(
            TreatmentsItem(treatmentName, treatmentId, id = conditionId), Constants.EDIT
        ).show(childFragmentManager, getString(R.string.edit_treatment))
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

    private fun openTreatmentDeleteConfirmationDialog(
        name: String, position: Int, parentPosition: Int
    ) {
        activity?.let {
            showActionDialog(it,
                getString(R.string.remove),
                "${getString(R.string.remove_prompt)}  $name",
                getString(R.string.remove),
                getString(R.string.cancel),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        deleteTreatment(position, parentPosition)
                    }
                })
        }
    }
}