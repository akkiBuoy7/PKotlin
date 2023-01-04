package plm.patientslikeme2.utils.extensions

import android.content.Context
import android.widget.ArrayAdapter
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.community.allgroups.querygroup.Group
import plm.patientslikeme2.ui.main.adapter.community.AllGroupsAutoCompleteAdapter
import plm.patientslikeme2.utils.usercontrol.UserAutoCompleteTextView

inline fun <reified T : Any> UserAutoCompleteTextView.addHintWithArrayList(
    context: Context,
    list: ArrayList<T>
) {
    val adapter = ArrayAdapter(context, R.layout.layout_auto_complete_list_item, list)
    threshold = 1
    setAdapter(adapter)
    adapter.notifyDataSetChanged()
    adapter.setNotifyOnChange(true)
    setOnFocusChangeListener { _, b -> if (b) this.showDropDown() }
}

fun UserAutoCompleteTextView.addHintWithCustomArray(
    context: Context,
    list: ArrayList<Group>,
    listener: OnAutoCompleteItemClickListener,
    query: String
) {
    val adapter = AllGroupsAutoCompleteAdapter(
        context, R.layout.row_auto_complete_item, list,
        listener
    )
    threshold = 1
    setAdapter(adapter)
    adapter.notifyDataSetChanged()
    adapter.filter.filter(query)
    setOnFocusChangeListener({ view, b -> if (b) this.showDropDown() })
}
