package com.example.stocksviewer.utils.onclick

import com.example.stocksviewer.model.entity.QuoteWithInfoAndData

interface OnQuoteItemClick {
    fun onClick(quote: QuoteWithInfoAndData)
}