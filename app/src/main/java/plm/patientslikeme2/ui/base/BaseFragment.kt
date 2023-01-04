package plm.patientslikeme2.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import plm.patientslikeme2.R
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.action.ActionModel
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.global.FeatureFlagModel
import plm.patientslikeme2.databinding.LayoutSuccessErrorBinding
import plm.patientslikeme2.databinding.LayoutToolbarHomeBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.main.view.activity.home.MainActivity
import plm.patientslikeme2.ui.main.view.activity.onboarding.OnBoardingActivity
import plm.patientslikeme2.ui.main.viewmodel.global.GlobalViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.usercontrol.CommonDialog

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    var binding: B? = null
    var toolbarBinding: LayoutToolbarHomeBinding? = null
    private var successErrorBinding: LayoutSuccessErrorBinding? = null
    var currentFragment: Int = 0
    private val viewModel: GlobalViewModel by activityViewModels()
    private var bottomMenuBar: BottomNavigationView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = onCreateBinding(inflater, container, savedInstanceState)
        this.binding = binding
        return binding.root
    }

    abstract fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): B

    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = CommonDialog.showLoadingDialog(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Preferences.getValue(Constants.IS_LOGGED_IN, false)) {
            bindFeatureFlag()
        }

        if (activity is MainActivity) {
            toolbarBinding = (activity as MainActivity).toolbarHome
        }

        if (activity is MainActivity) {
            successErrorBinding = (activity as MainActivity).successErrorBinding
        } else if (activity is OnBoardingActivity) {
            successErrorBinding = (activity as OnBoardingActivity).successErrorBinding
        }

        toolbarBinding?.ivBack?.setOnClickListener {
            if (currentFragment > 0) {
                findNavController().popBackStack(currentFragment, true)
            }
        }

        toolbarBinding?.ivClose?.setOnClickListener {
            if (currentFragment > 0) {
                findNavController().popBackStack(currentFragment, true)
            }
        }
    }

    fun updateBottomMenuBarState(icon: String, text: String) {
        (activity as MainActivity).changeMenuItemCheckedStateColor(icon, text)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        currentFragment = 0
    }

    fun openMainActivity() {
        (activity as OnBoardingActivity).openMainActivity()
    }

    fun showProgress() {
        if (isAdded) {
            progressDialog.let {
                if (it?.isShowing == false) it.show()
            }
        }
    }

    fun hideProgress() {
        if (isAdded) {
            progressDialog.let {
                if (it?.isShowing == true) it.dismiss()
            }
        }
    }

    fun showSuccessErrorLayout(type: Boolean, message: String) {
        if (!isAdded) {
            return
        }
        val drawable = if (type) {
            activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_success) }
        } else {
            activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_error) }
        }
        if (message.isEmpty()) {
            message == getString(R.string.error_internet)
        }
        successErrorBinding?.model = drawable?.let { ActionModel(type, it, message) }
        successErrorBinding?.executePendingBindings()
        successErrorBinding?.ivClose?.setOnClickListener {
            successErrorBinding?.cardView?.visibility = View.GONE
        }
        successErrorBinding?.cardView?.visibility = View.VISIBLE
        successErrorBinding?.cardView?.postDelayed({
            successErrorBinding?.cardView?.visibility = View.GONE
        }, 5000)
    }

    fun updateToolbar(
        title: String,
        showBackArrow: Boolean = false,
        hideRightIcon: Boolean = false,
        showCloseRightIcon: Boolean = false,
        showRightAction: Boolean = false,
        actionButtonText: String = ""
    ) {
        if (isAdded) {
            toolbarBinding?.toolbarTitle?.text = title
            if (showBackArrow) {
                toolbarBinding?.ivBack?.visibility = View.VISIBLE
                toolbarBinding?.ivMenu?.visibility = View.GONE
                toolbarBinding?.llRight?.visibility = View.INVISIBLE
            } else {
                toolbarBinding?.ivBack?.visibility = View.GONE
                toolbarBinding?.ivMenu?.visibility = View.VISIBLE
                toolbarBinding?.llRight?.visibility = View.VISIBLE
                toolbarBinding?.ivMessaging?.visibility = View.VISIBLE
                toolbarBinding?.ivNotification?.visibility = View.VISIBLE
                toolbarBinding?.ivClose?.visibility = View.GONE
            }
            if (hideRightIcon) {
                toolbarBinding?.llRight?.visibility = View.INVISIBLE
            } else {
                toolbarBinding?.llRight?.visibility = View.VISIBLE
            }
            if (showCloseRightIcon) {
                toolbarBinding?.ivBack?.visibility = View.GONE
                toolbarBinding?.ivMessaging?.visibility = View.GONE
                toolbarBinding?.ivNotification?.visibility = View.GONE
                toolbarBinding?.ivClose?.visibility = View.VISIBLE
                toolbarBinding?.llRight?.visibility = View.VISIBLE
            } else {
                toolbarBinding?.ivMessaging?.visibility = View.VISIBLE
                toolbarBinding?.ivNotification?.visibility = View.VISIBLE
                toolbarBinding?.ivClose?.visibility = View.GONE
            }
            if (showRightAction) {
                toolbarBinding?.btnAction?.visibility = View.VISIBLE
                toolbarBinding?.btnAction?.text = actionButtonText
            } else {
                toolbarBinding?.btnAction?.visibility = View.INVISIBLE
            }
        }
    }

    fun removeOptionMoreIcon() {
        toolbarBinding?.ivClose?.setImageResource(R.drawable.ic_close_dark_green)
        toolbarBinding?.ivMessaging?.visibility = View.VISIBLE
        toolbarBinding?.ivNotification?.visibility = View.VISIBLE
        toolbarBinding?.ivClose?.visibility = View.GONE
        toolbarBinding?.llRight?.visibility = View.GONE
    }

    fun setMessagingIcon(isPrivateGroup: Boolean = false) {
        if (isPrivateGroup) {
            toolbarBinding?.ivMessaging?.setImageDrawable(if (MyApplication.unreadMessageCount == 0)
                ContextCompat.getDrawable(MyApplication.appContext,
                    R.drawable.ic_messages_without_badge_white)
            else ContextCompat.getDrawable(MyApplication.appContext,
                R.drawable.ic_messages_with_badge_white))

            toolbarBinding?.ivNotification?.setImageDrawable(if (MyApplication.unreadNotificationCount == 0)
                ContextCompat.getDrawable(MyApplication.appContext,
                    R.drawable.ic_notification_white)
            else ContextCompat.getDrawable(MyApplication.appContext,
                R.drawable.ic_notification_badge_white))
        } else {
            toolbarBinding?.ivMessaging?.setImageDrawable(if (MyApplication.unreadMessageCount == 0)
                ContextCompat.getDrawable(MyApplication.appContext,
                    R.drawable.ic_messages_without_badge_black)
            else ContextCompat.getDrawable(MyApplication.appContext,
                R.drawable.ic_messages_with_badge_black))
            toolbarBinding?.ivNotification?.setImageDrawable(if (MyApplication.unreadNotificationCount == 0)
                ContextCompat.getDrawable(MyApplication.appContext,
                    R.drawable.ic_notification)
            else ContextCompat.getDrawable(MyApplication.appContext,
                R.drawable.ic_notification_badge))
        }
    }

    fun isConnected(): Boolean {
        if (!isAdded) {
            return false
        }
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    }

    fun startFwdAnimation(activity: Activity?) {
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun startBackAnimation(activity: Activity?) {
        activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun hideSoftKeyboardInput() {
        try {
            activity?.let {
                val inputMethodManager =
                    it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    it.currentFocus?.windowToken, 0
                )
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun apiErrorMessage(string: String?): String {
        return ErrorResponse().stringToObject(string).message.toString()
    }

    fun hideBottomNavigationMenu(status: Boolean) {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom_view)
        navBar?.visibility = if (status) View.GONE else View.VISIBLE
    }

    private fun bindFeatureFlag() {
        viewModel.getFeatureFlagData().observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    featureFlag = it.data?.data?.feature_flags ?: FeatureFlagModel()
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun adjustKeyBoard(isResize: Boolean) {
        if (activity is MainActivity) {
            bottomMenuBar = (activity as MainActivity).bottomMenuBar
        }
        activity?.window?.decorView?.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets, view)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                bottomMenuBar?.isGone = insetsCompat.isVisible(WindowInsets.Type.ime())
            }
            view.onApplyWindowInsets(insets)
        }
        if (isResize) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        } else {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }
}