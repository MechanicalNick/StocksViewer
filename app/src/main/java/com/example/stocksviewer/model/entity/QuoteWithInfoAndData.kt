package com.example.stocksviewer.model.entity

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.example.stocksviewer.utils.PriceConverter
import kotlin.math.abs
import kotlin.math.round

data class QuoteWithInfoAndData(
    @Embedded var quoteWithInfo: QuoteWithInfo,
    @Relation(
        parentColumn = "symbol",
        entityColumn = "symbol"
    )
    var price: Price?
) {

    var quote: Quote
        get() = this.quoteWithInfo.quote
        set(value) {
            this.quoteWithInfo.quote = value
        }

    var info: InfoItem?
        get() = this.quoteWithInfo.infoItem
        set(value) {
            this.quoteWithInfo.infoItem = value
        }

    @Ignore
    var name: String = ""
        get() = if (this.quoteWithInfo.infoItem != null)
            (this.quoteWithInfo.infoItem!!.longName ?: this.quoteWithInfo.infoItem!!.shortName)
        else ""


    @Ignore
    var difText: String = ""
        get() {
            price?.let { it ->
                var abs = abs(Math.round(it.dif * 10.0) / 10.0)
                if (abs < 0.01)
                    return ""
                var sign = if (it.dif >= 0) "+" else "-"
                var price = PriceConverter.convert("%.2f".format(abs), info?.financialCurrency)
                return "${sign}${price}(${"%.2f".format(it.percent)}%)"
            }
            return ""
        }

    @Ignore
    var currentPrice: String = ""
        get() = PriceConverter.convert(price?.c.toString(), info?.financialCurrency)

}