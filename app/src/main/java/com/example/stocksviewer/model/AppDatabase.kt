package com.example.stocksviewer.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stocksviewer.model.DAO.HistoricalDAO
import com.example.stocksviewer.model.DAO.InfoDAO
import com.example.stocksviewer.model.DAO.PriceDAO
import com.example.stocksviewer.model.DAO.QuoteDAO
import com.example.stocksviewer.model.entity.*

@Database(entities = [Quote::class, InfoItem::class, Price::class, HistoricDataRequest::class], version = 7)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quoteDAO(): QuoteDAO
    abstract fun infoDAO(): InfoDAO
    abstract fun priceDAO(): PriceDAO
    abstract fun historicalDAO(): HistoricalDAO
}