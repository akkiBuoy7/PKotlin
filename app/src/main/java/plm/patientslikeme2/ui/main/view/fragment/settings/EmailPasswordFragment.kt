package plm.patientslikeme2.ui.main.view.fragment.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.signup.User
import plm.patientslikeme2.databinding.FragmentEmailPasswordBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.view.dialog.settings.ChangeEmailDialog
import plm.patientslikeme2.ui.main.view.dialog.settings.ChangePasswordDialog
import plm.patientslikeme2.ui.main.viewmodel.profile.ProfileViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences

@AndroidEntryPoint
class EmailPasswordFragment : BaseFragment<FragmentEmailPasswordBinding>() {

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentEmailPasswordBinding =
        FragmentEmailPasswordBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.emailPasswordFragment
        binding?.handlers = this

        updateUserEmail()
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
        when (view.id) {
            R.id.ll_email -> {
                ChangeEmailDialog.newInstance(binding?.tvEmail?.text.toString())
                    .show(childFragmentManager, getString(R.string.email_address))
            }
            R.id.ll_password -> {
                ChangePasswordDialog.newInstance()
                    .show(childFragmentManager, getString(R.string.change_password))
            }
        }
    }

    fun showSuccessErrorView(message: String?, status: Boolean?) {
        if (status != null) {
            if (message != null) {
                showSuccessErrorLayout(status, message)
                updateUserEmail()
            }
        }
    }

    fun updateUserEmail() {
        if (!TextUtils.isEmpty(Preferences.getValue(Constants.USER, ""))) {
            val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
            if (!TextUtils.isEmpty(user.email)) {
                binding?.tvEmail?.text = user.email
            } else {
                bindUserDetails()
            }
        } else {
            bindUserDetails()
        }
    }

    private fun bindUserDetails() {
        viewModel.getUserDetails().observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    Preferences.setValue(Constants.USER, Gson().toJson(it.data?.user))
                    binding?.tvEmail?.text = it.data?.user?.email
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.email_address_password), showBackArrow = true, hideRightIcon = true
        )
    }
}