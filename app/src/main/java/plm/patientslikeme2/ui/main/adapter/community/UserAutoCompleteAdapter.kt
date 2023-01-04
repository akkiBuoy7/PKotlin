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
import plm.patientslikeme2.data.model.community.members.Users
import plm.patientslikeme2.databinding.RowUserDropDownBinding
import plm.patientslikeme2.utils.extensions.OnAutoCompleteItemClickListener
import plm.patientslikeme2.utils.extensions.loadImage

class UserAutoCompleteAdapter(
    context: Context, resource: Int, val listener: OnAutoCompleteItemClickListener
) : ArrayAdapter<Users>(context, resource), Filterable {
    private val userList: MutableList<Users>

    init {
        userList = ArrayList()
    }

    fun setData(list: ArrayList<Users>) {
        userList.clear()
        userList.addAll(list)
    }

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): Users {
        return userList[position]
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val binding: RowUserDropDownBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_user_drop_down, parent, false
            )
            convertView = binding.root
        } else {
            binding = convertView.tag as RowUserDropDownBinding
        }
        val user = getItem(position)
        binding.user = user
        convertView.tag = binding
        binding.ivUser.loadImage(user.name, user.profilePic)

        binding.root.setOnClickListener {
            listener.onItemClick(getItem(position))
        }
        binding.divider.isVisible = (position == userList.size - 1).not()
        return convertView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = userList
                    filterResults.count = userList.size
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