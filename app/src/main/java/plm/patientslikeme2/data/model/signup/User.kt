package plm.patientslikeme2.data.model.signup

import com.google.gson.Gson

data class User(
    var id: Int? = null,
    var metric_guid: String? = null,
    var email: String? = null,
    var login: String? = null,
    var full_name: String? = null,
    var sex: String? = null,
    var canned_genders: ArrayList<String>? = null,
    var additional_gender: String? = null,
    var avatar_url: String? = null,
    var birth_date: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var postcode: String? = null,
    var ethnicity: String? = null,
    var education_level: String? = null,
    var age_and_address_text: String? = null,
    var primary_condition_text: String? = null,
    var primary_interest: Interest? = null,
    var secondary_interests: ArrayList<Interest>? = null,
    var health_insurance_type: String? = null,
    var military_status: String? = null,
    var military_branch_id: String? = null,
    var military_rank: String? = null,
    var military_job: String? = null,
    var bio: Bio? = Bio(),
    var accreditations: ArrayList<Accreditations>? = null,
    var profile_colour: String? = null,
    var communities: ArrayList<Communities>? = null,
    var quick_stats: QuickStats? = QuickStats(),
    var condition_infos: ArrayList<ConditionInfo>? = null,
    var treatment_histories: ArrayList<TreatmentHistories>? = null,
    var signup_step: String? = null,
    var military_deployed_to_combat_zone: String? = null,
    var military_va_healthcare_eligibility: String? = null,
    var military_service_start_date_hash: MilitaryServiceDateHash? = null,
    var military_service_end_date_hash: MilitaryServiceDateHash? = null
) {

    fun stringToObject(string: String?): User {
        return Gson().fromJson(string, User::class.java)
    }
}

data class MilitaryServiceDateHash(
    var year: Int? = null,
    var month: Int? = null
)

data class Accreditations(
    var badge: String? = null,
    var name: String? = null
)

data class Bio(
    var content: String? = null,
    var brief_bio: String? = null
)

data class Communities(
    var id: Int? = null,
    var name: String? = null
)

data class QuickStats(
    var since: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var helpful: String? = null
)

data class ConditionInfo(
    var id: Int? = null,
    var condition_id: Int? = null,
    var name: String? = null,
    var position: String? = null,
    var stage_name: String? = null,
    var diagnosed_since: String? = null,
    var known_symptoms: ArrayList<KnownSymptoms>? = null,
    var privacy: String? = null,
    var stage_id: String? = null,
    var first_symptom_date: String? = null
)

data class Interest(
    var name: String? = null,
    var id: Int? = null
)

data class UserDetailsResponse(
    var user: User? = null
)

data class History(
    var id: Int? = null,
    var symptom_id: Int? = null,
    var symptom_info_id: Int? = null,
    var severity: Int? = null,
    var start_date: String? = null,
    var end_date: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null
)

data class KnownSymptoms(
    var id: Int? = null,
    var symptom_info_id: Int? = null,
    var name: String? = null,
    var privacy: String? = null,
    var history: ArrayList<History> = arrayListOf()
)

data class Purposes(
    var attribution_id: Int? = null,
    var name: String? = null
)

data class FrequencyUnits(
    var id: Int? = null,
    var display_name: String? = null
)

data class TreatmentCategory(
    var id: Int? = null,
    var name: String? = null,
    var default_dosage_units: ArrayList<DosageUnits>? = null,
    var dosage_units: ArrayList<DosageUnits>? = null,
    var frequency_units: ArrayList<FrequencyUnits>? = null
)

data class DosageUnits(
    var id: String? = null,
    var description: String? = null
)

data class ReminderNotificationsAttributes(
    var id: Int? = null,
    var is_notify: Boolean? = null,
    var notify_time: String? = null
)

data class Doses(
    var quantity: String? = null,
    var structured_dosage_id: String? = null,
    var as_needed: Boolean? = null,
    var use_other_dosage: String? = null
)

data class TreatmentDosageHistory(
    var id: Int? = null,
    var start_date: String? = null,
    var drugdb_frequency_unit_id: Int? = null,
    var doses: ArrayList<Doses>? = null
)

data class TreatmentHistories(
    var id: Int? = null,
    var treatment_id: Int? = null,
    var name: String? = null,
    var purposes: ArrayList<Purposes>? = null,
    var privacy: String? = null,
    var treatment_category: TreatmentCategory? = TreatmentCategory(),
    var reminder_notifications_attributes: ArrayList<ReminderNotificationsAttributes> = arrayListOf(),
    var treatment_dosage_history: ArrayList<TreatmentDosageHistory> = arrayListOf()
)