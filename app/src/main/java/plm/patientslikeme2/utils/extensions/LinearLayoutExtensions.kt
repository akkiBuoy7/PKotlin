package plm.patientslikeme2.utils.extensions

import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import plm.patientslikeme2.data.model.community.groups.groupOverview.GroupOverviewResponse

@BindingAdapter("currentIndex", "groupOverviewResponse")
fun LinearLayout.topContributors(currentIndex: Int, groupOverviewResponse: GroupOverviewResponse) {
    val topContributorsSize =
        groupOverviewResponse.data?.group?.highlights?.topContributors?.size ?: 0
    if (topContributorsSize > currentIndex) this.visibility = View.VISIBLE else this.visibility =
        View.INVISIBLE
}