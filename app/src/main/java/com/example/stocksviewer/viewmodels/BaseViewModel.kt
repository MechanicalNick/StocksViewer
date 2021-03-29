package com.example.stocksviewer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocksviewer.model.MainRepository
import com.example.stocksviewer.model.entity.QuoteWithInfoAndData
import kotlinx.coroutines.launch

open class BaseViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun onFavouriteClick(quote: QuoteWithInfoAndData) {
        viewModelScope.launch {
            quote.quote.isFavourite = !quote.quote.isFavourite
            mainRepository.update(quote)
        }
    }

}