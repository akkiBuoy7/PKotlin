package plm.patientslikeme2.data.model.conditions

import com.google.gson.annotations.SerializedName

data class SearchSymptomResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: Any? = null
)

data class Data(

    @field:SerializedName("symptom_infos")
    val symptomInfos: ArrayList<SymptomInfosItem>? = null
)

data class SymptomInfosItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
) {
    override fun toString(): String {
        return name.toString()
    }
}

data class SuggestedSymptomsResponse(

    @field:SerializedName("data")
    val data: SuggestedSymptomsData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: Any? = null
)

data class SuggestedSymptomsData(

    @field:SerializedName("symptoms")
    val symptoms: ArrayList<SuggestedSymptomItem>? = null
)


data class SuggestedSymptomItem(

    @field:SerializedName("ask_severity")
    val askSeverity: Boolean? = null,

    @field:SerializedName("short_definition")
    val shortDefinition: Any? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("long_definition")
    val longDefinition: Any? = null
) {
    override fun toString(): String {
        return name.toString()
    }
}

data class AddSymptomRequest(

    @field:SerializedName("symptoms")
    var symptoms: ArrayList<AddSymptomsItem>? = null,

    @field:SerializedName("id")
    var id: Int? = null
)

data class AddSymptomsItem(

    @field:SerializedName("severity")
    var severity: String? = null,

    @field:SerializedName("other")
    var other: Boolean? = false,

    @field:SerializedName("history_id")
    var historyId: Int? = null,

    @field:SerializedName("privacy")
    var privacy: String? = null,

    @field:SerializedName("id")
    var id: Int? = null,

    var name: String? = null
)





