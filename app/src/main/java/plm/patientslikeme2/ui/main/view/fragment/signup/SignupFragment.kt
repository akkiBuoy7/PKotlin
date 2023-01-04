package plm.patientslikeme2.ui.main.view.fragment.signup

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.SignupError
import plm.patientslikeme2.data.model.signup.SignupData
import plm.patientslikeme2.databinding.FragmentSignupBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.signup.SignupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.ACCESS_TOKEN
import plm.patientslikeme2.utils.Constants.EXPIRES_IN
import plm.patientslikeme2.utils.Constants.IS_LOGGED_IN
import plm.patientslikeme2.utils.Constants.PRIVACY_URL
import plm.patientslikeme2.utils.Constants.REFRESH_TOKEN
import plm.patientslikeme2.utils.Constants.SIGNUP_STEP
import plm.patientslikeme2.utils.Constants.TERMS_CONDITIONS
import plm.patientslikeme2.utils.Constants.TOKEN_TYPE
import plm.patientslikeme2.utils.Constants.USER
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.Validation.isValidEmail
import plm.patientslikeme2.utils.Validation.isValidPassword
import plm.patientslikeme2.utils.Validation.isValidUsername
import plm.patientslikeme2.utils.extensions.*
import plm.patientslikeme2.utils.usercontrol.UserEditText
import plm.patientslikeme2.utils.usercontrol.UserTextView

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>() {

    private var isPasswordVisible = false
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSignupBinding = FragmentSignupBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding?.etEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateCreateButtonState()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding?.etUsername?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateCreateButtonState()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding?.etPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateCreateButtonState()
                calculatePasswordStrength(
                    s.toString(),
                    binding?.llPasswordStrength,
                    binding?.tvPasswordStrength
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

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

        binding?.tvDay?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateCreateButtonState()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding?.tvDay?.setOnClickListener {
            showDatePicker()
        }

        binding?.tvMonth?.setOnClickListener {
            showDatePicker()
        }

        binding?.tvYear?.setOnClickListener {
            showDatePicker()
        }

        binding?.cbTermsConditions?.makeLinks(
            Pair("terms & conditions of use", View.OnClickListener {
                openHyperLinks(activity, TERMS_CONDITIONS)
            }),
            Pair("privacy policy", View.OnClickListener {
                openHyperLinks(activity, PRIVACY_URL)
            })
        )

        binding?.cbTermsConditions?.setOnCheckedChangeListener { _, _ ->
            updateCreateButtonState()
        }

        binding?.tvAlreadyMember?.setOnClickListener {
            closeFragment()
        }
        binding?.btnBack?.setOnClickListener {
            closeFragment()
        }

        binding?.btnCreateAccount?.setOnClickListener {
            if (isConnected()) {
                validatedEnteredData()
            } else {
                showSuccessErrorLayout(false, getString(R.string.error_internet))
            }
        }
    }

    private fun validatedEnteredData() {
        val email = binding?.etEmail?.text.toString()
        val userName = binding?.etUsername?.text.toString()
        val password = binding?.etPassword?.text.toString()
        val month = binding?.tvMonth?.text.toString()
        val day = binding?.tvDay?.text.toString()
        val year = binding?.tvYear?.text.toString()
        val dob = "$day-$month-$year"

        if (email.isEmpty() && userName.isEmpty() && password.isEmpty() &&
            dob.isEmpty() && dob == "--" && binding?.cbTermsConditions?.isChecked == false
        ) {
            showError(getString(R.string.error_all_inputs_required))
            return
        }

        if (email.isEmpty()) {
            setEditTextError(binding?.etEmail)
            showError(getString(R.string.invalid_email_address))
            return
        }
        if (!isValidEmail(email)) {
            setEditTextError(binding?.etEmail)
            showError(getString(R.string.invalid_email_address))
            return
        }
        setEditTextNormal(binding?.etEmail)

        if (userName.isEmpty()) {
            setEditTextError(binding?.etUsername)
            showError(getString(R.string.username_can_not_be_blank))
            return
        }
        if (!isValidUsername(userName)) {
            setEditTextError(binding?.etUsername)
            showError(getString(R.string.invalid_username))
            return
        }
        setEditTextNormal(binding?.etUsername)

        if (password.isEmpty()) {
            setEditTextError(binding?.etPassword)
            showError(getString(R.string.password_can_not_be_blank))
            return
        }
        if (password.length < 8) {
            setEditTextError(binding?.etPassword)
            showError(getString(R.string.error_password_length))
            return
        }
        if (!isValidPassword(password)) {
            setEditTextError(binding?.etPassword)
            showError(getString(R.string.invalid_password_data))
            return
        }
        setEditTextNormal(binding?.etPassword)

        if (dob == "--") {
            setTextViewError(binding?.tvDay)
            setTextViewError(binding?.tvMonth)
            setTextViewError(binding?.tvYear)
            showError(getString(R.string.invalid_password_data))
            return
        }
        if (calculateAge(dob) < 4745) {
            setTextViewError(binding?.tvDay)
            setTextViewError(binding?.tvMonth)
            setTextViewError(binding?.tvYear)
            showError(getString(R.string.error_dob_age))
            return
        }
        setTextViewNormal(binding?.tvDay)
        setTextViewNormal(binding?.tvMonth)
        setTextViewNormal(binding?.tvYear)

        viewModel.signupRequest.email = email
        viewModel.signupRequest.username = userName
        viewModel.signupRequest.password = password
        viewModel.signupRequest.dob = dob
        viewModel.signupRequest.caregiver = binding?.cbJoiningCaregiver?.isChecked
        viewModel.signupRequest.terms_of_service = binding?.cbTermsConditions?.isChecked

        Preferences.setValue(Constants.USER_NAME, userName)

        hideError()

        if (isConnected()) checkMailIsExit(email) else showError(getString(R.string.error_internet))
    }

    private fun setEditTextNormal(editText: UserEditText?) {
        editText?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
    }

    private fun setEditTextError(editText: UserEditText?) {
        editText?.setBackgroundResource(R.drawable.bg_edittext_round_red)
    }

    private fun setTextViewError(textView: UserTextView?) {
        textView?.setBackgroundResource(R.drawable.bg_spinner_down_red)
    }

    private fun setTextViewNormal(textView: UserTextView?) {
        textView?.setBackgroundResource(R.drawable.bg_spinner_down_gray)
    }

    private fun showDatePicker() {
        hideSoftKeyboardInput()
        showDOBDatePicker(activity, binding?.tvDay, binding?.tvMonth, binding?.tvYear)
    }

    private fun checkMailIsExit(email: String) {
        viewModel.getCheckEmail(email).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    if (it.data?.success == false) {
                        hideProgress()
                        setEditTextError(binding?.etEmail)
                        showError(getString(R.string.email_address_taken))
                    } else {
                        callUserRegistration()
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage = SignupError().stringToObject(it.errorMessage)
                    val message = errorMessage.data?.error?.joinToString().toString()
                    if (message.contains("email")) {
                        setEditTextError(binding?.etEmail)
                    }
                    showError(message)
                }
            }
        }
    }

    private fun callUserRegistration() {
        viewModel.postUserRegistrations().observe(viewLifecycleOwner) {
            hideProgress()
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == false) {
                        setEditTextError(binding?.etUsername)
                        showError(getString(R.string.username_taken))
                    } else {
                        updateSignupResponse(it.data?.data)
                    }
                }
                it.status.isError() -> {
                    val errorMessage = SignupError().stringToObject(it.errorMessage)
                    val message = errorMessage.data?.error?.joinToString().toString()
                    if (message.contains("email")) {
                        setEditTextError(binding?.etEmail)
                    }
                    if (message.contains("username")) {
                        setEditTextError(binding?.etUsername)
                    }
                    if (message.contains("password")) {
                        setEditTextError(binding?.etPassword)
                    }
                    showError(message)
                }
            }
        }
    }

    private fun updateSignupResponse(signupModel: SignupData?) {
        activity?.let {
            Preferences.setValue(IS_LOGGED_IN, true)
            Preferences.setValue(ACCESS_TOKEN, signupModel?.data?.access_token.toString())
            Preferences.setValue(REFRESH_TOKEN, signupModel?.data?.refresh_token.toString())
            Preferences.setValue(TOKEN_TYPE, signupModel?.data?.token_type.toString())
            Preferences.setValue(EXPIRES_IN, signupModel?.data?.expires_in.toString())
            Preferences.setValue(SIGNUP_STEP, signupModel?.user?.signup_step.toString())
            Preferences.setValue(USER, Gson().toJson(signupModel?.user))
            findNavController().navigate(R.id.action_signupFragment_to_signupConditionsFragment)
        }
    }

    private fun showError(message: String?) {
        binding?.llError?.visibility = View.VISIBLE
        binding?.tvError?.text = message
    }

    private fun hideError() {
        binding?.llError?.visibility = View.GONE
    }

    private fun updateCreateButtonState() {
        val email = binding?.etEmail?.text.toString()
        val userName = binding?.etUsername?.text.toString()
        val password = binding?.etPassword?.text.toString()
        val month = binding?.tvMonth?.text.toString()
        val day = binding?.tvDay?.text.toString()
        val year = binding?.tvYear?.text.toString()
        val dob = "$day-$month-$year"
        if (email.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty() && dob.isNotEmpty() && dob != "--" && binding?.cbTermsConditions?.isChecked == true) {
            binding?.btnCreateAccount?.isEnabled = true
            binding?.btnCreateAccount?.setBackgroundResource(R.drawable.bg_button_dark_green)
            binding?.btnCreateAccount?.setTextColor(Color.WHITE)
        } else {
            binding?.btnCreateAccount?.isEnabled = false
            activity?.let {
                binding?.btnCreateAccount?.setBackgroundResource(R.drawable.bg_button_dark_green_scale)
                binding?.btnCreateAccount?.setTextColor(
                    ContextCompat.getColor(it, R.color.gray_scale)
                )
            }
        }
    }

    private fun closeFragment() {
        findNavController().popBackStack(R.id.signupFragment, true)
    }
}