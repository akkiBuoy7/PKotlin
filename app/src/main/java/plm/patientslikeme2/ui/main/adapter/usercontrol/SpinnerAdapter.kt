package plm.patientslikeme2.ui.main.adapter.usercontrol

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class SpinnerAdapter<T>(context: Context, resource: Int, objects: ArrayList<T>) :
    ArrayAdapter<T>(context, resource, objects) {

    @SuppressLint("CutPasteId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return super.getCount()
    }
}