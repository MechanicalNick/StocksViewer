package com.example.stocksviewer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.example.stocksviewer.R
import com.example.stocksviewer.model.entity.HistoricItemWithCurrency
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {
    private val price: TextView = findViewById(R.id.baloon_price)
    private val date: TextView = findViewById(R.id.baloon_date)

    override fun refreshContent(e: Entry, highlight: Highlight) {
        var item = e.data as HistoricItemWithCurrency
        price.text = PriceConverter.convert(item.historicItem.open.toString(), item.currency)
        date.text = item.historicItem.date
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}