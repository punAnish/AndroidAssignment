package fi.example.parliamentmpapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for accessing Comment entities in the Room database.
 *
 * @date Anish - 2112913 - 12/10/2024
 */

@Dao
interface CommentDao {
    // Inserts a comment, replacing if conflict occurs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: Comment)

    // Retrieves comments for a specific MP
    @Query("SELECT * FROM comment_table WHERE mpHetekaId = :mpHetekaId")
    fun getCommentsForMP(mpHetekaId: Int): Flow<List<Comment>>

}