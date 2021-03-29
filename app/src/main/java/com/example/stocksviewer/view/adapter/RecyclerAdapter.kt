package com.example.stocksviewer.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.stocksviewer.R
import com.example.stocksviewer.databinding.ListItemViewBinding
import com.example.stocksviewer.model.entity.QuoteWithInfoAndData
import com.example.stocksviewer.utils.onclick.OnQuoteItemClick
import com.example.stocksviewer.viewmodels.MainViewModel

class RecyclerAdapter(var mainViewModel: MainViewModel, private val listner: OnQuoteItemClick) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var quotes: List<QuoteWithInfoAndData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil
            .inflate(inflater, R.layout.list_item_view, parent, false) as ListItemViewBinding;
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Bind", "bind, position = " + position)
        var quote = quotes.get(position)
        holder.bind(quote, mainViewModel, position, listner)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    fun updateData(newQuotes: List<QuoteWithInfoAndData>) {
        quotes = newQuotes.toList()
        notifyDataSetChanged()
    }

    class ViewHolder internal constructor(var binding: ListItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: QuoteWithInfoAndData, mainViewModel: MainViewModel, position: Int, listner: OnQuoteItemClick) {
            binding.quote = quote
            binding.mainViewModel = mainViewModel
            binding.position = position
            binding.quoteListner = listner
            binding.executePendingBindings()
        }
    }
}