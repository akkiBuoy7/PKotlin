package plm.patientslikeme2.ui.main.adapter.notification

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.data.model.home.NotificationImages
import plm.patientslikeme2.data.model.home.NotificationModel
import plm.patientslikeme2.databinding.RowNotificationBinding
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.callback.CallBackListener
import plm.patientslikeme2.utils.enum.ContextType
import plm.patientslikeme2.utils.enum.NotificationImage
import plm.patientslikeme2.utils.extensions.loadIconImage
import plm.patientslikeme2.utils.extensions.loadImage
import plm.patientslikeme2.utils.extensions.setHtmlText

class NotificationAdapter(
    val itemList: ArrayList<NotificationModel>,
    private val callback: CallBackListener<NotificationModel>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var pageNo = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            RowNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationHolder(binding)
    }

    fun addItemList(list: ArrayList<NotificationModel>?) {
        list?.let {
            val offset = itemList.size
            if (pageNo == 1) {
                itemList.clear()
                itemList.addAll(list)
            } else {
                itemList.addAll(list)
            }
            notifyItemRangeInserted(offset, list.size)
        }
    }

    fun updateItem(model: NotificationModel) {
        itemList[itemList.indexOf(model)] = model
        notifyDataSetChanged()
    }

    fun removeItem(model: NotificationModel?) {
        model?.let {
            itemList.removeAt(itemList.indexOf(it))
            notifyItemRangeChanged(itemList.indexOf(it), itemList.size)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        if (holder is NotificationHolder) {
            holder.bind(item)
        }
    }

    internal inner class NotificationHolder(private val binding: RowNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: NotificationModel) {
            binding.model = model
            when (model.context_type) {
                ContextType.Post.name,
                ContextType.Topic.name,
                -> {
                    if (TextUtils.isEmpty(model.title)) {
                        binding.tvDescription.text = model.body
                    } else {
                        binding.tvDescription.setHtmlText("${model.body} <b>${model.title}</b>")
                    }
                    binding.tvTitle.visibility = View.GONE
                    binding.tvDescription.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvTitle.text = model.title
                    binding.tvDescription.text = model.body
                    if (model.title.isEmpty()) {
                        binding.tvTitle.visibility = View.GONE
                    } else {
                        binding.tvTitle.visibility = View.VISIBLE
                    }
                    if (model.body.isEmpty()) {
                        binding.tvDescription.visibility = View.GONE
                    } else {
                        binding.tvDescription.visibility = View.VISIBLE
                    }
                }
            }
            if (model.seen) {
                binding.ivSeen.visibility = View.GONE
            } else {
                binding.ivSeen.visibility = View.VISIBLE
            }

            when (model.images.size) {
                1 -> {
                    binding.flImage.visibility = View.VISIBLE
                    binding.flImageMOne.visibility = View.GONE
                    binding.flImageMTwo.visibility = View.GONE
                    binding.flImageMThree.visibility = View.GONE
                    val imageModel = model.images[0]
                    when (imageModel.type) {
                        NotificationImage.icon.toString() -> {
                            binding.ivUserIcon.visibility = View.VISIBLE
                            binding.ivUser.visibility = View.GONE
                            binding.tvShortName.visibility = View.GONE
                            binding.ivUserIcon.loadIconImage(imageModel.value)
                        }
                        NotificationImage.avatar_url.toString() -> {
                            binding.ivUserIcon.visibility = View.GONE
                            binding.ivUser.visibility = View.VISIBLE
                            binding.tvShortName.visibility = View.GONE
                            binding.ivUser.loadImage(imageModel.value)
                        }
                        else -> {
                            binding.ivUserIcon.visibility = View.GONE
                            binding.ivUser.visibility = View.VISIBLE
                            binding.tvShortName.visibility = View.VISIBLE
                            binding.tvShortName.text = imageModel.value
                        }
                    }
                }
                2 -> {
                    binding.flImage.visibility = View.GONE
                    binding.flImageMOne.visibility = View.VISIBLE
                    binding.flImageMTwo.visibility = View.VISIBLE
                    binding.flImageMThree.visibility = View.GONE

                    val imageModel1 = model.images[0]
                    updateFirstImage(binding, imageModel1)

                    val imageModel2 = model.images[1]
                    updateSecondImage(binding, imageModel2)
                }
                else -> {
                    binding.flImage.visibility = View.GONE
                    binding.flImageMOne.visibility = View.VISIBLE
                    binding.flImageMTwo.visibility = View.VISIBLE
                    binding.flImageMThree.visibility = View.VISIBLE

                    val imageModel1 = model.images[0]
                    updateFirstImage(binding, imageModel1)

                    val imageModel2 = model.images[1]
                    updateSecondImage(binding, imageModel2)

                    val imageModel3 = model.images[2]
                    updateThreeImage(binding, imageModel3)
                }
            }
            binding.rlTop.setOnClickListener {
                callback.onClick(model, Constants.OPEN)
            }
            binding.tvDescription.setOnClickListener {
                callback.onClick(model, Constants.OPEN)
            }

            if (adapterPosition == itemList.size - 1) {
                binding.viewDivider.isVisible = false
            }
            binding.executePendingBindings()
        }
    }

    private fun updateFirstImage(binder: RowNotificationBinding, image: NotificationImages) {
        when (image.type) {
            NotificationImage.avatar_url.toString() -> {
                binder.ivUserMOne.loadImage(image.value)
                binder.ivUserMOne.visibility = View.VISIBLE
                binder.tvShortNameMOne.visibility = View.GONE
            }
            else -> {
                binder.ivUserMOne.visibility = View.VISIBLE
                binder.tvShortNameMOne.visibility = View.VISIBLE
                binder.tvShortNameMOne.text = image.value
            }
        }
    }

    private fun updateSecondImage(binder: RowNotificationBinding, image: NotificationImages) {
        when (image.type) {
            NotificationImage.avatar_url.toString() -> {
                binder.ivUserMTwo.loadImage(image.value)
                binder.ivUserMTwo.visibility = View.VISIBLE
                binder.tvShortNameMTwo.visibility = View.GONE
            }
            else -> {
                binder.ivUserMTwo.visibility = View.VISIBLE
                binder.tvShortNameMTwo.visibility = View.VISIBLE
                binder.tvShortNameMTwo.text = image.value
            }
        }
    }

    private fun updateThreeImage(binder: RowNotificationBinding, image: NotificationImages) {
        when (image.type) {
            NotificationImage.avatar_url.toString() -> {
                binder.ivUserMThree.loadImage(image.value)
                binder.ivUserMThree.visibility = View.VISIBLE
                binder.tvShortNameMThree.visibility = View.GONE
            }
            else -> {
                binder.ivUserMThree.visibility = View.VISIBLE
                binder.tvShortNameMThree.visibility = View.VISIBLE
                binder.tvShortNameMThree.text = image.value
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}