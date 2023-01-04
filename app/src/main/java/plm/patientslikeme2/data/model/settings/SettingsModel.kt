package plm.patientslikeme2.data.model.settings

data class SettingsModel(
    var id: String,
    var name: String
)

data class SettingsResponse(
    var data: ArrayList<SettingsModel>
)