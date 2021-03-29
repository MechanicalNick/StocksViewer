package com.example.stocksviewer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.stocksviewer.model.MainRepository
import com.example.stocksviewer.model.Resource
import com.example.stocksviewer.model.entity.HistoricDataRequest
import com.example.stocksviewer.model.entity.QuoteWithInfoAndData
import com.example.stocksviewer.utils.Interval
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val mainRepository: MainRepository) :
    BaseViewModel(mainRepository) {
    private val symbol = MutableLiveData<String>()
    private val interval = MutableLiveData<String>()

    private val _quote = symbol.switchMap { s ->
        mainRepository.getQuote(s)
    }
    val quote: LiveData<QuoteWithInfoAndData> = _quote

    private val _data = interval.switchMap { i ->
        mainRepository.getHistoricalDataRequest(symbol.value!!, i)
    }
    var data: LiveData<Resource<List<HistoricDataRequest>>> = _data


    fun start(newSymbol: String) {
        symbol.value = newSymbol
    }
    fun setInterval(newInterval: String) {
        interval.value = newInterval
    }

    init {
        setInterval(Interval.D.description)
    }
}