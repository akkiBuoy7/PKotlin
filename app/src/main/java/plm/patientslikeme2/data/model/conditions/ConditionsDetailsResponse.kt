package plm.patientslikeme2.data.model.conditions

import com.google.gson.annotations.SerializedName

data class ConditionsDetailsResponse(

    @field:SerializedName("data") val data: ConditionDetailsData? = null,

    @field:SerializedName("success") val success: Boolean? = null,

    @field:SerializedName("message") val message: String? = null
)

data class ConditionDetailsData(
    @field:SerializedName("condition_info") val conditionInfo: ConditionInfo? = null
)

data class ConditionInfo(

    @field:SerializedName("symptoms") var symptoms: List<SymptomsDetailsItem>? = null,

    @field:SerializedName("first_symptom_date") var firstSymptomDate: String? = null,

    @field:SerializedName("stage_id") val stageId: Int? = null,

    @field:SerializedName("stage_name") val stageName: String? = null,

    @field:SerializedName("name") var name: String? = null,

    @field:SerializedName("privacy") val privacy: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("condition_id") val conditionId: Int? = null,

    @field:SerializedName("diagnosed_since") var diagnosedSince: String? = null,

    @field:SerializedName("treatments") val treatments: List<TreatmentsItem>? = null,

    @field:SerializedName("condition_stage_present") val conditionStagePresent: Boolean? = null
)

data class SymptomsDetailsItem(

    @field:SerializedName("symptom_info_id") val symptomInfoId: Int? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("privacy") val privacy: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("history") val history: List<HistoryDetailsItem>? = null
)

data class HistoryDetailsItem(

    @field:SerializedName("symptom_id") val symptomId: Int? = null,

    @field:SerializedName("severity") val severity: Int? = null,

    @field:SerializedName("end_date") val endDate: String? = null,

    @field:SerializedName("symptom_info_id") val symptomInfoId: Int? = null,

    @field:SerializedName("updated_at") val updatedAt: String? = null,

    @field:SerializedName("created_at") val createdAt: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("start_date") val startDate: String? = null
)

data class TreatmentsItem(

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("treatment_id") val treatmentId: Int? = null,

    @field:SerializedName("privacy") val privacy: String? = null,

    @field:SerializedName("id") val id: Int? = null
)

data class AddSymptomRequestResponse(

    @field:SerializedName("data") val data: SymptomRequestResponseData? = null,

    @field:SerializedName("success") val success: Boolean? = null,

    @field:SerializedName("message") val message: String? = null
)

data class SymptomRequestResponseData(

    @field:SerializedName("symptoms") val symptomInfo: List<SymptomsDetailsItem>? = null
)
