package plm.patientslikeme2.data.model.home

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class UserModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @SerializedName("name") var name: String,
    var description: String,
    var isMember: Boolean,
    var memberCount: String
)