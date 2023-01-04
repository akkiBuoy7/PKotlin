package plm.patientslikeme2.data.model.home

data class ConditionCardModel(
    var id: Int,
    var icon: String,
    var description: String
)

data class ConditionCardResponse(
    var success: Boolean,
    var message: String,
    var data: ConditionCardModel
)