package com.example.stocksviewer.model.entity

class SymbolsRequest : ArrayList<SymbolsRequestItem>()

data class SymbolsRequestItem(
    val count: Int,
    val jobTimestamp: Long,
    val quotes: List<String>,
    val startInterval: Long
)
