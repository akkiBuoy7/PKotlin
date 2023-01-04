package plm.patientslikeme2.ui.main.adapter.community

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.Discussions
import plm.patientslikeme2.data.model.community.groups.discussions.GroupDiscussions
import plm.patientslikeme2.databinding.RowSuggestionsDiscussionsDropDownBinding

class DiscussionsAutoCompleteAdapter(
    context: Context, resource: Int, val listener: OnItemClickListener
) : ArrayAdapter<Discussions>(context, resource), Filterable {
    private val mainList: MutableList<Discussions>

    init {
        mainList = ArrayList()
    }

    fun setData(discussions: GroupDiscussions) {
        discussions.data?.discussions?.let{
            mainList.clear()
            mainList.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return mainList.size
    }

    override fun getItem(position: Int): Discussions {
        return mainList[position]
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val binding: RowSuggestionsDiscussionsDropDownBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_suggestions_discussions_drop_down, parent, false)
            convertView = binding.root
        } else {
            binding = convertView.tag as RowSuggestionsDiscussionsDropDownBinding
        }
        val user = getItem(position)
        binding.suggestions = user
        convertView.tag = binding

        binding.root.setOnClickListener {
            listener.onItemClick(getItem(position))
        }
        binding.divider.isVisible = (position == mainList.size - 1).not()
        return convertView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = mainList
                    filterResults.count = mainList.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(item: Discussions?)
}