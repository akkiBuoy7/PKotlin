package plm.patientslikeme2.ui.main.view.dialog.settings

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.login.ResetPasswordError
import plm.patientslikeme2.data.model.settings.EmailUpdateRequest
import plm.patientslikeme2.data.model.signup.User
import plm.patientslikeme2.databinding.DialogChangeEmailBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.view.fragment.settings.EmailPasswordFragment
import plm.patientslikeme2.ui.main.viewmodel.settings.SettingsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.VALUE
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.Validation.isValidEmail

class ChangeEmailDialog : BaseBottomSheetFragment<DialogChangeEmailBinding>(), OnClickListener {

    private val viewModel: SettingsViewModel by activityViewModels()
    private var isPasswordVisible = false

    companion object {
        fun newInstance(email: String): ChangeEmailDialog {
            return ChangeEmailDialog().apply {
                val bundle = Bundle().apply {
                    putString(VALUE, email)
                }
                arguments = bundle
            }
        }
    }

    private lateinit var selectedValue: String

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            selectedValue = arguments?.getString(VALUE).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): DialogChangeEmailBinding = DialogChangeEmailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding?.ivClose?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)
        binding?.tvCurrentEmailId?.text = selectedValue

        binding?.ivVisiblePassword?.setOnClickListener {
            if (isPasswordVisible) {
                binding?.etCurrentPassword?.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding?.ivVisiblePassword?.setImageResource(R.drawable.ic_visibility)
                isPasswordVisible = false
            } else {
                binding?.etCurrentPassword?.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding?.ivVisiblePassword?.setImageResource(R.drawable.ic_visibility_off)
                isPasswordVisible = true
            }
            binding?.etCurrentPassword?.text?.length?.let { it1 ->
                binding?.etCurrentPassword?.setSelection(
                    it1
                )
            }
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
                validatedDetails()
            }
        }
    }

    private fun validatedDetails() {
        val currentPassword = binding?.etCurrentPassword?.text.toString()
        val newEmail = binding?.etNewEmail?.text.toString()
        val confirmNewMail = binding?.etConfirmNewMail?.text.toString()
        val currentEmail = binding?.tvCurrentEmailId?.text.toString()

        if (currentPassword.isEmpty()) {
            binding?.etCurrentPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.invalid_password))
            return
        }
        binding?.etCurrentPassword?.setBackgroundResource(R.drawable.bg_edittext_round_gray)

        if (newEmail.isEmpty()) {
            binding?.etNewEmail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.invalid_email_address))
            return
        }
        if (!isValidEmail(newEmail)) {
            binding?.etNewEmail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.invalid_email_address))
            return
        }
        if (newEmail == currentEmail) {
            binding?.etNewEmail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.same_email_error_msg))
            return
        }
        binding?.etNewEmail?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        if (confirmNewMail.isEmpty()) {
            binding?.etConfirmNewMail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.invalid_email_address))
            return
        }
        if (!isValidEmail(confirmNewMail)) {
            binding?.etConfirmNewMail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.invalid_email_address))
            return
        }
        if (newEmail != confirmNewMail) {
            binding?.etNewEmail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            binding?.etConfirmNewMail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.new_confirm_email_not_same))
            return
        }
        binding?.etNewEmail?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        binding?.etConfirmNewMail?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        binding?.llError?.visibility = View.GONE

        if (binding?.btnSave?.text == "Save") {
            updateEmailObservers(getJsonRequest())
        } else {
            verifyEmailObservers()
        }
    }

    private fun getJsonRequest(): EmailUpdateRequest {
        val request = EmailUpdateRequest()
        request.password = binding?.etCurrentPassword?.text.toString()
        request.new_email = binding?.etNewEmail?.text.toString()
        request.confirm_email = binding?.etConfirmNewMail?.text.toString()
        return request
    }

    private fun showError(message: String?) {
        binding?.tvEmailError?.text = message
        binding?.llError?.visibility = View.VISIBLE
    }

    private fun updateEmailObservers(request: EmailUpdateRequest) {
        viewModel.updateEmail(request).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success != null) {
                        if (it.data?.success == true) {
                            binding?.btnSave?.text = getString(R.string.verify)
                        } else if (it.data?.success == false) {
                            dismiss()
                            (parentFragment as EmailPasswordFragment).showSuccessErrorView(
                                it.data?.data?.error?.get(0), it.data?.success
                            )
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage = ResetPasswordError().stringToObject(it.errorMessage)
                    showError(errorMessage.data?.error)
                }
            }
        }
    }

    private fun verifyEmailObservers() {
        viewModel.verifyEmail().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success != null) {
                        if (it.data?.success == true) {
                            val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
                            user.email = binding?.etNewEmail?.text.toString()
                            Preferences.setValue(Constants.USER, Gson().toJson(user))
                            (parentFragment as EmailPasswordFragment).updateUserEmail()
                            dismiss()
                            (parentFragment as EmailPasswordFragment).showSuccessErrorView(
                                getString(R.string.verify_email_hint), it.data?.success
                            )
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }
}