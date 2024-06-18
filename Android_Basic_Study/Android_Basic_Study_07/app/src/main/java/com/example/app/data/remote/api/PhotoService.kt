package com.example.app.data.remote.api

import com.example.app.data.model.ResponsePhotoDTOItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {
    @GET("photos/random")
    suspend fun getRandomPhotos(
        @Query("client_id") clientId: String,
        @Query("count") count: Int = 5,
        @Query("query") query: String? = null
    ): Response<List<ResponsePhotoDTOItem>>
}