package plm.patientslikeme2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import plm.patientslikeme2.data.model.home.UserModel

/**
 * App Database
 * Define all entities and access doa's here/ Each entity is a table.
 */
@Database(entities = [UserModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userModel(): UserDao
}