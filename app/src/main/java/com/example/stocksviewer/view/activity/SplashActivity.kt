package com.example.stocksviewer.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.stocksviewer.R
import com.example.stocksviewer.utils.NetworkChecker

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var loading = findViewById<TextView>(R.id.loading)
        var error = findViewById<TextView>(R.id.error)
        var tryAgainButton = findViewById<AppCompatButton>(R.id.tryAgainButton)
        if(NetworkChecker.isNetworkConnected(applicationContext)){
            error.visibility = View.GONE
            tryAgainButton.visibility = View.GONE
            loading.visibility = View.VISIBLE
            success()
        }
        else{
            error.visibility = View.VISIBLE
            tryAgainButton.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }

    fun recreate(view: View?) {
        recreate()
    }

    private fun success() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}