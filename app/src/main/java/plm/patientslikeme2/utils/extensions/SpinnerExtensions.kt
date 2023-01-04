package plm.patientslikeme2.utils.extensions

import android.content.Context
import android.widget.Spinner
import plm.patientslikeme2.R
import plm.patientslikeme2.ui.main.adapter.usercontrol.*

inline fun <reified T : Any> Spinner.addHintWithArrayList(context: Context, list: ArrayList<T>) {
    val hintAdapter = SpinnerHintAdapter(context, R.layout.layout_simple_spinner_item, list)
    adapter = hintAdapter
    setSelection(hintAdapter.count)
}

inline fun < T : Any> Spinner.addItemSelectedAdapter(context: Context, list: ArrayList<T>) {
    val hintAdapter = SpinnerItemSelectedAdapter(context, R.layout.layout_simple_spinner_item, list)
    adapter = hintAdapter
    setSelection(hintAdapter.count)
}

inline fun <reified T : Any> Spinner.addHintWithArray(context: Context, list: Array<T>) {
    val hintAdapter = SpinnerHintArrayAdapter(context, R.layout.layout_simple_spinner_item, list)
    adapter = hintAdapter
    setSelection(hintAdapter.count)
}

inline fun <reified T : Any> Spinner.addWithArrayList(context: Context, list: ArrayList<T>) {
    val hintAdapter = SpinnerAdapter(context, R.layout.layout_simple_spinner_item_gray, list)
    adapter = hintAdapter
}

inline fun <reified T : Any> Spinner.addFirstPositionAsHintWithArrayList(
    context: Context,
    list: ArrayList<T>
) {
    val hintAdapter = SpinnerFirstPositionAsHintAdapter(context, R.layout.layout_simple_spinner_item, list)
    adapter = hintAdapter
}