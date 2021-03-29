package com.example.stocksviewer.model.entity

import androidx.room.*
import com.google.gson.Gson

@Entity(tableName = "historicRequests")
data class HistoricDataRequest(
    @PrimaryKey(autoGenerate = true)
    var id : Long,
    @TypeConverters(Converters::class)
    var items: HistoricItems,
    @Embedded
    val meta: Meta
)

data class Meta(
    val chartPreviousClose: Double,
    val currency: String,
    val dataGranularity: String,
    val exchangeName: String,
    val exchangeTimezoneName: String,
    val firstTradeDate: Int,
    val gmtoffset: Int,
    val instrumentType: String,
    val previousClose: Double,
    val priceHint: Int,
    val range: String,
    val regularMarketPrice: Double,
    val regularMarketTime: Int,
    val scale: Int,
    val symbol: String,
    val timezone: String
)

class Converters {
    @TypeConverter
    fun listToJson(value: HistoricItems?) = Gson().toJson(value?.items)

    @TypeConverter
    fun jsonToList(value: String) = HistoricItems(Gson().fromJson(value, Array<HistoricItem>::class.java).toList())
}

data class HistoricItems(
    var items: List<HistoricItem>
)

@Entity
data class HistoricItem(
    val close: Double,
    val date: String,
    val high: Double,
    val low: Double,
    val `open`: Double
)
