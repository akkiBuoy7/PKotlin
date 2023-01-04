package plm.patientslikeme2.ui.main.adapter.messages

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.data.model.home.NavMenuModel.Companion.HEADER
import plm.patientslikeme2.data.model.messages.ThreadMessages
import plm.patientslikeme2.databinding.RowMessageReceiverBinding
import plm.patientslikeme2.databinding.RowMessageSenderBinding
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.loadImage
import plm.patientslikeme2.utils.extensions.setContentToWebView

class MessagesConversationAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messagesList: ArrayList<ThreadMessages> = ArrayList()

    var onItemClicked: ((ThreadMessages, String) -> Unit)? = null

    private inner class MyMessageView(private val binding: RowMessageSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val model = messagesList[position]
            binding.model = model
            model.body_html?.let { binding.tvMessage.setContentToWebView(it) }
            binding.ivUser.loadImage(model.sender?.login, model.sender?.avatar_url)
        }
    }

    private inner class OtherMessageView(private val binding: RowMessageReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val model = messagesList[position]
            binding.model = model
            model.body_html?.let { binding.tvMessage.setContentToWebView(it) }
            binding.ivUser.loadImage(model.sender?.login, model.sender?.avatar_url)
            binding.ivMore.setOnClickListener {
                onItemClicked?.invoke(model, Constants.REPORT_AS_SPAM)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> {
                MyMessageView(
                    RowMessageSenderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> {
                OtherMessageView(
                    RowMessageReceiverBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messagesList[position].is_self_user_message == true) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (messagesList[position].is_self_user_message) {
            true -> (holder as MyMessageView).bind(position)
            else -> (holder as OtherMessageView).bind(position)
        }
    }

    fun setData(newData: ArrayList<ThreadMessages>) {
        messagesList.addAll(0, newData)
        this.notifyItemRangeInserted(0, newData.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        messagesList.clear()
        this.notifyDataSetChanged()
    }
}