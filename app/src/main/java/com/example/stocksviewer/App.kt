package com.example.stocksviewer

import android.app.Application
import com.example.stocksviewer.di.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    companion object {
        private lateinit var mComponent: AppComponent
        val component: AppComponent get() = mComponent
    }

    private fun initializeDagger() {
        mComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule(this))
            .roomModule(RoomModule(this))
            .build()
    }

}