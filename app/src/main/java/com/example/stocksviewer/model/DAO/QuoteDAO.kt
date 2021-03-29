package com.example.stocksviewer.model.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.stocksviewer.model.entity.Quote
import com.example.stocksviewer.model.entity.QuoteWithInfo
import com.example.stocksviewer.model.entity.QuoteWithInfoAndData

@Dao
interface QuoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: Quote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<Quote>)

    @Update
    suspend fun update(quote: Quote)

    @Delete
    suspend fun delete(quote: Quote)

    @Query("SELECT * FROM quotes")
    fun getAll(): LiveData<List<Quote>>

    @Query("DELETE FROM quotes")
    suspend fun deleteTable()

    @Transaction
    @Query("SELECT * FROM quotes")
    fun getQuotesWithInfoAndData(): LiveData<List<QuoteWithInfoAndData>>

    @Transaction
    @Query("SELECT * FROM quotes WHERE symbol = :symbol")
    fun getQuote(symbol : String): LiveData<QuoteWithInfoAndData>
}