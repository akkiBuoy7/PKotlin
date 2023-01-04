package plm.patientslikeme2.data.model.global

import com.google.gson.Gson

data class GlobalModel(
    var login: FeatureLogin,
)

data class FeatureLogin(
    val username: Boolean = true,
    val password: Boolean = true,
    val forget_password: Boolean = true,
    val remember_me: Boolean = true,
    val create_account: Boolean = true,
)

data class SignupLogin(
    val username: Boolean = true,
    val password: Boolean = true,
    val create_account: Boolean = true,
)

data class MasterData(
    var success: Boolean? = null,
    var message: String? = null,
    var data: Data? = Data(),
)

data class Details(
    var id: String? = null,
    var name: String? = null,
) {
    override fun toString(): String {
        return name.toString()
    }
}

data class JobsData(
    var enlisted: ArrayList<String>? = null,
    var officer: ArrayList<String>? = null,
    var warrant: ArrayList<String>? = null,
)

data class Jobs(
    var id: String? = null,
    var name: String? = null,
    var data: JobsData? = JobsData(),
) {
    override fun toString(): String {
        return name.toString()
    }
}

data class RanksData(
    var enlisted: ArrayList<String>? = null,
    var officer: ArrayList<String>? = null,
)

data class Ranks(
    var id: String? = null,
    var name: String? = null,
    var data: RanksData? = RanksData(),
) {
    override fun toString(): String {
        return name.toString()
    }
}

data class Military(
    var branches: ArrayList<Details>? = null,
    var jobs: ArrayList<Jobs>? = null,
    var ranks: ArrayList<Ranks>? = null,
    var statuses: ArrayList<Details>? = null,
    var combat_zones: ArrayList<Details>? = null,
)

data class Data(
    var date_formats: ArrayList<Details>? = null,
    var education_levels: ArrayList<Details>? = null,
    var ethinicity: ArrayList<Details>? = null,
    var genders: ArrayList<Details>? = null,
    var health_insurance_types: ArrayList<Details>? = null,
    var interests: ArrayList<Details>? = null,
    var military: Military? = Military(),
    var sexes: ArrayList<Details>? = null,
    var country: ArrayList<String> = arrayListOf(),
) {

    fun stringToObject(string: String?): Data {
        return Gson().fromJson(string, Data::class.java)
    }
}

