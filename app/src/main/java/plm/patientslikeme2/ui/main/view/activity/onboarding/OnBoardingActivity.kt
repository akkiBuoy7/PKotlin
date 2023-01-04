package plm.patientslikeme2.ui.main.view.activity.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.ActivityOnboardingBinding
import plm.patientslikeme2.ui.base.BaseActivity
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.IS_LOGGED_IN
import plm.patientslikeme2.utils.Constants.RESET_PASSWORD_TOKEN
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.enum.SignupStep

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding>() {

    companion object {

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }

    private lateinit var navController: NavController
    private var resetPasswordToken: String? = null

    override fun createBinding(): ActivityOnboardingBinding {
        return ActivityOnboardingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        successErrorBinding = binding.icSuccessError
        navController = findNavController(R.id.nav_host_fragment)
        if (intent.data != null) {
            resetPasswordToken = intent.data?.getQueryParameter(RESET_PASSWORD_TOKEN)
        }
        bindFeatureFlag {  }
        initView()
    }

    private fun initView() {
        if (!TextUtils.isEmpty(resetPasswordToken)) {
            val bundle = Bundle()
            bundle.putString(RESET_PASSWORD_TOKEN, resetPasswordToken)
            navController.navigate(R.id.action_loginFragment_to_resetPasswordFragment, bundle)
        } else if (Preferences.getValue(IS_LOGGED_IN, false)) {
            when (Preferences.getValue(Constants.SIGNUP_STEP, "")) {
                SignupStep.add_condition.toString() -> {
                    navController.navigate(R.id.action_loginFragment_to_signupConditionsFragment)
                }
                SignupStep.member_home.toString() -> {
                    openMainActivity()
                }
            }
        } else {
            navController.navigate(R.id.loginFragment)
        }
    }
}