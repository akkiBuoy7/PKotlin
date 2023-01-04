package plm.patientslikeme2.ui.main.view.fragment.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentScreenBrightnessBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences

@AndroidEntryPoint
class ScreenBrightnessFragment : BaseFragment<FragmentScreenBrightnessBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentScreenBrightnessBinding =
        FragmentScreenBrightnessBinding.inflate(inflater, container, false)

    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var contentObserver: Any

    override fun onAttach(context: Context) {
        super.onAttach(context)

        someActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    onBrightnessChange()
                }
            }

        contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {

            override fun onChange(selfChange: Boolean) {
                // get system brightness level
                if (!selfChange) {
                    val brightnessAmount = Settings.System.getInt(
                        context.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS, 0
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.screenBrightnessFragment
        initView()
    }

    private fun initView() {
        binding?.sbBrightness?.progress =
            Settings.System.getInt(activity?.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        val userBrightness = Preferences.getValue(Constants.brightness, 0)
        if (userBrightness != null) {
            binding?.sbBrightness?.progress = userBrightness
        }
        context?.contentResolver?.registerContentObserver(
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS),
            false, (contentObserver as ContentObserver)
        )

        binding?.ivAdd?.setOnClickListener {
            applyBrightnessChange()
        }
        binding?.ivMinus?.setOnClickListener {
            applyBrightnessChange()
        }
        binding?.sbBrightness?.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    private var seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            binding?.sbBrightness?.progress = progress
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            try {
                applyBrightnessChange()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun applyBrightnessChange() {
        if (checkSystemWritePermission()) {
            onBrightnessChange()
        } else {
            openAndroidPermissionsMenu()
        }
    }

    private fun onBrightnessChange() {
        Settings.System.putInt(
            context?.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
        val brightness = binding?.sbBrightness?.progress?.times(255)?.div(100)
        brightness?.let { it1 ->
            Preferences.setValue(Constants.brightness, it1)
        }

        brightness?.let {
            Settings.System.putInt(
                context?.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                it
            )
        }
    }

    private fun checkSystemWritePermission(): Boolean {
        if (Settings.System.canWrite(context)) return true else openAndroidPermissionsMenu()
        return false
    }

    private fun openAndroidPermissionsMenu() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + requireContext().packageName)
        someActivityResultLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.screen_brightness),
            showBackArrow = true,
            hideRightIcon = true
        )
    }

    override fun onDetach() {
        super.onDetach()
        context?.contentResolver?.unregisterContentObserver((contentObserver as ContentObserver))
    }
}