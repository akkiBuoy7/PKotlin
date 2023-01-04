package plm.patientslikeme2.data.model.treatments

import com.google.gson.annotations.SerializedName

data class AddTreatmentRequest(

    @field:SerializedName("treatment_history")
    var treatmentHistory: TreatmentHistory? = null
)

data class ReminderNotificationsAttributesItem(

    @field:SerializedName("is_notify")
    var isNotify: Boolean? = null,

    @field:SerializedName("notify_time")
    var notifyTime: String? = null,

    @field:SerializedName("id")
    var id: Int = 0,

    @field:SerializedName("reminder_id")
    var reminder_id: Int = 0
)

data class DoseAttributes(

    @field:SerializedName("as_needed")
    var asNeeded: Boolean? = null,

    @field:SerializedName("quantity")
    var quantity: String? = null,

    @field:SerializedName("structured_dosage_id")
    var structuredDosageId: String? = null,

    @field:SerializedName("use_other_dosage")
    var useOtherDosage: String? = null,

    @field:SerializedName("strength_num_unit_id")
    var strengthNumUnitId: String? = null,

    @field:SerializedName("strength_num_amount")
    var strengthNumAmount: String? = null
)

data class TreatmentHistory(

    @field:SerializedName("condition_info_id")
    var conditionInfoId: Int? = null,

    @field:SerializedName("purposes")
    var purposes: ArrayList<AddPurposesItem>? = null,

    @field:SerializedName("treatment_dosage_histories_attributes")
    var treatmentDosageHistoriesAttributes: ArrayList<TreatmentDosageHistoriesAttributesItem>? = null,

    @field:SerializedName("reminder_notifications_attributes")
    var reminderNotificationsAttributes: ArrayList<ReminderNotificationsAttributesItem>? = null,

    @field:SerializedName("treatment_id")
    var treatmentId: Int? = null,

    @field:SerializedName("privacy")
    var privacy: String? = null
)

data class AddPurposesItem(

    @field:SerializedName("condition_id")
    val conditionId: Int? = null,

    @field:SerializedName("attribution_id")
    val attributionId: Int? = null,

    @field:SerializedName("symptom_id")
    val symptomId: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("type")
    val type: String? = null
)

data class DateHash(

    @field:SerializedName("month")
    var month: String? = null,

    @field:SerializedName("year")
    var year: String? = null,

    @field:SerializedName("day")
    var day: String? = null
)

data class TreatmentDosageHistoriesAttributesItem(

    @field:SerializedName("drugdb_frequency_unit_id")
    var drugdbFrequencyUnitId: String? = null,

    @field:SerializedName("dose_attributes")
    var doseAttributes: DoseAttributes? = null,

    @field:SerializedName("date_hash")
    var dateHash: DateHash? = null
)
