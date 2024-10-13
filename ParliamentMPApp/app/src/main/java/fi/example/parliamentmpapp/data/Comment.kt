package fi.example.parliamentmpapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a comment made about a member of parliament.
 *
 * @date Anish - 2112913 - 12/10/2024
 */

@Entity(tableName = "comment_table")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mpHetekaID: Int,
    val text: String
)