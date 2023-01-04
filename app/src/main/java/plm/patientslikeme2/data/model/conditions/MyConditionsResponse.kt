package plm.patientslikeme2.data.model.conditions

import com.google.gson.annotations.SerializedName

data class MyConditionsResponse(

    @field:SerializedName("success") val success: Boolean? = null,

    @field:SerializedName("message") val message: String? = null,

    @field:SerializedName("data") val data: MyConditionsResponseData? = null
)

data class MyConditionsResponseData(

    @field:SerializedName("condition_infos") val conditionInfos: List<ConditionInfosItem>? = null
)

data class ConditionInfosItem(

    @field:SerializedName("first_symptom_date") val firstSymptomDate: String? = null,

    @field:SerializedName("known_symptoms") val knownSymptoms: List<KnownSymptomsItem?>? = null,

    @field:SerializedName("stage_id") val stageId: Int? = null,

    @field:SerializedName("stage_name") val stageName: String? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("privacy") val privacy: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("position") val position: Int? = null,

    @field:SerializedName("condition_id") val conditionId: Int? = null,

    @field:SerializedName("diagnosed_since") val diagnosedSince: String? = null,

    @field:SerializedName("condition_stage_present") val conditionStagePresent: Boolean? = null
)

data class KnownSymptomsItem(

    @field:SerializedName("symptom_info_id") val symptomInfoId: Int? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("privacy") val privacy: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("history") val history: List<HistoryItem>? = null
)

data class MyConditionsItem(

    @field:SerializedName("first_symptom_date") val firstSymptomDate: String? = null,

    @field:SerializedName("known_symptoms") val knownSymptoms: List<KnownSymptomsItem>? = null,

    @field:SerializedName("stage_id") val stageId: Int? = null,

    @field:SerializedName("stage_name") val stageName: String? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("privacy") val privacy: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("position") val position: Int? = null,

    @field:SerializedName("condition_id") val conditionId: Int? = null,

    @field:SerializedName("diagnosed_since") val diagnosedSince: String? = null
)

data class HistoryItem(

    @field:SerializedName("symptom_id") val symptomId: Int? = null,

    @field:SerializedName("severity") val severity: Int? = null,

    @field:SerializedName("end_date") val endDate: String? = null,

    @field:SerializedName("symptom_info_id") val symptomInfoId: Int? = null,

    @field:SerializedName("updated_at") val updatedAt: String? = null,

    @field:SerializedName("created_at") val createdAt: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("start_date") val startDate: String? = null
)

data class AddConditionRequest(

    @field:SerializedName("symptoms") var symptoms: ArrayList<AddSymptomsItem>? = null,

    @field:SerializedName("symptom_date") var symptomDate: String? = null,

    @field:SerializedName("diagnosis_date") var diagnosisDate: String? = null,

    @field:SerializedName("privacy") var privacy: String? = null,

    @field:SerializedName("stage_id") var stageId: String? = null,

    @field:SerializedName("condition_id") var conditionId: Int? = null
)

data class EditConditionRequestResponse(

    @field:SerializedName("data") val data: MyConditionsItem? = null,

    @field:SerializedName("success") val success: Boolean? = null,

    @field:SerializedName("message") val message: String? = null
)

data class SymptomsItem(

    @field:SerializedName("other") val other: Boolean? = null,

    @field:SerializedName("privacy") val privacy: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("severity") var severity: Int? = null
)

data class AddConditionRequestResponse(

    @field:SerializedName("data") val data: ConditionInfosItem? = null,

    @field:SerializedName("success") val success: Boolean? = null,

    @field:SerializedName("message") val message: String? = null
)


