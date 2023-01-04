package plm.patientslikeme2.utils.usercontrol

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatCheckedTextView
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.getFonts
import plm.patientslikeme2.utils.extensions.getTypeFace
import plm.patientslikeme2.utils.extensions.getUpdatedFontSettings

open class UserCheckedTextView : AppCompatCheckedTextView {
    var defaultTextSize: Float = textSize

    var defaultLineSpacing: Float = lineSpacingExtra

    constructor(context: Context?) : super(context!!) {
        setFont()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
        setFont()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        setFont()
    }

    private fun setFont() {
        val fontFamily = getTypeFace(this.tag)
        fontFamily?.let {
            this.typeface = getFonts(context, it)
        }
        setFontSize()
    }

    private fun setFontSize() {
        val preferences = context.getSharedPreferences(Constants.Preference, Context.MODE_PRIVATE)
        preferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == Constants.FONT_SIZE) setFontBySettingValue(preferences)
        }
        setFontBySettingValue(preferences)
    }

    private fun setFontBySettingValue(preferences: SharedPreferences) {
        val fontSizeEnum = preferences.getInt(Constants.FONT_SIZE, 0)
        var sp: Float = defaultTextSize / resources.displayMetrics.scaledDensity
        if (preferences.getBoolean(Constants.IS_LOGGED_IN, false)) {
            sp = getUpdatedFontSettings(sp, fontSizeEnum)
        }
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
        setLineSpacing(preferences, fontSizeEnum)
    }

    private fun setLineSpacing(preferences: SharedPreferences, fontSizeEnum: Int) {
        if (maxLines > 1) {
            if (preferences.getBoolean(Constants.IS_LOGGED_IN, false)) {
                val spacing = getUpdatedFontSettings(defaultLineSpacing, fontSizeEnum)
                setLineSpacing(spacing, 1f)
            }
        }
    }
}