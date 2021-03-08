package io.arg.cryptowallet.data.server.api

import io.arg.cryptowallet.data.server.model.ERC20TokenList
import io.arg.cryptowallet.data.server.model.TokenBalance
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ERC20TokenService {

    @GET("https://api.ethplorer.io/getTopTokens?limit=100&apiKey=freekey")
    fun getTokens(): Observable<ERC20TokenList>

    @GET("api?module=account&action=tokenbalance&address=0xde57844f758a0a6a1910a4787ab2f7121c8978c3&tag=latest")
    fun getTokenBalance(@Query("contractaddress") address: String, @Query("apikey") apikey: String): Observable<TokenBalance>

}