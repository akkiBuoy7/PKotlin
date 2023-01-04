package plm.patientslikeme2.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import plm.patientslikeme2.data.model.home.UserModel

/**
 * Abstracts access to the user database
 */
@Dao
interface UserDao {

    /**
     * Insert user into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: List<UserModel>): List<Long>

    /**
     * Get all the user from database
     */
    @Query("SELECT * FROM user")
    fun getUser(): LiveData<List<UserModel>>

    @Query("DELETE FROM user")
    abstract fun deleteAllUser()
}