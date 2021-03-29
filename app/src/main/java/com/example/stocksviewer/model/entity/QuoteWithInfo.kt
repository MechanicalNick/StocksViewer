package com.example.stocksviewer.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class QuoteWithInfo(
    @Embedded var quote: Quote,
    @Relation(
        parentColumn = "symbol",
        entityColumn = "symbol",
        entity = InfoItem::class
    )
    var infoItem: InfoItem?
) {

}