package plm.patientslikeme2.ui.main.adapter.usercontrol

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.text.HtmlCompat

class SpinnerFirstPositionAsHintAdapter<T>(context: Context, resource: Int, objects: ArrayList<T>) :
    ArrayAdapter<T>(context, resource, objects) {

    @SuppressLint("CutPasteId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if (position == 0) {
            (view.findViewById(android.R.id.text1) as TextView).text = ""
            (view.findViewById(android.R.id.text1) as TextView).hint =
                HtmlCompat.fromHtml(
                    "<i>" + getItem(0).toString() + "</i>",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
        }
        return view
    }
}