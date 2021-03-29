package com.example.stocksviewer.model.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(@PrimaryKey val symbol: String,
                 var isFavourite: Boolean
) {
    constructor(symbol: String) : this(symbol,false)
}
