package fi.example.parliamentmpapp.data

import retrofit2.http.GET

/**
 * Service interface for interacting with the MP API using Retrofit.
 * This interface defines the HTTP methods for fetching Member of Parliament (MP) data.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
interface MPService {
    // Fetch all MPs from the API
    @GET("seating.json")
    suspend fun getMPs(): List<MP>


    // Fetch extra MP data from the API
    @GET("extras.json")
    suspend fun getExtraMPData(): List<ExtraMPData>

}