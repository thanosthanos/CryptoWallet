package io.arg.cryptowallet.server.interceptor

import android.content.Context
import android.net.ConnectivityManager
import io.arg.cryptowallet.application.CryptoApplication
import io.arg.cryptowallet.exception.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.Request
import java.io.IOException

class NetworkAndCachingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isInternetAvailable()) {
            throw NoConnectivityException()
        }

        val builder: Request.Builder = chain.request().newBuilder()

        return chain.proceed(builder.build())
    }

    private fun isInternetAvailable(): Boolean {
        val cm = CryptoApplication.ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
    }

}