package plm.patientslikeme2.data.model.treatments

import com.google.gson.annotations.SerializedName

data class MyFrequencyDosageResponse(

    @field:SerializedName("data") val data: MyDosageData? = null,

    @field:SerializedName("success") val success: Boolean? = null
)

data class MyDosageData(
    @field:SerializedName("treatment_categories") val treatmentCategories: MyFrequencyDosageData? = null
)

data class FrequencyUnitsItem(

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("display_name") val displayName: String? = null
) {
    override fun toString(): String {
        return displayName.toString()
    }
}

data class DosageUnitsItem(

    @field:SerializedName("description") val description: String? = null,

    @field:SerializedName("id") val id: String? = null
) {
    override fun toString(): String {
        return description.toString()
    }
}

data class DefaultDosageUnitsItem(

    @field:SerializedName("description") val description: String? = null,

    @field:SerializedName("id") val id: String? = null
) {
    override fun toString(): String {
        return description.toString()
    }
}

data class MyFrequencyDosageData(

    @field:SerializedName("default_dosage_units") val defaultDosageUnits: ArrayList<DefaultDosageUnitsItem>? = null,

    @field:SerializedName("frequency_units") val frequencyUnits: ArrayList<FrequencyUnitsItem>? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("dosage_units") val dosageUnits: ArrayList<DosageUnitsItem>? = null
)

data class AddTreatmentRequestResponse(

    @field:SerializedName("data") val data: AddTreatmentRequestResponseData? = null,

    @field:SerializedName("success") val success: Boolean? = null,

    @field:SerializedName("message") val message: String? = null
)

data class AddTreatmentRequestResponseData(

    @field:SerializedName("treatment_history") val treatmentHistory: AddTreatmentRequestResponseTreatmentHistory? = null
)

data class AddTreatmentRequestResponseTreatmentHistory(

    @field:SerializedName("treatment_dosage_history") val treatmentDosageHistory: List<TreatmentDosageHistoryItem?>? = null,

    @field:SerializedName("purposes") val purposes: List<PurposesItem?>? = null,

    @field:SerializedName("reminder_notification") val reminderNotification: List<ReminderNotificationsAttributesItem>? = null,

    @field:SerializedName("treatment_category") val treatmentCategory: TreatmentCategory? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("treatment_id") val treatmentId: Int? = null,

    @field:SerializedName("privacy") val privacy: String? = null,

    @field:SerializedName("id") val id: Int? = null
)

data class TreatmentCategory(

    @field:SerializedName("default_dosage_units") val defaultDosageUnits: List<DefaultDosageUnitsItem>? = null,

    @field:SerializedName("frequency_units") val frequencyUnits: List<FrequencyUnitsItem>? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("dosage_units") val dosageUnits: ArrayList<DosageUnitsItem>? = null
)

data class DosesItem(

    @field:SerializedName("quantity") val quantity: String? = null,

    @field:SerializedName("as_needed") val asNeeded: Boolean? = null,

    @field:SerializedName("strength_num_unit_id") val strengthNumUnitId: Int? = null,

    @field:SerializedName("strength_num_amount") val strengthNumAmount: String? = null,

    @field:SerializedName("structured_dosage") val structuredDosage: Any? = null
)

data class TreatmentDosageHistoryItem(

    @field:SerializedName("drugdb_frequency_unit_id") val drugdbFrequencyUnitId: Int? = null,

    @field:SerializedName("doses") val doses: List<DosesItem?>? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("start_date") val startDate: String? = null
)

data class PurposesItem(

    @field:SerializedName("attribution_id") val attributionId: Int? = null,

    @field:SerializedName("name") val name: String? = null
)

data class ReminderNotificationItem(

    @field:SerializedName("notify_time") val notifyTime: String? = null,

    @field:SerializedName("is_notify") val isNotify: Boolean? = null
)
