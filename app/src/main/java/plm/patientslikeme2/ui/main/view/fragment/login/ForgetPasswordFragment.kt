package plm.patientslikeme2.ui.main.view.fragment.login

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.login.ForgetPassword
import plm.patientslikeme2.data.model.login.ResetPasswordError
import plm.patientslikeme2.databinding.FragmentForgetPasswordBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.login.LoginViewModel
import plm.patientslikeme2.utils.Validation

@AndroidEntryPoint
class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentForgetPasswordBinding =
        FragmentForgetPasswordBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack(R.id.forgetPasswordFragment, true)
        }

        binding?.btnSubmit?.setOnClickListener {
            if (isConnected()) {
                validatedEntered()
            } else {
                showError(getString(R.string.error_internet))
            }
        }
    }

    private fun validatedEntered() {
        val email = binding?.etEmail?.text.toString()
        if (email.isEmpty()) {
            binding?.etEmail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.error_please_enter_your_email))
            return
        }

        if (!Validation.isValidEmail(email)) {
            binding?.etEmail?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.invalid_email_address))
            return
        }
        hideError()
        hideSoftKeyboardInput()
        val request = ForgetPassword(email)
        callForgetPasswordAPI(request)
    }

    private fun callForgetPasswordAPI(request: ForgetPassword) {
        viewModel.callForgetPasswordRequest(request).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == false) {
                        showError(it.data?.message)
                    } else {
                        updateForgetPasswordResponse(it?.data?.message)
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

    private fun updateForgetPasswordResponse(message: String?) {
        binding?.etEmail?.setText("")
        binding?.tvEmailError?.text = message.toString()
        binding?.ivError?.visibility = View.GONE
        binding?.llError?.visibility = View.VISIBLE
    }

    private fun showError(message: String?) {
        binding?.tvEmailError?.text = Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY)
        binding?.tvEmailError?.movementMethod = LinkMovementMethod.getInstance()

        binding?.ivError?.visibility = View.VISIBLE
        binding?.llError?.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding?.etEmail?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        binding?.llError?.visibility = View.GONE
    }
}