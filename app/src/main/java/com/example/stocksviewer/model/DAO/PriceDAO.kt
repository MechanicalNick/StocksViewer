package com.example.stocksviewer.model.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.stocksviewer.model.entity.Price

@Dao
interface PriceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(price: Price)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(price: Price)

    @Update
    suspend fun update(price: Price)

    @Delete
    suspend fun delete(price: Price)

    @Query("SELECT * FROM price")
    fun getAll(): LiveData<List<Price>>

    @Query("DELETE FROM price")
    suspend fun deleteTable()
}