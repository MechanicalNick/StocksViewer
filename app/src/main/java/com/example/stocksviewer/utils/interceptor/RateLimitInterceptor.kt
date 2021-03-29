package com.example.stocksviewer.utils.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import com.google.common.util.concurrent.RateLimiter

class RateLimitInterceptor : Interceptor {
    private val rateLimiter: RateLimiter = RateLimiter.create(30.0)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        rateLimiter.acquire(1);
        return chain.proceed(chain.request());
    }
}
