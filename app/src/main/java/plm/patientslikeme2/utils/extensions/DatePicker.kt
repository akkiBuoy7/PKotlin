package plm.patientslikeme2.utils.extensions

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.DateFormatSymbols
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.TextView
import java.util.*
import kotlin.math.abs

val DOB_FORMATTER = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)

fun showDOBDatePicker(context: Context?, tvDay: TextView?, tvMonth: TextView?, tvYear: TextView?) {
    val cal = Calendar.getInstance()
    val selectedDay = tvDay?.text.toString()
    val selectedMonth = tvMonth?.text.toString()
    val selectedYear = tvYear?.text.toString()
    if (selectedDay.isNotEmpty() && selectedMonth.isNotEmpty() && selectedYear.isNotEmpty()) {
        cal.set(selectedYear.toInt(), getMonthNumber(selectedMonth), selectedDay.toInt())
    }

    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val monthFormat = SimpleDateFormat("MMM", Locale.ENGLISH)

        tvDay?.text = dayOfMonth.toString()
        tvMonth?.text = monthFormat.format(cal.time).toString()
        tvYear?.text = year.toString()
    }
    context?.let {
        DatePickerDialog(
            it,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

fun calculateAge(dob: String): Long {
    val currentDate = DOB_FORMATTER.parse(getCurrentDate())
    val dobDate = DOB_FORMATTER.parse(dob)
    val difference = abs(currentDate.time - dobDate.time)
    return difference / (24 * 60 * 60 * 1000)
}

fun futureDateValidation(dob: String?): Int {
    val currentDate = DOB_FORMATTER.parse(getCurrentDate())
    val dobDate = DOB_FORMATTER.parse(dob)
    //  0 comes when two date are same,
    //  1 comes when date1 is higher then date2
    // -1 comes when date1 is lower then date2
    return currentDate.compareTo(dobDate)
}

fun getCurrentDate(): String {
    return DOB_FORMATTER.format(Date())
}

fun getMonthNumber(monthName: String): Int {
    val date = SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthName)
    val cal = Calendar.getInstance()
    cal.time = date
    return cal[Calendar.MONTH]
}

fun getMonthName(month: Int): String? {
    return DateFormatSymbols().months.get(month - 1).substring(0..2)
}
