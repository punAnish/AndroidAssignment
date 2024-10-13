package fi.example.parliamentmpapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents a member of parliament.
 * @date Anish - 2112913- 12/10/2024
 */

@Entity(tableName = "mp_table")
data class MP(
    @PrimaryKey val hetekaId: Int,
    val seatNumber: Int,
    val lastname: String,
    val firstname: String,
    val party: String,
    val minister: Boolean,
    val pictureUrl: String?

)


