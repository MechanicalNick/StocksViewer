package com.example.stocksviewer.di

import android.app.Application
import com.example.stocksviewer.model.entity.HistoricItems
import com.example.stocksviewer.model.service.FinnhubService
import com.example.stocksviewer.model.service.MboumService
import com.example.stocksviewer.utils.GetItemsDeserializer
import com.example.stocksviewer.utils.interceptor.FakeInterceptor
import com.example.stocksviewer.utils.interceptor.RateLimitInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(var app: Application) {
    @Singleton
    @Provides
    fun provideMboumService(): MboumService {
        var client = OkHttpClient.Builder()
            .addInterceptor(FakeInterceptor(app.applicationContext))
            .build()

        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(HistoricItems::class.java, GetItemsDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl("https://mboum.com/api/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(MboumService::class.java)
    }

    @Singleton
    @Provides
    fun provideFinnhubService(): FinnhubService {
        var client = OkHttpClient.Builder()
            .addInterceptor(RateLimitInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(FinnhubService::class.java)
    }
}