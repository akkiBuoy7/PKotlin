package plm.patientslikeme2.ui.main.view.fragment.signup

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.signup.AddConditionsRequest
import plm.patientslikeme2.data.model.signup.Conditions
import plm.patientslikeme2.data.model.signup.UpdateConditionsRequest
import plm.patientslikeme2.databinding.FragmentSignupConditionsBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.signup.SignupMyConditionsAdapter
import plm.patientslikeme2.ui.main.view.dialog.signup.AddSignupConditionDialog
import plm.patientslikeme2.ui.main.viewmodel.signup.SignupViewModel
import plm.patientslikeme2.utils.Constants.ADD
import plm.patientslikeme2.utils.Constants.DELETE
import plm.patientslikeme2.utils.Constants.EDIT
import plm.patientslikeme2.utils.extensions.ActionDialogListener
import plm.patientslikeme2.utils.extensions.showActionDialog

@AndroidEntryPoint
class SignupConditionsFragment : BaseFragment<FragmentSignupConditionsBinding>() {

    private val viewModel: SignupViewModel by activityViewModels()

    private lateinit var mAdapter: SignupMyConditionsAdapter
    private var position: Int = 0
    private lateinit var selectedModel: Conditions

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSignupConditionsBinding =
        FragmentSignupConditionsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        mAdapter = SignupMyConditionsAdapter()
        binding?.rvConditions?.adapter = mAdapter

        mAdapter.onItemClicked = { model, position, type ->
            this.position = position
            this.selectedModel = model
            when (type) {
                EDIT -> openAddConditionDialog(model, EDIT)
                DELETE -> showDeleteConfirmDialog()
            }
        }

        binding?.btnNext?.setOnClickListener {
            if (isConnected()) {
                findNavController().navigate(R.id.action_signupConditionsFragment_to_signupYourselfFragment)
            }
        }

        binding?.btnAddCondition?.setOnClickListener {
            openAddConditionDialog(Conditions(), ADD)
        }

        if (isConnected()) {
            fetchConditionList()
        } else {
            showSuccessErrorLayout(false, getString(R.string.error_internet))
        }
    }

    fun updateCondition(model: Conditions) {
        val request = UpdateConditionsRequest()
        request.condition_id = model.condition_id
        request.privacy = model.privacy
        viewModel.putUpdateCondition(request).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        mAdapter.updateItem(model, position)
                        showSuccessErrorLayout(true, it.data?.message.toString())
                        updateNextButton()
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage = ErrorResponse().stringToObject(it.errorMessage)
                    showSuccessErrorLayout(false, errorMessage.message.toString())
                }
            }
        }
    }

    fun addCondition(model: Conditions) {
        val request = AddConditionsRequest()
        request.condition_id = model.condition_id
        request.stage_id = model.stage_id
        request.privacy = model.privacy
        viewModel.postAddCondition(request).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        mAdapter.addItemInList(it.data?.data ?: Conditions())
                        showSuccessErrorLayout(true, it.data?.message.toString())
                        updateNextButton()
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage = ErrorResponse().stringToObject(it.errorMessage)
                    showSuccessErrorLayout(false, errorMessage.message.toString())
                }
            }
        }
    }

    private fun showDeleteConfirmDialog() {
        activity?.let {
            showActionDialog(
                it,
                getString(R.string.delete),
                getString(R.string.are_you_sure_delete) + " " + selectedModel.name.toString() + "?",
                getString(R.string.yes),
                getString(R.string.no),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        callDeleteConditionAPI()
                    }
                })
        }
    }

    private fun fetchConditionList() {
        viewModel.getConditionList().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        mAdapter.submitList(it.data?.data ?: arrayListOf())
                        updateNextButton()
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun callDeleteConditionAPI() {
        viewModel.deleteCondition(selectedModel.id).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        showSuccessErrorLayout(true, it.data?.message.toString())
                        mAdapter.removeItem(selectedModel)
                        updateNextButton()
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    if (it.errorMessage?.contains("Connection timed out") == true) {
                        showSuccessErrorLayout(false, it.errorMessage.toString())
                    } else {
                        val errorMessage = ErrorResponse().stringToObject(it.errorMessage)
                        showSuccessErrorLayout(false, errorMessage.message.toString())
                    }
                }
            }
        }
    }

    private fun openAddConditionDialog(model: Conditions, type: String) {
        AddSignupConditionDialog.newInstance(model, type)
            .show(childFragmentManager, getString(R.string.edit_a_conditions))
    }

    private fun updateNextButton() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (mAdapter.itemCount == 0) {
                binding?.tvMyConditions?.visibility = View.GONE
                binding?.btnNext?.isEnabled = false
                binding?.btnNext?.setBackgroundResource(R.drawable.bg_button_dark_green_scale)
                binding?.btnNext?.setTextColor(Color.GRAY)
            } else {
                binding?.tvMyConditions?.visibility = View.VISIBLE
                binding?.btnNext?.isEnabled = true
                binding?.btnNext?.setBackgroundResource(R.drawable.bg_button_dark_green)
                binding?.btnNext?.setTextColor(Color.WHITE)
            }
        }, 300)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isAdded) {
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_signupConditionsFragment_to_loginFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        }
    }
}