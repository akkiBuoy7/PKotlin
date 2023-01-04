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
import plm.patientslikeme2.data.model.login.LoginModel
import plm.patientslikeme2.data.model.login.LoginRequest
import plm.patientslikeme2.data.model.login.ResetPasswordError
import plm.patientslikeme2.databinding.FragmentLoginBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.login.LoginViewModel
import plm.patientslikeme2.utils.Constants.ACCESS_TOKEN
import plm.patientslikeme2.utils.Constants.EXPIRES_IN
import plm.patientslikeme2.utils.Constants.IS_LOGGED_IN
import plm.patientslikeme2.utils.Constants.IS_REMEMBER_ME
import plm.patientslikeme2.utils.Constants.LOGIN_PASSWORD
import plm.patientslikeme2.utils.Constants.LOGIN_USER_NAME
import plm.patientslikeme2.utils.Constants.METRIC_GUID
import plm.patientslikeme2.utils.Constants.REFRESH_TOKEN
import plm.patientslikeme2.utils.Constants.SIGNUP_STEP
import plm.patientslikeme2.utils.Constants.TOKEN_TYPE
import plm.patientslikeme2.utils.Constants.USER_ID
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.enum.SignupStep

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    private var isPasswordVisible = false
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val rememberMe = Preferences.getValue(IS_REMEMBER_ME, false)
        binding?.cbRememberMe?.isChecked = rememberMe
        if (rememberMe) {
            binding?.etEmail?.setText(Preferences.getValue(LOGIN_USER_NAME, ""))
            binding?.etPassword?.setText(Preferences.getValue(LOGIN_PASSWORD, ""))
        }
        binding?.tvForgetPassword?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
        binding?.tvCreateAccount?.setOnClickListener {
            when (Preferences.getValue(SIGNUP_STEP, "")) {
                SignupStep.add_condition.toString() -> {
                    findNavController().navigate(R.id.action_loginFragment_to_signupConditionsFragment)
                }
                else -> {
                    findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
                }
            }
        }
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
                binding?.etPassword?.setSelection(
                    it1
                )
            }
        }
        binding?.btnSignIn?.setOnClickListener {
            if (isConnected()) {
                validatedEnteredData()
            } else {
                showError(getString(R.string.error_internet))
            }
        }
        if (featureFlag.android_signupandonboarding.not()) {
            binding?.tvCreateAccount?.visibility = View.GONE
        }
    }

    private fun validatedEnteredData() {
        val userName = binding?.etEmail?.text.toString()
        val password = binding?.etPassword?.text.toString()
        if (userName.isEmpty() && password.isEmpty()) {
            binding?.etEmail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            binding?.etPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            binding?.llErrorEmail?.visibility = View.VISIBLE
            binding?.llErrorPassword?.visibility = View.VISIBLE
            return
        }
        if (userName.isEmpty()) {
            binding?.etEmail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            return
        }
        binding?.llErrorEmail?.visibility = View.GONE
        binding?.etEmail?.setBackgroundResource(R.drawable.bg_edittext_round_gray)

        if (password.isEmpty()) {
            binding?.etPassword?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            binding?.llErrorPassword?.visibility = View.VISIBLE
            return
        }
        binding?.llErrorPassword?.visibility = View.GONE
        binding?.etPassword?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        binding?.llError?.visibility = View.GONE

        val request = LoginRequest()
        request.email = userName
        request.username = userName
        request.password = password
        hideSoftKeyboardInput()
        callLoginAPI(request)
    }

    private fun callLoginAPI(request: LoginRequest) {
        viewModel.callLoginRequest(request).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == false) {
                        showError(it.data?.message)
                    } else {
                        updateLoginResponse(it.data)
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

    private fun updateLoginResponse(data: LoginModel?) {
        activity?.let {
            Preferences.setValue(IS_LOGGED_IN, true)
            Preferences.setValue(ACCESS_TOKEN, data?.access_token.toString())
            Preferences.setValue(REFRESH_TOKEN, data?.refresh_token.toString())
            Preferences.setValue(TOKEN_TYPE, data?.token_type.toString())
            Preferences.setValue(EXPIRES_IN, data?.expires_in.toString())
            Preferences.setValue(SIGNUP_STEP, data?.signup_step.toString())
            Preferences.setValue(IS_REMEMBER_ME, binding?.cbRememberMe?.isChecked)
            Preferences.setValue(USER_ID, data?.user_id.toString())
            Preferences.setValue(METRIC_GUID, data?.user_id.toString())
            if (binding?.cbRememberMe?.isChecked == true) {
                Preferences.setValue(LOGIN_USER_NAME, binding?.etEmail?.text.toString())
                Preferences.setValue(LOGIN_PASSWORD, binding?.etPassword?.text.toString())
            } else {
                Preferences.setValue(LOGIN_USER_NAME, "")
                Preferences.setValue(LOGIN_PASSWORD, "")
            }
            when (data?.signup_step) {
                SignupStep.add_condition.toString() -> {
                    findNavController().navigate(R.id.action_loginFragment_to_signupConditionsFragment)
                }
                SignupStep.member_home.toString() -> {
                    openMainActivity()
                }
            }
        }
    }

    private fun showError(string: String?) {
        binding?.tvCommonError?.text = string
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