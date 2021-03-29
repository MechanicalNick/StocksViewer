package com.example.stocksviewer.utils

object PriceConverter {
    fun convert(price: String?, currency : String?) : String{
        return when {
            price.isNullOrEmpty() -> ""
            currency.isNullOrEmpty() -> price
            currency.equals("usd", true) -> "$ ${price}"
            currency.equals("rub", true) -> "${price} ₽"
            else -> "${price} ${currency}"
        }
    }
}