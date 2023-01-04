package plm.patientslikeme2.ui.main.adapter.usercontrol

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.text.HtmlCompat

class SpinnerItemSelectedAdapter<T>(context: Context, resource: Int, objects: ArrayList<T>) :
    ArrayAdapter<T>(context, resource, objects) {

    @SuppressLint("CutPasteId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if (position == count) {
            (view.findViewById(android.R.id.text1) as TextView).text =
                HtmlCompat.fromHtml(
                    "<i>" + getItem(count).toString() + "</i>",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
        }
        return view
    }

    override fun getCount(): Int {
        val count = super.getCount()
        // The last item will be the hint.
        return count - 1
    }
}