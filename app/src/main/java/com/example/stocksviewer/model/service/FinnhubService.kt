package com.example.stocksviewer.model.service

import com.example.stocksviewer.model.entity.Price
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubService {
    @GET("quote")
    suspend fun getPrice(
        @Query("symbol") symbol: String,
        @Query("token") token: String
    ): Response<Price>
}
