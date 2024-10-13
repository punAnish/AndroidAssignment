package fi.example.parliamentmpapp.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing ExtraMPData operations.
 * It acts as a mediator between the ExtraMPDataDao and the rest of the application.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
class ExtraMPDataRepository(private val extraMPDataDao: ExtraMPDataDao) {

    // Insert extra MP data
    suspend fun insert(extraMPData: ExtraMPData) {
        extraMPDataDao.insert(extraMPData)
    }

    // Retrieve extra MP data by ID
    fun getExtraMPDataById(id: Int): Flow<ExtraMPData?> {
        return extraMPDataDao.getExtraMPDataById(id)
    }
}
