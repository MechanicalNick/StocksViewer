package com.example.stocksviewer.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso

import androidx.databinding.BindingAdapter
import com.example.stocksviewer.R

object PicassoAdapter {
    @JvmStatic
    @BindingAdapter("app:url")
    fun loadImage(imageView: ImageView?, url: String?) {
        if (!url.isNullOrEmpty()) {
            Picasso.get()
                .load("https://finnhub.io/api/logo?symbol=${url}")
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.error)
                .fit()
                .transform(RoundedTransformation(35F, 0))
                .into(imageView)
        }
    }
}