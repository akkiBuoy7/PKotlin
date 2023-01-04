package plm.patientslikeme2.ui.main.adapter.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class GenericListAdapter<T : Any, VM : ViewDataBinding>(
    @LayoutRes val layoutID: Int,
    private val bindingInterface: GenericRecyclerBindingInterface<VM, T>,
) :
    ListAdapter<T, GenericListAdapter.GenericRecyclerViewHolder>(
        GenericDiffUtilCallback()
    ) {
    var selectionTracker: SelectionTracker<Long>? = null
    lateinit var  binding : ViewDataBinding

    class GenericRecyclerViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun <T : Any, VM : ViewDataBinding> bind(
            item: T,
            bindingInterface: GenericRecyclerBindingInterface<VM, T>,
            position: Int,
            b: Boolean
        ) {
            bindingInterface.bindData(binding as VM, item, position)
            itemView.isActivated = b
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            layoutID,
            parent,
            false
        )
        return GenericRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericRecyclerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
            bindingInterface,
            position,
            selectionTracker?.isSelected(position.toLong()) ?: false
        )
    }
}

class GenericDiffUtilCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.toString() == newItem.toString()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

interface GenericRecyclerBindingInterface<VM : ViewDataBinding, T : Any> {
    fun bindData(binder: VM, model: T, position: Int)
}

