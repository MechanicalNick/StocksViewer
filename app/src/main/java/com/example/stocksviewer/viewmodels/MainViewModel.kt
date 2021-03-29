package com.example.stocksviewer.viewmodels

import androidx.lifecycle.*
import com.example.stocksviewer.model.MainRepository
import com.example.stocksviewer.model.entity.QuoteWithInfoAndData
import com.example.stocksviewer.utils.Epsilon
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.math.abs


class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : BaseViewModel(mainRepository) {
    private val query = MutableLiveData("")
    var quotes = mainRepository.getQuotes()
    lateinit var filtredQuotes: LiveData<List<QuoteWithInfoAndData>>
    var submitedQuery = mutableListOf<String>()

    fun getItems(counter: Int): LiveData<List<QuoteWithInfoAndData>> {
        val result = MediatorLiveData<List<QuoteWithInfoAndData>>()

        val filter = {
            val queryStr = query.value!!
            val items = quotes.value!!.data!!

            result.value = if (queryStr.isEmpty()) items.filter { filter(counter, it) }
            else items.filter {
                (it.quote.symbol.contains(queryStr, true) || it.name.contains(queryStr, true))
                        && filter(counter, it)
            }
        }

        result.addSource(quotes) { filter.invoke() }
        result.addSource(query) { filter.invoke() }

        return result
    }

    private fun filter(counter: Int, quote: QuoteWithInfoAndData): Boolean {
        return (counter == 0 || quote.quote.isFavourite) &&
                (quote.price != null && abs(quote.price!!.c) > Epsilon.Value)
    }

    fun handleSearchQuery(query: String?) {
        query?.let {
            this.query.value = it
            if (query.isEmpty())
                return
            var last = submitedQuery.lastOrNull()
            if (last != null && it.startsWith(last)) {
                submitedQuery.remove(last)
            }
            submitedQuery.add(it)
        }
    }
}