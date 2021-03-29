package com.example.stocksviewer.model.service

import com.example.stocksviewer.model.entity.HistoricDataRequest
import com.example.stocksviewer.model.entity.InfoRequest
import com.example.stocksviewer.model.entity.SymbolsRequest
import com.example.stocksviewer.model.entity.Request
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MboumService {
    @GET("co/collections/?list=day_gainers&start=1")
    suspend fun getRequest(@Query("apikey") apiKey: String): Response<Request>

    @GET("qu/quote")
    suspend fun getInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): Response<InfoRequest>

    @GET("tr/trending")
    suspend fun getSymbols(@Query("apikey") apiKey: String): Response<SymbolsRequest>

    @GET(" hi/history")
    suspend fun getHistoricData(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("apikey") apiKey: String
    ): Response<HistoricDataRequest>
}