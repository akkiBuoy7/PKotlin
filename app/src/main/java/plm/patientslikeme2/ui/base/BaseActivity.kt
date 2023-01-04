package plm.patientslikeme2.ui.base

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import plm.patientslikeme2.R
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.global.FeatureFlagModel
import plm.patientslikeme2.databinding.LayoutSuccessErrorBinding
import plm.patientslikeme2.databinding.LayoutToolbarHomeBinding
import plm.patientslikeme2.ui.main.view.activity.home.MainActivity
import plm.patientslikeme2.ui.main.view.activity.onboarding.OnBoardingActivity
import plm.patientslikeme2.ui.main.viewmodel.global.GlobalViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.IS_REMEMBER_ME
import plm.patientslikeme2.utils.Constants.LOGIN_PASSWORD
import plm.patientslikeme2.utils.Constants.LOGIN_USER_NAME
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.enum.Theme
import plm.patientslikeme2.utils.usercontrol.CommonDialog

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    abstract fun createBinding(): B

    protected lateinit var binding: B
    private var progressDialog: Dialog? = null
    var toolbarHome: LayoutToolbarHomeBinding? = null
    var successErrorBinding: LayoutSuccessErrorBinding? = null
    var bottomMenuBar: BottomNavigationView? = null
    private val viewModel: GlobalViewModel by viewModels()

    companion object {
        var appThemeMode = Theme.System
        var isThemeChange: Boolean = true
        private var Values = Theme.values()
        fun getByValue(value: Int): Theme {
            return Values.firstOrNull { it.ordinal == value } ?: Theme.System
        }

        var isOnRestart = false
        var featureFlag: FeatureFlagModel = FeatureFlagModel()
    }

    fun openMainActivity() {
        hideSoftKeyboardInput()
        startActivity(MainActivity.newIntent(this))
        finish()
    }

    fun openOnboardingActivity() {
        hideSoftKeyboardInput()
        val rememberMe = Preferences.getValue(IS_REMEMBER_ME, false)
        val userName = Preferences.getValue(LOGIN_USER_NAME, "")
        val password = Preferences.getValue(LOGIN_PASSWORD, "")
        Preferences.clearAll()
        if (rememberMe) {
            Preferences.setValue(IS_REMEMBER_ME, true)
            Preferences.setValue(LOGIN_USER_NAME, userName)
            Preferences.setValue(LOGIN_PASSWORD, password)
        }
        startActivity(OnBoardingActivity.newIntent(this))
        finish()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = createBinding()
        this.binding = binding
        setContentView(binding.root)

        if (isThemeChange) {
            appThemeMode = getByValue(
                Preferences.getValue(
                    Constants.APP_THEME,
                    Theme.System.ordinal
                )
            )
            changeTheme()
            isThemeChange = false
        }
        progressDialog = CommonDialog.showLoadingDialog(this)

        if (Preferences.getValue(Constants.IS_LOGGED_IN, false)) {
            if (MyApplication.APP_MODE == Environment.LIVE) {
                startRefreshingTokens()
            }
        }
    }

    fun bindFeatureFlag(callback: (() -> Unit)) {
        viewModel.getFeatureFlagData().observe(this) {
            when {
                it.status.isSuccessful() -> {
                    it.data?.data?.feature_flags?.let {
                        featureFlag = it
                        callback.invoke()
                    }
                }
                it.status.isError() -> {
                    callback.invoke()
                }
            }
        }
    }

    private fun startRefreshingTokens() {
        lifecycleScope.launch {
            // Suspend the coroutine until the lifecycle is DESTROYED.
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            //delay(1000 * 7000) // Initial delay as current token is valid for 7000 sec
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Safely collect from locations when the lifecycle is STARTED
                // and stop collecting when the lifecycle is STOPPED
                repeat(Int.MAX_VALUE) { i ->
                    viewModel.refreshToken().observe(this@BaseActivity) { response ->
                        when {
                            response.status.isSuccessful() -> {
                                response.data?.let { it ->
                                    Preferences.setValue(Constants.ACCESS_TOKEN, it.access_token)
                                    Preferences.setValue(Constants.REFRESH_TOKEN, it.refresh_token)
                                }
                            }
                            response.status.isError() -> {
                                openOnboardingActivity()
                            }
                        }
                    }
                    delay(1000 * 7000) // refresh tokens every 7000 sec
                }
            }
            // Note: at this point, the lifecycle is DESTROYED!
        }
    }

    override fun onResume() {
        super.onResume()
        this.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            decorView.apply {
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            statusBarColor = ContextCompat.getColor(this@BaseActivity, R.color.white)
        }
    }

    fun setTransparentBackground() {
        val window = this.window
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun showProgress() {
        hideProgress()
        progressDialog.let {
            if (it != null && !it.isShowing) {
                it.show()
            }
        }
    }

    fun hideProgress() {
        progressDialog.let {
            if (it != null && it.isShowing) {
                it.dismiss()
            }
        }
    }

    open fun changeTheme() {
        when (appThemeMode) {
            Theme.System -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            Theme.Light -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Theme.Dark -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    open fun refreshTheme() {
        isOnRestart = true
        appThemeMode = getByValue(Preferences.getValue(Constants.APP_THEME, Theme.System.ordinal))
        changeTheme()
    }

    fun hideSoftKeyboardInput() {
        try {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus?.windowToken, 0
            )
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun changeMenuItemCheckedStateColor(
        checkedColorHex: String,
        uncheckedColorHex: String
    ) {
        val checkedColor: Int = Color.parseColor(checkedColorHex)
        val uncheckedColor: Int = Color.parseColor(uncheckedColorHex)
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
        val colors = intArrayOf(
            uncheckedColor,
            checkedColor
        )
        val colorStateList = ColorStateList(states, colors)
        bottomMenuBar?.itemTextColor = colorStateList
        bottomMenuBar?.itemIconTintList = colorStateList
    }
}