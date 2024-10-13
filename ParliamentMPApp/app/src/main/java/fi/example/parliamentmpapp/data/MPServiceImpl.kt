package fi.example.parliamentmpapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Object for providing a Retrofit instance for the MPService.
 *
 * This singleton object is responsible for creating and managing the Retrofit
 * service that interacts with the MP API.
 *
 * @date Anish - 2112913 - 12/10/2024
 */
object MPServiceImpl {
    // Base URL for the MP API
    private const val BASE_URL = "https://users.metropolia.fi/~peterh/"

    // Lazy initialization of the Retrofit service
    val service: MPService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MPService::class.java)
    }
}