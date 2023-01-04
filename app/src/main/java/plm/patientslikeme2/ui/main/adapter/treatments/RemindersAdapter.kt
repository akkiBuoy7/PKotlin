package plm.patientslikeme2.ui.main.adapter.treatments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.R
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.treatments.ReminderNotificationsAttributesItem
import plm.patientslikeme2.databinding.RowReminderTimeBinding
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.AM
import plm.patientslikeme2.utils.Constants.PM
import plm.patientslikeme2.utils.extensions.formatTime
import plm.patientslikeme2.utils.extensions.to2DString
import plm.patientslikeme2.utils.usercontrol.SwitchTrackTextDrawable
import java.util.*


class RemindersAdapter(private val context: Context?) : ListAdapter<ReminderNotificationsAttributesItem, RemindersAdapter.MyViewHolder>(
    ComparatorDiffUtil()
) {

    var onItemReminderClicked: ((Boolean, String, Int, String, String) -> Unit)? = null
    var isEdit: Boolean = false
    var selectedTime: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowReminderTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = getItem(position)
        model?.let {
            holder.bind(model)
        }

    }

    fun addItemInList(model: ReminderNotificationsAttributesItem) {
        val list: MutableList<ReminderNotificationsAttributesItem> = mutableListOf()
        list.addAll(currentList)
        list.add(model)
        submitList(list)
    }

    inner class MyViewHolder(private val binding: RowReminderTimeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.sAppointmnetReminder.setOnClickListener {
                val ampm = if (binding.switchAMPM.isChecked) {
                    AM
                } else {
                    PM
                }
                onItemReminderClicked?.invoke(
                    binding.sAppointmnetReminder.isChecked, Constants.NOTIFICATION, adapterPosition, binding.edtTiming.text.toString(), ampm
                )
            }
            binding.switchAMPM.setOnClickListener {
                val ampm = if (binding.switchAMPM.isChecked) {
                    PM
                } else {
                    AM
                }
                onItemReminderClicked?.invoke(
                    binding.sAppointmnetReminder.isChecked, Constants.AMPM, adapterPosition, binding.edtTiming.text.toString(), ampm
                )
            }
        }

        fun bind(model: ReminderNotificationsAttributesItem) {
            binding.model = model
            if (isEdit) {
                val (hourDefault, minuteDefault) = formatTime(model.notifyTime)
//                binding.switchAMPM.isChecked = hourDefault > 12
//                val hour = if (hourDefault > 12) {
//                    hourDefault - 12
//                } else {
//                    hourDefault
//                }
                var hour = 0
                var hourString = model.notifyTime?.split(":")?.get(0)
                if (hourDefault > 12) {
                    hour = hourDefault - 12
                    binding.switchAMPM.isChecked = true
                } else {
                    if (hourDefault == 0) {
                        binding.switchAMPM.isChecked = hourString == "12"
                        hour = 12
                    } else {
                        hour = hourDefault
                        binding.switchAMPM.isChecked = false
                    }
                }

                binding.edtTiming.text = "${hour.to2DString()}:${minuteDefault.to2DString()}"
                binding.sAppointmnetReminder.isChecked = model.isNotify == true
                selectedTime = "${hourDefault.to2DString()}:${minuteDefault.to2DString()}"
                val ampm = if (binding.switchAMPM.isChecked) {
                    PM
                } else {
                    AM
                }
                if (currentList.size > 0) {
                    onItemReminderClicked?.invoke(
                        binding.sAppointmnetReminder.isChecked, Constants.NOTIFICATION, adapterPosition, binding.edtTiming.text.toString(), ampm
                    )
                }
            } else {
                val mCurrentTimeDefault = Calendar.getInstance()
                val hourDefault = mCurrentTimeDefault[Calendar.HOUR_OF_DAY]
                val minuteDefault = mCurrentTimeDefault[Calendar.MINUTE]
                if (mCurrentTimeDefault.get(Calendar.AM_PM) == Calendar.AM) binding.switchAMPM.isChecked = false
                else if (mCurrentTimeDefault.get(Calendar.AM_PM) == Calendar.PM) binding.switchAMPM.isChecked = true
                val hour = if (hourDefault > 12) {
                    hourDefault - 12
                } else {
                    hourDefault
                }
                binding.edtTiming.text = "${hour.to2DString()}:${minuteDefault.to2DString()}"
                selectedTime = "${hourDefault.to2DString()}:${minuteDefault.to2DString()}"
                val ampm = if (binding.switchAMPM.isChecked) {
                    PM
                } else {
                    AM
                }
                onItemReminderClicked?.invoke(
                    binding.sAppointmnetReminder.isChecked, Constants.NOTIFICATION, adapterPosition, binding.edtTiming.text.toString(), ampm
                )
            }

            binding.edtTiming.setOnClickListener {
                val mCurrentTime = Calendar.getInstance()
                val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mCurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                TimePickerDialog(
                    context as AppCompatActivity, { _, selectedHour, selectedMinute ->
                        var hour = 0
                        if (selectedHour > 12) {
                            hour = selectedHour - 12
                            binding.switchAMPM.isChecked = true
                        } else {
                            if (selectedHour == 0) {
                                hour = 12
                            } else {
                                hour = selectedHour
                            }
                            if (selectedHour==12){
                                binding.switchAMPM.isChecked = true
                            }else{
                                binding.switchAMPM.isChecked = false
                            }
                        }
                        binding.edtTiming.text = "${hour.to2DString()}:${selectedMinute.to2DString()}"
                        selectedTime = "${selectedHour.to2DString()}:${selectedMinute.to2DString()}"
                        val ampm = if (binding.switchAMPM.isChecked) {
                            PM
                        } else {
                            AM
                        }
                        onItemReminderClicked?.invoke(
                            binding.sAppointmnetReminder.isChecked, Constants.NOTIFICATION, adapterPosition, binding.edtTiming.text.toString(), ampm
                        )
                    }, hour, minute, false
                ).also { mTimePicker = it } //Yes 24 hour time

                mTimePicker.setTitle(R.string.select_time)
                mTimePicker.show()
                mTimePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)?.setTextColor(MyApplication.appContext.getColor(R.color.calendar))
                mTimePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)?.setTextColor(MyApplication.appContext.getColor(R.color.calendar))

            }

            binding.switchAMPM.trackDrawable = SwitchTrackTextDrawable(MyApplication.appContext, R.string.am, R.string.pm)
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<ReminderNotificationsAttributesItem>() {
        override fun areItemsTheSame(
            oldItem: ReminderNotificationsAttributesItem, newItem: ReminderNotificationsAttributesItem
        ): Boolean {
            return oldItem.reminder_id == newItem.reminder_id
        }

        override fun areContentsTheSame(
            oldItem: ReminderNotificationsAttributesItem, newItem: ReminderNotificationsAttributesItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}