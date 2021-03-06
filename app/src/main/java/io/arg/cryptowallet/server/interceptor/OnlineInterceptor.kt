package io.arg.cryptowallet.server.interceptor

import io.arg.cryptowallet.application.CryptoApplication
import io.arg.cryptowallet.exception.NoConnectivityException
import io.arg.cryptowallet.server.api.TokensApi.CACHE_CONTROL_HEADER
import io.arg.cryptowallet.server.api.TokensApi.HEADER_PRAGMA
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class OnlineInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!CryptoApplication.isInternetAvailable()) {
            throw NoConnectivityException()
        }

        val cacheControl = CacheControl.Builder()
                .maxAge(5, TimeUnit.MINUTES)
                .build()

        return response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(CACHE_CONTROL_HEADER)
                .header(CACHE_CONTROL_HEADER, cacheControl.toString())
                .build()
    }
}