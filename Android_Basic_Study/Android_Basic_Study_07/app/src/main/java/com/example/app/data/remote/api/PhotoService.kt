package com.example.app.data.remote.api

import com.example.app.data.model.PhotoDetailDTO.ResponseDetailDTO
import com.example.app.data.model.ResponsePhotoDTOItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoService {

    @GET("/photos")
    suspend fun getPhotos(
        @Header("Accept-Version") version: String,
        @Header("Authorization") clientId: String,
    ): Response<List<ResponsePhotoDTOItem>>

    @GET("photos/random")
    suspend fun getRandomPhotos(
        @Header("Accept-Version") version: String,
        @Header("Authorization") clientId: String,
        @Query("count") count: Int = 10,
    ): Response<List<ResponsePhotoDTOItem>>

    @GET("photos/{id}")
    suspend fun getPhotoDetail(
        @Header("Accept-Version") version: String,
        @Header("Authorization") clientId: String,
        @Path("id") id: String,
    ): Response<ResponseDetailDTO>
}