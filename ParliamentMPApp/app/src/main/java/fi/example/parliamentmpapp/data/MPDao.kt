package fi.example.parliamentmpapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for accessing MP entities in the Room database.
 *
 * @date Anish - 2112913- 12/10/2024
 */

@Dao
interface MPDao {

    // Insert a single MP into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mp: MP)

    // Insert a list of MPs into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mps: List<MP>)

    // Retrieves all members of parliament as a Flow
    @Query("SELECT * FROM mp_table")
    fun getAllMPs(): Flow<List<MP>>

    // Retrieves a member of parliament by their unique HeteKa ID
    @Query("SELECT * FROM mp_table WHERE hetekaId = :hetekaId")
    fun getMPByHeteKaId(hetekaId: Int): Flow<MP?>

    // Fetch comments for a specific MP
    @Query("SELECT * FROM comment_table WHERE mpHetekaID = :heketaId")
    fun getCommentsForMP(heketaId: Int): Flow<List<Comment>>

}