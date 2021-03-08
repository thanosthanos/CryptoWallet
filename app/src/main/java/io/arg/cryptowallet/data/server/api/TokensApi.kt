package io.arg.cryptowallet.data.server.api

import io.arg.cryptowallet.application.CryptoApplication.Companion.ctx
import io.arg.cryptowallet.data.server.interceptor.OnlineInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object TokensApi {

    private const val baseUrl = "https://api.etherscan.io/"
    private const val cacheSize = (5 * 1024 * 1024).toLong()
    private val myCache = Cache(ctx.cacheDir, cacheSize)
    const val CACHE_CONTROL_HEADER = "Cache-Control"
    const val HEADER_PRAGMA = "Pragma"

    private val apiClient = OkHttpClient
            .Builder()
            .addInterceptor(OnlineInterceptor())
            .cache(myCache)
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(apiClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getTokensApi(): ERC20TokenService {
        return retrofit.create(ERC20TokenService::class.java)
    }

}