package plm.patientslikeme2.ui.main.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.data.model.home.NavMenuModel
import plm.patientslikeme2.data.model.home.NavMenuModel.Companion.EMPTY
import plm.patientslikeme2.data.model.home.NavMenuModel.Companion.HEADER
import plm.patientslikeme2.data.model.home.NavMenuModel.Companion.LOGOUT
import plm.patientslikeme2.data.model.home.NavMenuModel.Companion.SUB_HEADER
import plm.patientslikeme2.databinding.RowNavMenuEmptyBinding
import plm.patientslikeme2.databinding.RowNavMenuHeaderBinding
import plm.patientslikeme2.databinding.RowNavMenuLogoutBinding
import plm.patientslikeme2.databinding.RowNavMenuSubHeaderBinding

class DrawerNavMenuAdapter(var itemList: ArrayList<NavMenuModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClicked: ((NavMenuModel) -> Unit)? = null

    private inner class NavHeaderHolder(private val binding: RowNavMenuHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = itemList[position]
            binding.tvTitle.text = itemView.context.getString(item.title)
            binding.ivIcon.setImageResource(item.icon)
            itemView.setOnClickListener {
                onItemClicked?.invoke(item)
            }
        }
    }

    private inner class NavSubHeaderHolder(private val binding: RowNavMenuSubHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = itemList[position]
            binding.tvTitle.text = itemView.context.getString(item.title)
            itemView.setOnClickListener {
                onItemClicked?.invoke(item)
            }
        }
    }

    private inner class NavEmptyHolder(binding: RowNavMenuEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class NavLogoutHolder(private val binding: RowNavMenuLogoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = itemList[position]
            binding.btnLogout.setOnClickListener {
                onItemClicked?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> {
                NavHeaderHolder(
                    RowNavMenuHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            EMPTY -> {
                NavEmptyHolder(
                    RowNavMenuEmptyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            SUB_HEADER -> {
                NavSubHeaderHolder(
                    RowNavMenuSubHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                NavLogoutHolder(
                    RowNavMenuLogoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (itemList[position].type) {
            HEADER -> (holder as NavHeaderHolder).bind(position)
            SUB_HEADER -> (holder as NavSubHeaderHolder).bind(position)
            EMPTY -> (holder as NavEmptyHolder)
            LOGOUT -> (holder as NavLogoutHolder).bind(position)
        }
    }
}