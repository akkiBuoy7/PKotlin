package plm.patientslikeme2.ui.main.viewmodel.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import plm.patientslikeme2.data.model.signup.AddConditionsRequest
import plm.patientslikeme2.data.model.signup.SignupRequest
import plm.patientslikeme2.data.model.signup.UpdateConditionsRequest
import plm.patientslikeme2.data.model.signup.UpdateUserInfo
import plm.patientslikeme2.data.repository.signup.SignupRepository
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val repository: SignupRepository) :
    ViewModel() {

    val signupRequest = SignupRequest()
    val updateUserInfoRequest = UpdateUserInfo()

    fun getCheckEmail(email: String) = repository.getCheckEmail(email)

    fun postUserRegistrations() = repository.postUserRegistrations(signupRequest)

    fun getConditionList() = repository.getConditionList()

    fun getSearchCondition(search: String) = repository.getSearchCondition(search)

    fun getConditionStages(id: Int?) = repository.getConditionStages(id)

    fun postAddCondition(request: AddConditionsRequest) = repository.postAddCondition(request)

    fun putUpdateCondition(request: UpdateConditionsRequest) = repository.putUpdateCondition(request)

    fun deleteCondition(id: Int?) = repository.deleteCondition(id)

    fun getSearchZipCode(query: String) = repository.getSearchZipCode(query)

    fun postUserAvatars(avatar: MultipartBody.Part) = repository.postUserAvatars(avatar)

    fun postUpdateUserInfo() = repository.postUpdateUserInfo(updateUserInfoRequest)

    fun removeUserAvatars() = repository.removeUserAvatars()

}