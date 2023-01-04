package plm.patientslikeme2.ui.main.view.dialog.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.settings.PasswordUpdateRequest
import plm.patientslikeme2.databinding.DialogChangePasswordBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.view.fragment.settings.EmailPasswordFragment
import plm.patientslikeme2.ui.main.viewmodel.settings.SettingsViewModel
import plm.patientslikeme2.utils.Validation
import plm.patientslikeme2.utils.usercontrol.PasswordStrength

class ChangePasswordDialog : BaseBottomSheetFragment<DialogChangePasswordBinding>(),
    OnClickListener {

    private val viewModel: SettingsViewModel by activityViewModels()
    private var isCurrentPasswordVisible = false
    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false

    companion object {
        fun newInstance(): ChangePasswordDialog {
            return ChangePasswordDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): DialogChangePasswordBinding = DialogChangePasswordBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivClose?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)

        binding?.etNewPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                calculatePasswordStrength(s.toString())
                if (s.isNotEmpty()) {
                    binding?.llPasswordStrength?.visibility = View.VISIBLE
                } else {
                    binding?.llPasswordStrength?.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding?.ivVisiblePassword?.setOnClickListener {
            if (isCurrentPasswordVisible) {
                binding?.etCurrentPassword?.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding?.ivVisiblePassword?.setImageResource(R.drawable.ic_visibility)
                isCurrentPasswordVisible = false
            } else {
                binding?.etCurrentPassword?.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding?.ivVisiblePassword?.setImageResource(R.drawable.ic_visibility_off)
                isCurrentPasswordVisible = true
            }
            binding?.etCurrentPassword?.text?.length?.let { it1 ->
                binding?.etCurrentPassword?.setSelection(
                    it1
                )
            }
        }

        binding?.ivVisibleNewPassword?.setOnClickListener {
            if (isNewPasswordVisible) {
                binding?.etNewPassword?.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding?.ivVisibleNewPassword?.setImageResource(R.drawable.ic_visibility)
                isNewPasswordVisible = false
            } else {
                binding?.etNewPassword?.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding?.ivVisibleNewPassword?.setImageResource(R.drawable.ic_visibility_off)
                isNewPasswordVisible = true
            }
            binding?.etNewPassword?.text?.length?.let { it1 ->
                binding?.etNewPassword?.setSelection(
                    it1
                )
            }
        }

        binding?.ivVisibleConfirmPassword?.setOnClickListener {
            if (isConfirmPasswordVisible) {
                binding?.etConfirmPassword?.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding?.ivVisibleConfirmPassword?.setImageResource(R.drawable.ic_visibility)
                isConfirmPasswordVisible = false
            } else {
                binding?.etConfirmPassword?.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding?.ivVisibleConfirmPassword?.setImageResource(R.drawable.ic_visibility_off)
                isConfirmPasswordVisible = true
            }
            binding?.etConfirmPassword?.text?.length?.let { it1 ->
                binding?.etConfirmPassword?.setSelection(
                    it1
                )
            }
        }
    }

    private fun calculatePasswordStrength(newPassword: String) {
        val passwordStrength = PasswordStrength.calculate(newPassword)
        binding?.tvPasswordStrength?.setText(passwordStrength.msg)
        binding?.tvPasswordStrength?.setTextColor(passwordStrength.color)
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
        val newPassword = binding?.etNewPassword?.text.toString()
        val confirmPassword = binding?.etConfirmPassword?.text.toString()

        if (currentPassword.isEmpty()) {
            binding?.etCurrentPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.invalid_password))
            return
        }
        binding?.etCurrentPassword?.setBackgroundResource(R.drawable.bg_edittext_round_gray)

        if (newPassword.isEmpty()) {
            binding?.etNewPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.invalid_password))
            return
        }
        if (newPassword.length < 6) {
            binding?.etNewPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.invalid_password_length))
            return
        }
        if (!Validation.isValidPassword(newPassword)) {
            binding?.etNewPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            binding?.tvPasswordError?.visibility = View.VISIBLE
            return
        }
        binding?.etNewPassword?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        if (confirmPassword.isEmpty()) {
            binding?.etConfirmPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.invalid_password))
            return
        }
        if (currentPassword == newPassword) {
            binding?.etCurrentPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            binding?.etNewPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.same_password_error_msg))
            return
        }
        if (confirmPassword.length < 6) {
            binding?.etConfirmPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.invalid_password_length))
            return
        }
        if (newPassword != confirmPassword) {
            binding?.etNewPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            binding?.etConfirmPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.new_confirm_password_not_same))
            return
        }
        binding?.etNewPassword?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        binding?.etConfirmPassword?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        binding?.llError?.visibility = View.GONE

        bindObservers(getJsonRequest())
    }

    private fun getJsonRequest(): PasswordUpdateRequest {
        val request = PasswordUpdateRequest()
        request.current_password = binding?.etCurrentPassword?.text.toString()
        request.password = binding?.etNewPassword?.text.toString()
        request.confirm_password = binding?.etConfirmPassword?.text.toString()
        return request
    }

    private fun bindObservers(request: PasswordUpdateRequest) {
        viewModel.updatePassword(request).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success != null) {
                        dismiss()
                        (parentFragment as EmailPasswordFragment).showSuccessErrorView(
                            it.data?.message, it.data?.success
                        )
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    setError(getString(R.string.invalid_password))
                }
            }
        }
    }

    private fun setError(message: String) {
        binding?.tvPasswordError?.text = message
        binding?.llError?.visibility = View.VISIBLE
    }
}