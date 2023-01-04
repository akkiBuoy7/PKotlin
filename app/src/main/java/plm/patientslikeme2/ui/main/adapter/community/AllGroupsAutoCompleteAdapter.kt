package plm.patientslikeme2.ui.main.adapter.community

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import plm.patientslikeme2.data.model.community.allgroups.querygroup.Group
import plm.patientslikeme2.databinding.RowAutoCompleteItemBinding
import plm.patientslikeme2.utils.extensions.OnAutoCompleteItemClickListener
import java.util.*


class AllGroupsAutoCompleteAdapter(
    c: Context,
    private val layoutResource: Int,
    val groupList: ArrayList<Group>?,
    val listener: OnAutoCompleteItemClickListener,
) :
    ArrayAdapter<Group>(c, layoutResource, groupList!!) {

    var filerUsersList: List<Group> = ArrayList()

    override fun getCount(): Int = filerUsersList.size

    override fun getItem(position: Int): Group = filerUsersList[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        val binding: RowAutoCompleteItemBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                plm.patientslikeme2.R.layout.row_auto_complete_item, parent, false
            )
            convertView = binding.getRoot()
        } else {
            binding = convertView.tag as RowAutoCompleteItemBinding
        }
        binding.model = getItem(position)
        convertView.tag = binding

        binding.root.setOnClickListener {
            listener.onItemClick(getItem(position))
        }

        return convertView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                filerUsersList = filterResults.values as List<Group>
                notifyDataSetChanged()
            }
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.lowercase(Locale.getDefault())

                val filterResults = FilterResults()
                filterResults.values = if ((queryString == null) || queryString.isEmpty())
                    groupList
                else
                    groupList?.filter {
                        it.name.lowercase(Locale.getDefault()).contains(queryString)
                    }
                return filterResults
            }
        }
    }
}