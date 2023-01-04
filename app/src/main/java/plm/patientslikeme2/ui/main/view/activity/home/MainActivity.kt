package plm.patientslikeme2.ui.main.view.activity.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.amlitude.AmplitudeEvents.trackEvent
import plm.patientslikeme2.data.amlitude.EventConstants
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.di.MyApplication.Companion.amplitude
import plm.patientslikeme2.data.model.home.NavMenuModel
import plm.patientslikeme2.data.model.login.UserDevices
import plm.patientslikeme2.databinding.ActivityMainBinding
import plm.patientslikeme2.ui.base.BaseActivity
import plm.patientslikeme2.ui.main.adapter.home.DrawerNavMenuAdapter
import plm.patientslikeme2.ui.main.viewmodel.home.HomeViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.CRISIS_RESOURCES
import plm.patientslikeme2.utils.Constants.HELP_CENTER
import plm.patientslikeme2.utils.Constants.MANAGE_ACCOUNT
import plm.patientslikeme2.utils.Constants.PRIVACY_URL
import plm.patientslikeme2.utils.Constants.TERMS_CONDITIONS
import plm.patientslikeme2.utils.Constants.USER_DEVICES
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.ActionDialogListener
import plm.patientslikeme2.utils.extensions.inviteOthers
import plm.patientslikeme2.utils.extensions.openHyperLinks
import plm.patientslikeme2.utils.extensions.showActionDialog

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        if (!Preferences.getValue(USER_DEVICES, false)) {
            updateUserDevicesDetails()
        }
        if (Preferences.getValue(Constants.USER, "")?.isEmpty() == true) {
            bingUserDetailsObserver()
        }
        bingMessageCountObserver()
        bingNotificationCountObserver()
    }

    private fun initView() {
        toolbarHome = binding.toolbar
        successErrorBinding = binding.icSuccessError
        bottomMenuBar = binding.navBottomView
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    binding.navBottomView.itemIconTintList = colorStateListOf(
                        intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.gray_scale),
                        intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.bottomBarGreen),
                    )
                    binding.navBottomView.menu[0].isChecked = true
                }
                R.id.navigation_community -> {
                    binding.navBottomView.itemIconTintList = colorStateListOf(
                        intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.gray_scale),
                        intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.bottomBarBlue),
                    )
                    binding.navBottomView.menu[1].isChecked = true
                }
                R.id.navigation_my_health -> {
                    binding.navBottomView.itemIconTintList = colorStateListOf(
                        intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.gray_scale),
                        intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.bottomBarPink),
                    )
                    binding.navBottomView.menu[2].isChecked = true
                }
                R.id.navigation_learning -> {
                    binding.navBottomView.itemIconTintList = colorStateListOf(
                        intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.gray_scale),
                        intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.bottomBarOrange),
                    )
                    binding.navBottomView.menu[3].isChecked = true
                }
            }
        }

        binding.navBottomView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    binding.navBottomView.itemIconTintList = colorStateListOf(
                        intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.gray_scale),
                        intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.bottomBarGreen),
                    )
                    navController.navigate(R.id.navigation_home)
                }
                R.id.navigation_community -> {
                    binding.navBottomView.itemIconTintList = colorStateListOf(
                        intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.gray_scale),
                        intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.bottomBarBlue),
                    )
                    navController.navigate(R.id.navigation_community)
                }
                R.id.navigation_my_health -> {
                    binding.navBottomView.itemIconTintList = colorStateListOf(
                        intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.gray_scale),
                        intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.bottomBarPink),
                    )
                    navController.navigate(R.id.navigation_my_health)
                }
                R.id.navigation_learning -> {
                    binding.navBottomView.itemIconTintList = colorStateListOf(
                        intArrayOf(-android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.gray_scale),
                        intArrayOf(android.R.attr.state_selected) to ContextCompat.getColor(this,
                            R.color.bottomBarOrange),
                    )
                    navController.navigate(R.id.navigation_learning)
                }
            }
            return@setOnItemSelectedListener true
        }

        bindFeatureFlag {
            setUpNavigationView()
        }

        toolbarHome?.ivMenu?.setOnClickListener {
            toggleDrawer()
        }

        toolbarHome?.ivNotification?.setOnClickListener {
            navController.navigate(R.id.navigation_notification)
        }
    }

    private fun setUpNavigationView() {
        val headerView = binding.navView.getHeaderView(0)
        val ivClose = headerView.findViewById<View>(R.id.iv_close) as ImageView

        val rvNavMenu = headerView.findViewById<View>(R.id.rv_nav_menu) as RecyclerView
        val navAdapter = DrawerNavMenuAdapter(NavMenuModel.getDrawerNavMenuList(featureFlag))
        rvNavMenu.adapter = navAdapter
        rvNavMenu.setHasFixedSize(true)
        navAdapter.onItemClicked = { item ->
            binding.navBottomView.selectedItemId = R.id.navigation_hidden
            when (item.title) {
                R.string.nav_menu_following -> {
                    toggleDrawer()
                    trackEvent(
                        EventConstants.MAIN_MENU_CLICKED,
                        mutableMapOf(EventConstants.TAB_PATH to EventConstants.FOLLOWING)
                    )
                    navController.navigate(R.id.navigation_following)
                }
                R.string.nav_menu_my_conditions -> {
                    toggleDrawer()
                    navController.navigate(R.id.navigation_my_conditions)
                }
                R.string.nav_menu_my_treatments -> {
                    toggleDrawer()
                    navController.navigate(R.id.navigation_my_treatments)
                }
                R.string.nav_menu_personal_information -> {
                    toggleDrawer()
                    navController.navigate(R.id.navigation_personal_info)
                }
                R.string.nav_menu_settings -> {
                    toggleDrawer()
                    navController.navigate(R.id.navigation_settings)
                }
                R.string.nav_menu_invite_others -> {
                    toggleDrawer()
                    inviteOthers(this)
                }
                R.string.nav_menu_crisis_resources -> {
                    toggleDrawer()
                    trackEvent(
                        EventConstants.MAIN_MENU_CLICKED_EXTERNAL_LINKS,
                        mutableMapOf(EventConstants.CRISIS_RESOURCES to CRISIS_RESOURCES)
                    )
                    openHyperLinks(this, CRISIS_RESOURCES)
                }
                R.string.nav_menu_my_account -> {
                    toggleDrawer()
                    openHyperLinks(this, MANAGE_ACCOUNT)
                }
                R.string.nav_menu_help_center -> {
                    toggleDrawer()
                    openHyperLinks(this, HELP_CENTER)
                }
                R.string.nav_menu_privacy_policy -> {
                    toggleDrawer()
                    openHyperLinks(this, PRIVACY_URL)
                }
                R.string.nav_menu_terms_conditions -> {
                    toggleDrawer()
                    openHyperLinks(this, TERMS_CONDITIONS)
                }
                R.string.nav_menu_logout -> {
                    trackEvent(
                        EventConstants.SESSIONS_LOG_OUT,
                        mutableMapOf(EventConstants.LOGGED_OUT to true)
                    )
                    showLogoutDialog()
                }
            }
        }

        val mDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbarHome?.activityToolbar,
            R.string.open_drawer,
            R.string.close_drawer
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                if (drawerLayout.isDrawerOpen(navView)) {
                    binding.container.translationX = (slideOffset * drawerLayout.width)
                    drawerLayout.bringChildToFront(drawerLayout)
                    drawerLayout.requestLayout()
                } else {
                    binding.container.translationX = (slideOffset * drawerLayout.width - 120)
                    drawerLayout.bringChildToFront(drawerLayout)
                    drawerLayout.requestLayout()
                }
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }
        drawerLayout.addDrawerListener(mDrawerToggle)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START)

        toolbarHome?.ivMenu?.setOnClickListener {
            trackEvent(EventConstants.MAIN_MENU_EXITED, mutableMapOf(EventConstants.CLOSE to true))
            toggleDrawer()
        }

        toolbarHome?.ivNotification?.setOnClickListener {
            navController.navigate(R.id.navigation_notification)
        }

        toolbarHome?.ivMessaging?.setOnClickListener {
            navController.navigate(R.id.navigation_messaging)
        }

        toolbarHome?.ivNotification?.visibility =
            if (featureFlag.android_menu) View.VISIBLE else View.GONE

        toolbarHome?.ivNotification?.visibility =
            if (featureFlag.android_notifications) View.VISIBLE else View.GONE

        toolbarHome?.ivMessaging?.visibility =
            if (featureFlag.android_private_messaging) View.VISIBLE else View.GONE

        ivClose.setOnClickListener {
            toggleDrawer()
        }
    }

    private fun showLogoutDialog() {
        showActionDialog(this,
            getString(R.string.logout),
            getString(R.string.logout_message),
            getString(R.string.yes),
            getString(R.string.cancel),
            object : ActionDialogListener {
                override fun onPositiveButtonClick() {
                    callLogoutAPI()
                }
            })
    }

    private fun callLogoutAPI() {
        viewModel.postUserLogout().observe(this) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    openOnboardingActivity()
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun updateUserDevicesDetails() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            val request = UserDevices(task.result, deviceId)
            viewModel.postUserDevices(request).observe(this) {
                when {
                    it.status.isSuccessful() -> {
                        if (it?.data?.success == true) {
                            Preferences.setValue(USER_DEVICES, true)
                        }
                    }
                }
            }
        })
    }

    private fun bingMessageCountObserver() {
        viewModel.getUnreadConversationCount().observe(this) {
            when {
                it.status.isSuccessful() -> {
                    if (it?.data?.success == true) {
                        MyApplication.unreadMessageCount = it.data?.data?.unread_count!!
                        binding.toolbar.ivMessaging.setImageDrawable(
                            if (it.data?.data?.unread_count == 0)
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_messages_without_badge_black
                                )
                            else ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_messages_with_badge_black
                            )
                        )
                    }
                }
            }
        }
    }

    private fun bingNotificationCountObserver() {
        viewModel.getUnreadNotificationCount().observe(this) {
            when {
                it.status.isSuccessful() -> {
                    MyApplication.unreadNotificationCount = it.data?.data?.notificationCount!!
                    binding.toolbar.ivNotification.setImageDrawable(
                        if (MyApplication.unreadNotificationCount == 0) {
                            ContextCompat.getDrawable(this, R.drawable.ic_notification)
                        } else {
                            ContextCompat.getDrawable(this, R.drawable.ic_notification_badge)
                        }
                    )
                }
            }
        }
    }

    private fun bingUserDetailsObserver() {
        viewModel.getUserDetails().observe(this) {
            when {
                it.status.isSuccessful() -> {
                    Preferences.setValue(Constants.PROFILE_COLOR, it.data?.user?.profile_colour)
                    Preferences.setValue(Constants.USER, Gson().toJson(it.data?.user))
                    amplitude?.setUserId(it.data?.user?.metric_guid.toString())
                }
            }
        }
    }

    private fun colorStateListOf(vararg mapping: Pair<IntArray, Int>): ColorStateList {
        val (states, colors) = mapping.unzip()
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            drawerLayout.openDrawer(navView)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            super.onBackPressed()
        }
    }
}