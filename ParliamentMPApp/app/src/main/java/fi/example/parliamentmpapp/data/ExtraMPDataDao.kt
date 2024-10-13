package fi.example.parliamentmpapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for accessing ExtraMPData entities in the Room database.
 *
 * @date Anish - 2112913 - 12/10/2024
 */

@Dao
interface ExtraMPDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(extraMPData: ExtraMPData)

    @Query("SELECT * FROM extra_mp_data_table WHERE id = :id")
    fun getExtraMPDataById(id: Int): Flow<ExtraMPData?>
}