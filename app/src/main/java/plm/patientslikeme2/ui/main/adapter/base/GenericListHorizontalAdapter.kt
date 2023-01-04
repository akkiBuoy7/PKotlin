package plm.patientslikeme2.ui.main.adapter.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class GenericListHorizontalAdapter<T : Any, VM : ViewDataBinding>(
    @LayoutRes val layoutID: Int,
    private val bindingInterface: GenericHorizontalRecyclerBindingInterface<VM, T>,
    private val rootWidthPixels: Float? = null,
) :
    ListAdapter<T, GenericListHorizontalAdapter.GenericHorizontalRecyclerViewHolder>(
        GenericDiffUtilCallback()
    ) {
    var selectionTracker: SelectionTracker<Long>? = null
    lateinit var binding: ViewDataBinding
    var onItemClicked: ((T, String, Int) -> Unit)? = null

    class GenericHorizontalRecyclerViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun <T : Any, VM : ViewDataBinding> bind(
            item: T,
            bindingInterface: GenericHorizontalRecyclerBindingInterface<VM, T>,
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericHorizontalRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            layoutID,
            parent,
            false
        )
        if (currentList.size > 1 && rootWidthPixels != null) {
            binding.root.layoutParams.width = rootWidthPixels.toInt()
        }
        return GenericHorizontalRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericHorizontalRecyclerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
            bindingInterface,
            position,
            selectionTracker?.isSelected(position.toLong()) ?: false
        )
    }
}


interface GenericHorizontalRecyclerBindingInterface<VM : ViewDataBinding, T : Any> {
    fun bindData(binder: VM, model: T, position: Int)
}
