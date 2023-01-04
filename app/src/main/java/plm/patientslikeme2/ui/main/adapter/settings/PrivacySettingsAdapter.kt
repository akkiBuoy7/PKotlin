package plm.patientslikeme2.ui.main.adapter.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.data.model.settings.OwnControlled
import plm.patientslikeme2.databinding.RowPrivacySettingsBinding
import plm.patientslikeme2.utils.extensions.getUpperFirstChar

class PrivacySettingsAdapter :
    ListAdapter<OwnControlled, PrivacySettingsAdapter.MyViewHolder>(ComparatorDiffUtil()) {

    var onItemClicked: ((OwnControlled, String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RowPrivacySettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = getItem(position)
        model?.let {
            holder.bind(model)
        }
    }

    inner class MyViewHolder(private val binding: RowPrivacySettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(model: OwnControlled) {

            val items: ArrayList<String> = arrayListOf(
                "Share with the community", "Personal"
            )
            binding.spinnerOptions.adapter = ArrayAdapter(
                binding.spinnerOptions.context, android.R.layout.simple_list_item_1, items
            )

            val selectionPosition =
                if (model.permissionLevel.equals("plm", true)) {
                    0
                } else {
                    1
                }
            binding.spinnerOptions.setSelection(selectionPosition)
            binding.spinnerOptions.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>, view: View?, position: Int, id: Long
                    ) {
                        val permissionLevel: String = if (position == 0) "plm" else "personal"
                        onItemClicked?.invoke(getItem(adapterPosition), permissionLevel)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            binding.tvConditions.getUpperFirstChar(model.controllableName.toString())
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<OwnControlled>() {
        override fun areItemsTheSame(
            oldItem: OwnControlled, newItem: OwnControlled
        ): Boolean {
            return oldItem.controllableId == newItem.controllableId
        }

        override fun areContentsTheSame(
            oldItem: OwnControlled, newItem: OwnControlled
        ): Boolean {
            return oldItem == newItem
        }
    }
}