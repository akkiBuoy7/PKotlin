package plm.patientslikeme2.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import plm.patientslikeme2.R
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences

fun getsSymptomSeverity(severity: String, context: Context): Drawable? {
    return when (severity) {
        Constants.NONE -> ContextCompat.getDrawable(context, R.drawable.ic_none)
        Constants.MILD -> ContextCompat.getDrawable(context, R.drawable.ic_mild)
        Constants.MODERATE -> ContextCompat.getDrawable(context, R.drawable.ic_moderate)
        Constants.SEVERE -> ContextCompat.getDrawable(context, R.drawable.ic_severe)
        else -> ContextCompat.getDrawable(context, R.drawable.ic_none)
    }
}

fun getProfileColor(context: Context): Int {
    return when (Preferences.getValue(Constants.PROFILE_COLOR, Constants.BLUE)) {
        Constants.BLUE -> context.getColor(R.color.blue)
        Constants.MAGENTA -> context.getColor(R.color.purple)
        Constants.TEAL -> context.getColor(R.color.pink)
        Constants.ORANGE -> context.getColor(R.color.orange)
        else -> context.getColor(R.color.blue)
    }
}

fun getsTreatmentSeverity(severity: String, context: Context): Drawable? {
    return when (severity) {
        Constants.MAJOR -> ContextCompat.getDrawable(context, R.drawable.ic_major)
        Constants.MODERATE -> ContextCompat.getDrawable(context, R.drawable.ic_moderate_green)
        else -> ContextCompat.getDrawable(context, R.drawable.ic_major)
    }
}