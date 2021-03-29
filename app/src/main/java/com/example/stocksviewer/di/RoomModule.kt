package com.example.stocksviewer.di

import android.app.Application
import androidx.room.Room
import com.example.stocksviewer.model.AppDatabase
import com.example.stocksviewer.model.DAO.HistoricalDAO
import com.example.stocksviewer.model.DAO.InfoDAO
import com.example.stocksviewer.model.DAO.PriceDAO
import com.example.stocksviewer.model.DAO.QuoteDAO
import com.example.stocksviewer.model.MainRepository
import com.example.stocksviewer.model.service.FinnhubService
import com.example.stocksviewer.model.service.MboumService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(app: Application) {
    private val databaseName = "appDatabase"

    private var database: AppDatabase = Room
        .databaseBuilder(app.applicationContext, AppDatabase::class.java, databaseName)
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providesAppDatabase(): AppDatabase {
        return database
    }

    @Singleton
    @Provides
    fun providesQuoteDao(database: AppDatabase): QuoteDAO {
        return database.quoteDAO()
    }

    @Singleton
    @Provides
    fun providesInfoDao(database: AppDatabase): InfoDAO {
        return database.infoDAO()
    }

    @Singleton
    @Provides
    fun providesPriceDao(database: AppDatabase): PriceDAO {
        return database.priceDAO()
    }

    @Singleton
    @Provides
    fun providesHistoricalDao(database: AppDatabase): HistoricalDAO {
        return database.historicalDAO()
    }

    @Singleton
    @Provides
    fun mainRepository(
        quoteDAO: QuoteDAO,
        infoDAO: InfoDAO,
        priceDAO: PriceDAO,
        providesHistoricalDao : HistoricalDAO,
        mboumService: MboumService,
        finnhubService: FinnhubService
    ): MainRepository {
        return MainRepository(quoteDAO, infoDAO, priceDAO, providesHistoricalDao, mboumService, finnhubService)
    }
}