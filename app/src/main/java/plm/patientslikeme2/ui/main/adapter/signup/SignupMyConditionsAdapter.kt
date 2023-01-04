package plm.patientslikeme2.ui.main.adapter.signup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.data.model.signup.Conditions
import plm.patientslikeme2.databinding.RowSignupMyConditionsBinding
import plm.patientslikeme2.utils.Constants.DELETE
import plm.patientslikeme2.utils.Constants.EDIT
import plm.patientslikeme2.utils.Constants.PERSONAL
import plm.patientslikeme2.utils.Constants.SHARE_WITH_COMMUNITY
import java.util.*

class SignupMyConditionsAdapter :
    ListAdapter<Conditions, SignupMyConditionsAdapter.MyViewHolder>(ComparatorDiffUtil()) {

    var onItemClicked: ((Conditions, Int, String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RowSignupMyConditionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    fun addItemInList(model: Conditions) {
        val list: MutableList<Conditions> = mutableListOf()
        list.add(model)
        list.addAll(currentList)
        submitList(list)
    }

    fun updateItem(model: Conditions, position: Int) {
        val currentList = currentList.toMutableList()
        currentList[position] = model
        submitList(currentList)
    }

    fun removeItem(model: Conditions) {
        val position = currentList.indexOf(model)
        val currentList = currentList.toMutableList()
        currentList.removeAt(position)
        submitList(currentList)
    }

    inner class MyViewHolder(private val binding: RowSignupMyConditionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Conditions) {
            binding.tvTitle.text = item.name?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ENGLISH
                ) else it.toString()
            }
            if (item.stage_name.isNullOrEmpty()) {
                binding.tvStage.visibility = View.GONE
            } else {
                binding.tvStage.text = item.stage_name
                binding.tvStage.visibility = View.VISIBLE
            }

            if (item.privacy == "plm") {
                binding.tvPrivacy.text = SHARE_WITH_COMMUNITY
            } else if (item.privacy == "personal") {
                binding.tvPrivacy.text = PERSONAL
            }

            if (currentList.size - 1 == adapterPosition) {
                binding.divider.visibility = View.GONE
            } else {
                binding.divider.visibility = View.VISIBLE
            }
            binding.ivEdit.setOnClickListener {
                onItemClicked?.invoke(getItem(adapterPosition), adapterPosition, EDIT)
            }

            binding.ivDelete.setOnClickListener {
                onItemClicked?.invoke(getItem(adapterPosition), adapterPosition, DELETE)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Conditions>() {
        override fun areItemsTheSame(
            oldItem: Conditions,
            newItem: Conditions
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Conditions,
            newItem: Conditions
        ): Boolean {
            return oldItem == newItem
        }
    }
}