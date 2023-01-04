package plm.patientslikeme2.ui.main.adapter.treatments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.treatments.TreatmentHistoryData
import plm.patientslikeme2.utils.usercontrol.UserTextView

class TreatmentsChildAdpter(
    private var conditionId: Int?,
    private val listData: ArrayList<TreatmentHistoryData>?,
    listener: RemoveTreatmentListener,
    var parentPosition: Int
) : RecyclerView.Adapter<TreatmentsChildAdpter.ViewHolder>() {

    private var removeTreatmentListener: RemoveTreatmentListener? = null

    init {
        this.removeTreatmentListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.row_treatment, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imgRemoveTreatments.setOnClickListener {
            removeTreatmentListener?.onRemoveTreatment(
                listData?.get(position)?.id!!.toString(),
                position,
                listData.get(position).name!!.toString(),
                parentPosition
            )
        }

        holder.imgEditTreatments.setOnClickListener {
            listData?.get(position)?.name.let { name ->
                if (name != null) {
                    listData?.get(position)?.id.let { id ->
                        if (id != null) {
                            removeTreatmentListener?.OnEditTreatment(
                                conditionId, name, id, position, parentPosition
                            )
                        }
                    }
                }
            }
        }

        val data: String = if (listData?.get(position)?.latestDosageString != null) {
            listData[position].name + "," + listData[position].latestDosageString
        } else {
            listData?.get(position)?.name + " " + " "
        }


        holder.tvPurpose.text = data

        if (position == itemCount - 1) {
            holder.ivDottedLine.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        if (listData != null) {
            return listData.size
        }
        return 0
    }

    fun delete(position: Int) {
        listData?.removeAt(position)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPurpose = itemView.findViewById<View>(R.id.tv_title) as UserTextView
        var imgRemoveTreatments = itemView.findViewById<View>(R.id.iv_delete) as ImageView
        var imgEditTreatments = itemView.findViewById<View>(R.id.iv_edit) as ImageView
        var ivDottedLine = itemView.findViewById<View>(R.id.view_divider) as View
    }

    interface RemoveTreatmentListener {
        fun onRemoveTreatment(id: String, position: Int, name: String, parentPosition: Int)
        fun OnEditTreatment(
            conditionId: Int?,
            treatmentName: String,
            treatmentId: Int,
            position: Int,
            parentPosition: Int
        )
    }
}