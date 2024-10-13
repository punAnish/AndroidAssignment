package fi.example.parliamentmpapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repository for managing Member of Parliament (MP) data operations.
 * It acts as a mediator between the MPDao and the MPService,
 * handling data access and business logic for MPs.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
class MPRepository (
    private val mpDao: MPDao,
    private val commentDao: CommentDao,
    private val mpService: MPService = MPServiceImpl.service
) {
    // Fetch all MPs from the database as a Flow
    fun getAllMPs(): Flow<List<MP>> = mpDao.getAllMPs()

    // Function to load MPs
    suspend fun loadMPs() {
        // Fetch MPs from the service (API)
        val mpList = withContext(Dispatchers.IO) {
            mpService.getMPs() // Replace with your actual service call
        }
        // Insert the fetched MPs into the database
        mpDao.insertAll(mpList) // Ensure you have an insertAll method in your DAO
    }

    // Insert a new MP into the database
    suspend fun insert(mp: MP) {
        mpDao.insert(mp)
    }

    // Fetch comments for a specific MP
    fun getCommentsForMP(mpHeketaId: Int): Flow<List<Comment>> {
        return mpDao.getCommentsForMP(mpHeketaId)
    }


    //  method to fetch MP images using Coil
    fun getMPImageUrl(mp: MP): String {
        return "https://avoindata.eduskunta.fi/${mp.pictureUrl}"
    }

}