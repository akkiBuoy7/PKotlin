package plm.patientslikeme2.data.model.home

data class GoalTrackingModel(
    var id: Int,
    var icon: String,
    var title: String,
    var color: String,
    var progress: Int
)

data class GoalTrackingResponse(
    var success: Boolean,
    var message: String,
    var data: GoalTrackingData
)

data class GoalTrackingData(
    var goal_tracking: ArrayList<GoalTrackingModel>
)