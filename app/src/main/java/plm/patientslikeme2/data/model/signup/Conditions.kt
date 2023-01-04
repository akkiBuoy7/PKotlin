package plm.patientslikeme2.data.model.signup

data class Conditions(
    var id: Int? = null,
    var condition_id: Int? = null,
    var name: String? = null,
    var position: String? = null,
    var stage_name: String? = null,
    var stage_id: String? = null,
    var privacy: String? = null,
    var diagnosed_since: String? = null
) {

    override fun toString(): String {
        return name.toString()
    }
}

data class ConditionsResponse(
    var success: Boolean? = null,
    var message: Any? = null,
    var data: ArrayList<Conditions>? = null
)

data class AddConditionsResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: Conditions? = null
)

data class ConditionsStageResponse(
    var success: Boolean? = null,
    var message: Any? = null,
    var data: ArrayList<ConditionsStage>? = null
)

data class ConditionsStage(
    var id: Int? = null,
    var name: String? = null
) {

    override fun toString(): String {
        return name.toString()
    }
}

data class DeleteConditionsResponse(
    var success: Boolean? = null,
    var message: String? = null
)

data class AddConditionsRequest(
    var condition_id: Int? = null,
    var stage_id: String? = null,
    var privacy: String? = null
)

data class UpdateConditionsRequest(
    var condition_id: Int? = null,
    var privacy: String? = null
)