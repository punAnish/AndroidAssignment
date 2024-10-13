package fi.example.parliamentmpapp.data

import androidx.room.Entity

/**
 * Represents additional data related to a Member of Parliament (MP).
 *
 * @author Anish
 * @date Anish - 2112913 - 12/10/2024
 */
@Entity(tableName = "extra_mp_data_table")
data class ExtraMPData(
    val id: Int,
    val twitter: String?,
    val yearBorn: Int?,
    val constituency: String?

)
