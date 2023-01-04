package plm.patientslikeme2.ui.main.view.fragment.login

import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.login.ResetPasswordRequest
import plm.patientslikeme2.databinding.FragmentResetPasswordBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.login.LoginViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.Validation
import plm.patientslikeme2.utils.enum.SignupStep

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {

    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var resetPasswordToken: String
    private var isPasswordVisible = false

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentResetPasswordBinding =
        FragmentResetPasswordBinding.inflate(inflater, container, false)

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        resetPasswordToken = args?.getString(Constants.RESET_PASSWORD_TOKEN).toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding?.ivVisiblePassword?.setOnClickListener {
            if (isPasswordVisible) {
                binding?.etPassword?.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding?.ivVisiblePassword?.setImageResource(R.drawable.ic_visibility)
                isPasswordVisible = false
            } else {
                binding?.etPassword?.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding?.ivVisiblePassword?.setImageResource(R.drawable.ic_visibility_off)
                isPasswordVisible = true
            }
            binding?.etPassword?.text?.length?.let { it1 ->
                binding?.etPassword?.setSelection(it1)
            }
        }

        binding?.btnContinue?.setOnClickListener {
            if (Preferences.getValue(Constants.IS_LOGGED_IN, false)) {
                when (Preferences.getValue(Constants.SIGNUP_STEP, "")) {
                    SignupStep.add_condition.toString() -> {
                        findNavController().navigate(R.id.action_loginFragment_to_signupConditionsFragment)
                    }
                    SignupStep.member_home.toString() -> {
                        openMainActivity()
                    }
                }
            } else {
                findNavController().popBackStack()
            }
        }

        binding?.btnSubmit?.setOnClickListener {
            if (isConnected()) {
                validatedEntered()
            } else {
                setError(getString(R.string.error_internet))
            }
        }
    }

    private fun validatedEntered() {
        val password = binding?.etPassword?.text.toString()
        if (password.isEmpty()) {
            binding?.etPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.password_can_not_be_blank))
            return
        }
        if (!Validation.isValidPassword(password)) {
            binding?.etPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            setError(getString(R.string.invalid_password_data))
            return
        }
        if (resetPasswordToken.isEmpty()) {
            setError(getString(R.string.error_valid_reset_password_token))
        }
        hideError()
        hideSoftKeyboardInput()
        val request = ResetPasswordRequest(resetPasswordToken, password, password)
        callResetPasswordAPI(request)
    }

    private fun callResetPasswordAPI(request: ResetPasswordRequest) {
        viewModel.callResetPasswordRequest(request).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == false) {
                        setError(it.data?.message)
                    } else {
                        updateResetPasswordResponse(it?.data?.message)
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    setError(getString(R.string.error_valid_reset_password_token))
                }
            }
        }
    }

    private fun updateResetPasswordResponse(message: String?) {
        binding?.tvSuccess?.text = message
        binding?.llReset?.visibility = View.GONE
        binding?.llChanged?.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding?.etPassword?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        binding?.llError?.visibility = View.GONE
    }

    private fun setError(string: String?) {
        binding?.tvEmailError?.text = string
        binding?.llError?.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isAdded) {
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        }
    }
}