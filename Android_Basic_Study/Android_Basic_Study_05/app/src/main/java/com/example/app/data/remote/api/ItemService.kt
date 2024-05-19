package com.example.app.data.remote.api

import com.example.app.data.model.ResponseItemDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItemService {
    @GET("products/search")
    fun getItem(
        @Query("q") q: String
    ): Response<ResponseItemDTO>
}