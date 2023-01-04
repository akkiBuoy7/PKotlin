package plm.patientslikeme2.utils.extensions

import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.text.SimpleDateFormat
import java.util.*

fun String.formatDate(inputFormat: String, outputFormat: String): String {
    return try {
        SimpleDateFormat(outputFormat).format(SimpleDateFormat(inputFormat).parse(this))
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
        this
    }
}

fun formatDate(dateString:String?): String {
    val oldFormat = SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH)
    val date = oldFormat.parse(dateString)
    val newFormat = SimpleDateFormat("MMM, yyyy",Locale.ENGLISH)
    val output = date?.let { newFormat.format(it) }
    return output.toString()
}

fun formatDateToMMMDDYYYY(dateString:String?): String {
    val oldFormat = SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH)
    val date = oldFormat.parse(dateString)
    val newFormat = SimpleDateFormat("MMM dd, yyyy",Locale.ENGLISH)
    val output = date?.let { newFormat.format(it) }
    return output.toString()
}

fun formatTime(timeString:String?): Pair<Int,Int> {
    val oldFormat = SimpleDateFormat("hh:mm",Locale.ENGLISH)
    val dTime = oldFormat.parse(timeString)
    val hour: Int = dTime.hours
    val minute: Int = dTime.minutes
    return Pair(hour,minute)
}

fun String.getTimeDifferenceFromCurrent(inputFormat: String): String {

    return try {
        val dateFormat = SimpleDateFormat(inputFormat) //"yyyy-MM-dd HH:mm:ss"
        val oldDate: Date = dateFormat.parse(this)
        val currentDate = Date()

        val diff: Long = currentDate.time - oldDate.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return if (oldDate.before(currentDate)) {
            if (days > 0) {
                "$days days"
            } else if (hours > 0) {
                "$hours hr"
            } else if (minutes > 0) {
                "$minutes min"
            } else {
                "$seconds sec"
            }
        } else {
            this
        }
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
        this
    }
}

fun convertDate(existingFormat: String, newFormat: String, existingDate: String?): String? {
    val existingSdf = SimpleDateFormat(existingFormat, Locale.ENGLISH)
    val newSdf = SimpleDateFormat(newFormat, Locale.ENGLISH)
    existingSdf.timeZone = TimeZone.getTimeZone("UTC")

    val dateIs = existingSdf.parse(existingDate)
    val cal = Calendar.getInstance()
    cal.timeInMillis = dateIs.time
    newSdf.timeZone = TimeZone.getDefault()

    return newSdf.format(cal.time)
}