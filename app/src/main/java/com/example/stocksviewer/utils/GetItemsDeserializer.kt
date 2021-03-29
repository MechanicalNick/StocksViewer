package com.example.stocksviewer.utils

import com.example.stocksviewer.model.entity.HistoricItem
import com.example.stocksviewer.model.entity.HistoricItems
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetItemsDeserializer : JsonDeserializer<HistoricItems> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): HistoricItems {
        var jsonObject = json!!.getAsJsonObject();
        var set = jsonObject.entrySet()
        var items = mutableListOf<HistoricItem>()
        for (element in set) {
            var obj = element.value.asJsonObject
            var close = obj.get("close").asDouble
            var date = obj.get("date").asString
            var high = obj.get("high").asDouble
            var low = obj.get("low").asDouble
            var open = obj.get("open").asDouble
            var item = HistoricItem(close, date, high, low, open)
            items.add(item)
        }
        return HistoricItems(items)
    }
}