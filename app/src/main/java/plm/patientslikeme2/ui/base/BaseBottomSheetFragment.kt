package plm.patientslikeme2.ui.base

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ActionModel
import plm.patientslikeme2.databinding.LayoutSuccessErrorBinding
import plm.patientslikeme2.utils.usercontrol.CommonDialog

abstract class BaseBottomSheetFragment<B : ViewBinding> : BottomSheetDialogFragment() {

    protected var binding: B? = null
    var currentFragment: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = onCreateBinding(inflater, container, savedInstanceState)
        this.binding = binding
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
        progressDialog = CommonDialog.showLoadingDialog(requireActivity())
    }

    abstract fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): B

    private var progressDialog: Dialog? = null

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        currentFragment = 0
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

    fun showSuccessErrorLayout(
        binding: LayoutSuccessErrorBinding?,
        type: Boolean,
        message: String
    ) {
        if (!isAdded) {
            return
        }
        val drawable = if (type) {
            activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_success) }
        } else {
            activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_error) }
        }
        binding?.model = drawable?.let { ActionModel(type, it, message) }
        binding?.executePendingBindings()
        binding?.ivClose?.setOnClickListener {
            binding.cardView.visibility = View.GONE
        }
        binding?.cardView?.visibility = View.VISIBLE
        binding?.cardView?.postDelayed({
            if (isAdded && binding.cardView.isVisible) {
                binding.cardView.visibility = View.GONE
            }
        }, 5000)
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
}