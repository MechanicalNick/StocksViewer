package com.example.stocksviewer.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.stocksviewer.model.DAO.HistoricalDAO
import com.example.stocksviewer.model.DAO.InfoDAO
import com.example.stocksviewer.model.DAO.PriceDAO
import com.example.stocksviewer.model.DAO.QuoteDAO
import com.example.stocksviewer.model.entity.*
import com.example.stocksviewer.model.service.FinnhubService
import com.example.stocksviewer.model.service.MboumService
import kotlinx.coroutines.Dispatchers


class MainRepository(
    private val quoteDAO: QuoteDAO,
    private val infoDAO: InfoDAO,
    private val priceDAO: PriceDAO,
    private val historicalDAO: HistoricalDAO,
    private val mboumService: MboumService,
    private val finnhubService: FinnhubService
) : BaseDataSource() {
    private val mboumKey = "demo"
    private val finnhubKey = "c0o11k748v6qah6rru2g"

    suspend fun getSymbols() = getResult { mboumService.getSymbols(mboumKey) }
    suspend fun getInfo(symbol: String) = getResult { mboumService.getInfo(symbol, mboumKey) }
    suspend fun getHistoricalData(symbol: String, interval: String) =
        getResult { mboumService.getHistoricData(symbol, interval, mboumKey) }

    suspend fun getPrice(symbol: String) = getResult { finnhubService.getPrice(symbol, finnhubKey) }


    fun getQuotes(): LiveData<Resource<List<QuoteWithInfoAndData>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val source = suspend { quoteDAO.getQuotesWithInfoAndData() }
            .invoke()
            .map { Resource.success(it) }
        emitSource(source)

        val symbols = suspend { getSymbols() }.invoke()
        if (symbols.status == Resource.Status.SUCCESS) {
            var quotes = symbols.data!!
                .map { request -> request.quotes }
                .flatten()
                .map { symbol -> Quote(symbol) }
            suspend {
                quoteDAO.insertAll(quotes)
                for (chunk in quotes.chunked(50)) {
                    var symbols = chunk.joinToString(",") { x -> x.symbol }
                    val info = getInfo(symbols)
                    if (info.status == Resource.Status.SUCCESS) {
                        infoDAO.insertAll(info.data!!)
                    }
                    for (quote in chunk) {
                        val price = getPrice(quote.symbol)
                        if (price.status == Resource.Status.SUCCESS) {
                            var data = price.data
                            if (data != null) {
                                data.symbol = quote.symbol
                                priceDAO.insert(data)
                            }
                        }
                    }
                }
            }.invoke()
        } else if (symbols.status == Resource.Status.ERROR) {
            emit(Resource.error(symbols.message!!))
            emitSource(source)
        }
    }

    fun getQuote(symbol: String): LiveData<QuoteWithInfoAndData> = liveData(Dispatchers.IO) {
        val source = suspend { quoteDAO.getQuote(symbol) }
            .invoke()
        emitSource(source)
    }

    fun getHistoricalDataRequest(symbol: String, interval: String):
            LiveData<Resource<List<HistoricDataRequest>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val source = suspend { historicalDAO.getData(symbol, interval) }
            .invoke()
            .map { Resource.success(it) }
        emitSource(source)
        val data = suspend { getHistoricalData(symbol, interval) }.invoke()
        if (data.status == Resource.Status.SUCCESS) {
            historicalDAO.insert(data.data!!)
        } else if (data.status == Resource.Status.ERROR) {
            emit(Resource.error(data.message!!))
            emitSource(source)
        }
    }

    suspend fun update(quote: QuoteWithInfoAndData) {
        quoteDAO.update(quote.quoteWithInfo.quote)
    }
}