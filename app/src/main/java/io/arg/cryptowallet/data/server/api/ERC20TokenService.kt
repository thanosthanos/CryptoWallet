package io.arg.cryptowallet.data.server.api

import io.arg.cryptowallet.data.server.model.ERC20TokenList
import io.arg.cryptowallet.data.server.model.TokenBalance
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ERC20TokenService {

    @GET("https://api.ethplorer.io/getTopTokens?limit=100&apiKey=freekey")
    fun getTokens(): Observable<ERC20TokenList>

    @GET("api?module=account&action=tokenbalance&contractaddress=0x57d90b64a1a57749b0f932f1a3395792e12e7055&tag=latest")
    fun getTokenBalance(@Query("address") address: String, @Query("apikey") apikey: String): Observable<TokenBalance>

}