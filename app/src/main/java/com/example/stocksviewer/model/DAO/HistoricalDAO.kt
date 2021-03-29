package com.example.stocksviewer.model.DAO

import android.icu.util.DateInterval
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.stocksviewer.model.entity.HistoricDataRequest
import com.example.stocksviewer.model.entity.QuoteWithInfoAndData

@Dao
interface HistoricalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historicDataRequest: HistoricDataRequest)

    @Query("SELECT * FROM historicRequests WHERE symbol = :symbol AND dataGranularity = :interval LIMIT 1")
    fun getData(symbol : String, interval: String): LiveData<List<HistoricDataRequest>>
}