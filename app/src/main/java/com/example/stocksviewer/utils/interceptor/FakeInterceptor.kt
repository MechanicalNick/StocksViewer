package com.example.stocksviewer.utils.interceptor

import android.content.Context
import com.example.stocksviewer.BuildConfig
import com.example.stocksviewer.R
import okhttp3.*
import java.io.*
import java.net.URI


class FakeInterceptor(var applicationContext: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var response: Response? = null
        if (BuildConfig.DEBUG) {
            val uri: URI = chain.request().url().uri()
            val query: String = uri.getQuery()
            val parsedQuery = query.split("=".toRegex()).toTypedArray()

            var responseString = GetResponseString(uri.toString(), parsedQuery)
            response = Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            response = chain.proceed(chain.request())
        }

        return response
    }

    fun GetResponseString(
        uri: String,
        parsedQuery: Array<String>
    ): String {
        val responseString: String
        if (uri.contains("trending")) {
            responseString = getRawResource(R.raw.trending)
        } else if (uri.contains("financial-data")) {
            responseString = getRawResource(R.raw.financialdata)
        } else if (uri.contains("quote?symbol=VFIAX%2CLTHM%2CCETX")) {
            responseString = getRawResource(R.raw.quotevfiax)
        } else if (uri.contains("quote?symbol=O%2CAONE%2CBNTX")) {
            responseString = getRawResource(R.raw.quotevfiax)
        } else if (uri.contains("quote?symbol=BTC-USD%2CETH-USD")) {
            responseString = getRawResource(R.raw.quotebtc)
        } else if (uri.contains("1d")) {
            responseString = getRawResource(R.raw.history1d)
        } else if (uri.contains("1h")) {
            responseString = getRawResource(R.raw.history1h)
        } else if (uri.contains("1wk")) {
            responseString = getRawResource(R.raw.history1wk)
        } else if (uri.contains("1mo")) {
            responseString = getRawResource(R.raw.history1mo)
        } else if (uri.contains("3mo")) {
            responseString = getRawResource(R.raw.history3mo)
        } else {
            responseString = ""
        }
        return responseString
    }

    private fun getRawResource(resource: Int): String {
        var XmlFileInputStream = applicationContext.getResources().openRawResource(resource)
        return readTextFile(XmlFileInputStream)
    }

    fun readTextFile(inputStream: InputStream): String {
        val outputStream = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var len: Int
        try {
            while (inputStream.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
        }
        return outputStream.toString()
    }
}