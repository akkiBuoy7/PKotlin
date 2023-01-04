package plm.patientslikeme2.ui.main.adapter.treatments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.treatments.Other
import plm.patientslikeme2.data.model.treatments.TreatmentHistoryData
import plm.patientslikeme2.utils.Constants.OTHER_TREATMENTS
import plm.patientslikeme2.utils.usercontrol.UserTextView
import java.util.*

class TreatmentsParentAdapter(
    listData: TreeMap<String, Other>?,
    listener: RemoveTreatmentListener,
) : RecyclerView.Adapter<TreatmentsParentAdapter.ViewHolder>(),
    TreatmentsChildAdpter.RemoveTreatmentListener {

    private var removeTreatmentListener: RemoveTreatmentListener? = null
    var mData = TreeMap<String, Other>()
    var mKeys: MutableSet<String>
    lateinit var myTreatmentCAdapter: TreatmentsChildAdpter

    init {
        this.removeTreatmentListener = listener
        mData = listData!!
        mKeys = mData.keys
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.row_my_treatment_detail, parent, false)
        return ViewHolder(listItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val conditionId: Int?
        if (mData.get(mKeys.elementAt(position))?.conditionName == null) {
            holder.tvPurpose.text = OTHER_TREATMENTS
            conditionId = -1
        } else {
            holder.tvPurpose.text = mData[mKeys.elementAt(position)]?.conditionName?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
            conditionId = mData.get(mKeys.elementAt(position))?.conditionId
        }

        myTreatmentCAdapter = TreatmentsChildAdpter(
            conditionId,
            mData.get(mKeys.elementAt(position))?.treatmentHistories,
            this, position
        )
        holder.rvTreatmentList.adapter = myTreatmentCAdapter
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPurpose = itemView.findViewById<View>(R.id.tv_purpose) as UserTextView
        var rvTreatmentList = itemView.findViewById<View>(R.id.rv_treatment) as RecyclerView
    }

    interface RemoveTreatmentListener {
        fun onRemoveTreatment(id: String, position: Int, name: String, parentPosition: Int)
        fun OnEditTreatment(
            conditionId: Int?,
            treatmentName: String, treatmentId: Int, position: Int,
        )
    }

    override fun onRemoveTreatment(id: String, position: Int, name: String, parentPosition: Int) {
        removeTreatmentListener?.onRemoveTreatment(id, position, name, parentPosition)
    }

    fun delete(position: Int,childPosition:Int) {
        mData.get(mKeys.elementAt(
            position
            )
        )?.treatmentHistories?.removeAt(childPosition)
        notifyDataSetChanged()
    }

    fun add(position: Int,childPosition:Int,model: TreatmentHistoryData) {
        mData.get(mKeys.elementAt(
            position
        )
        )?.treatmentHistories?.add(childPosition,model)
        notifyDataSetChanged()
    }

    override fun OnEditTreatment(
        conditionId: Int?,
        treatmentName: String,
        treatmentId: Int,
        position: Int,
        parentPosition: Int
    ) {
        removeTreatmentListener?.OnEditTreatment(conditionId, treatmentName, treatmentId, position)
    }
}