package plm.patientslikeme2.ui.main.viewmodel.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import plm.patientslikeme2.data.model.login.ForgetPassword
import plm.patientslikeme2.data.model.login.LoginRequest
import plm.patientslikeme2.data.model.login.ResetPasswordRequest
import plm.patientslikeme2.data.repository.login.LoginRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) :
    ViewModel() {

    fun callLoginRequest(request: LoginRequest) = repository.postLogin(request)

    fun callForgetPasswordRequest(request: ForgetPassword) = repository.postForgetPassword(request)

    fun callResetPasswordRequest(request: ResetPasswordRequest) = repository.putResetPassword(request)
}