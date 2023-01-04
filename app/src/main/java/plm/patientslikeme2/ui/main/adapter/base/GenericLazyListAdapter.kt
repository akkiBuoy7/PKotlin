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

class GenericLazyListAdapter<T : Any, VM : ViewDataBinding>(
    @LayoutRes val layoutID: Int,
    private val callBack: GenericLazyBindingInterface<VM, T>
) :
    ListAdapter<T, GenericLazyListAdapter.GenericLazyViewHolder>(
        GenericLazyDiffUtilCallback()
    ) {
    var selectionTracker: SelectionTracker<Long>? = null
    lateinit var binding: ViewDataBinding
    var onItemClicked: ((T, String, Int) -> Unit)? = null
    var pageNo = 0

    class GenericLazyViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun <T : Any, VM : ViewDataBinding> bind(
            item: T,
            bindingInterface: GenericLazyBindingInterface<VM, T>,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericLazyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, layoutID, parent, false)
        return GenericLazyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericLazyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
            callBack,
            position,
            selectionTracker?.isSelected(position.toLong()) ?: false
        )
    }
}

class GenericLazyDiffUtilCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.toString() == newItem.toString()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

interface GenericLazyBindingInterface<VM : ViewDataBinding, T : Any> {
    fun bindData(binder: VM, model: T, position: Int)
}

