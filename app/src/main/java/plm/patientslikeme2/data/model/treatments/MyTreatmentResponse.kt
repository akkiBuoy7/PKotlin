package plm.patientslikeme2.data.model.treatments

import com.google.gson.annotations.SerializedName
import java.util.*

class MyTreatmentsResponse {
    @SerializedName("success")
    val success: Boolean? = null

    @SerializedName("data")
    val data: Data? = null
}

class Data {
    @SerializedName("treatments")
    val treatments: TreeMap<String, Other>? = null
}

class Other {
    @SerializedName("condition_id")
    var conditionId: Int? = null

    @SerializedName("condition_name")
    val conditionName: String? = null

    @SerializedName("treatment_histories")
    val treatmentHistories: ArrayList<TreatmentHistoryData>? = null
}

class TreatmentHistoryData {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("latest_dosage_string")
    var latestDosageString: String? = null

    @SerializedName("privacy")
    var privacy: String? = null
}

data class SearchTreatmentResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: SearchTreatmentData? = null,

    )

data class SearchTreatmentData(
    @field:SerializedName("treatment")
    val treatment: ArrayList<SearchTreatmentItem>? = null
)

data class SearchTreatmentItem(

    @field:SerializedName("treatment_category_id")
    val treatmentCategoryId: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
) {
    override fun toString(): String {
        return name.toString()
    }
}


data class PurposeSearchResponse(

    @field:SerializedName("data")
    val data: PurposeSearchData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: Any? = null
)

data class PurposeSearchData(

    @field:SerializedName("results")
    val results: ArrayList<ResultsItem>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("type")
    val type: String? = null
) {
    override fun toString(): String {
        return name.toString()
    }
}

data class ResultsItem(

    @field:SerializedName("data")
    val data: PurposeSearchData? = null,

    @field:SerializedName("name")
    val name: String? = null
) {
    override fun toString(): String {
        return name.toString()
    }
}

data class GetTreatmentResponse(

    @field:SerializedName("data")
    val data: GetTreatmentData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: Any? = null
)

data class GetTreatmentDosageHistoryItem(

    @field:SerializedName("doses")
    val doses: List<GetDosesItem>? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("start_date")
    val startDate: String? = null,

    @field:SerializedName("drugdb_frequency_unit")
    val drugdbFrequencyUnit: DrugdbFrequencyUnit? = null
)

data class GetTreatmentHistory(

    @field:SerializedName("treatment_dosage_history")
    val treatmentDosageHistory: List<GetTreatmentDosageHistoryItem>? = null,

    @field:SerializedName("purposes")
    val purposes: List<GetPurposesItem>? = null,

    @field:SerializedName("treatment_category")
    val treatmentCategory: TreatmentCategory? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("reminder_notifications_attributes")
    val reminderNotificationsAttributes: ArrayList<ReminderNotificationsAttributesItem>? = null,

    @field:SerializedName("treatment_id")
    val treatmentId: Int? = null,

    @field:SerializedName("privacy")
    val privacy: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)

data class GetDosesItem(

    @field:SerializedName("strength_num_unit")
    val strengthNumUnit: StrengthNumUnit? = null,

    @field:SerializedName("as_needed")
    val asNeeded: Boolean? = null,

    @field:SerializedName("use_other_dosage")
    val useOtherDosage: String? = null,

    @field:SerializedName("strength_num_amount")
    val strengthNumAmount: String? = null,

    @field:SerializedName("quantity")
    val quantity: String? = null,

    @field:SerializedName("strength_num_unit_id")
    val strengthNumUnitId: Int? = null,

    @field:SerializedName("structured_dosage")
    val structuredDosage: StructuredDosage? = null
)

data class StructuredDosage(

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)

data class DrugdbFrequencyUnit(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("display_name")
    val displayName: String? = null
)

data class GetTreatmentData(

    @field:SerializedName("treatment_history")
    val treatmentHistory: GetTreatmentHistory? = null
)

data class GetPurposesItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("attribution_id")
    val attributionId: Int? = null,

    @field:SerializedName("type")
    val type: String? = null
)

data class StrengthNumUnit(

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)