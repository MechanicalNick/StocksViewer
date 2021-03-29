package com.example.stocksviewer.model.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.math.abs
import kotlin.math.absoluteValue

@Entity(tableName = "price")
data class Price(
    @PrimaryKey var symbol: String,
    //Current price
    val c: Double,
    //High price of the day
    val h: Double,
    //Low price of the day
    val l: Double,
    //Open price of the day
    val o: Double,
    //Previous close price
    val pc: Double,
    val t: Int
) {
    @Ignore
    var dif: Double = 0.0
        get() = c - o

    @Ignore
    var percent: Double = 0.0
        get() = abs( (c / o - 1) * 100)
}
