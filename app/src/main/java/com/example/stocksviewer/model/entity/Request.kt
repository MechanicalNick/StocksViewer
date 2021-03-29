package com.example.stocksviewer.model.entity

import androidx.room.Entity

@Entity(tableName = "request")
data class Request (
    val start: Long,
    val count: Long,
    val total: Long,
    val description: String,
    val quotes: List<Quote>
) { }