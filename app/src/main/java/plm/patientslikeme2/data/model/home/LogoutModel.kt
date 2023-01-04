package plm.patientslikeme2.data.model.home

data class LogoutModel(
    var token: Int,
    var client_id: Int,
    var app_type: Int
)