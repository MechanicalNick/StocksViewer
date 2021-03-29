package com.example.stocksviewer.model.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.stocksviewer.model.entity.InfoItem
import com.example.stocksviewer.model.entity.Quote
import com.example.stocksviewer.model.entity.QuoteWithInfo

@Dao
interface InfoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(infoItem: InfoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(infoItems: List<InfoItem>)

    @Update
    suspend fun update(infoItem: InfoItem)

    @Delete
    suspend fun delete(infoItem: InfoItem)

    @Query("SELECT * FROM infoItems")
    fun getAll(): LiveData<List<InfoItem>>

    @Query("DELETE FROM infoItems")
    suspend fun deleteTable()
}

