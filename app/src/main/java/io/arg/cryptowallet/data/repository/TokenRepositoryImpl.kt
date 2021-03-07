package io.arg.cryptowallet.data.repository

import androidx.lifecycle.MutableLiveData
import io.arg.cryptowallet.constant.Constants
import io.arg.cryptowallet.data.resource.Resource
import io.arg.cryptowallet.exception.NoBalanceFoundException
import io.arg.cryptowallet.data.server.api.TokensApi
import io.arg.cryptowallet.data.server.model.ERC20Token
import io.arg.cryptowallet.data.server.model.ERC20TokenList
import io.arg.cryptowallet.data.server.model.TokenBalance
import io.arg.cryptowallet.data.server.model.TokenResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TokenRepositoryImpl(private val service: TokensApi) : TokenRepository {

    private val statusSuccess = "1"

    override fun getTokenBalance(symbol: String, compositeDisposable: CompositeDisposable): MutableLiveData<Resource<List<TokenResult>>> {

        val data = MutableLiveData<Resource<List<TokenResult>>>()

        data.value = Resource.Loading()

        compositeDisposable.add(
                service.getTokensApi().getTokens()
                        .flatMapIterable {
                            tokenList: ERC20TokenList -> tokenList.tokens.filter { s -> s.symbol.contains(symbol) }
                        }
                        .concatMap {
                            token -> getTokenResult(token = token)
                        }
                        .toList()
                        .map {
                            tokenListResult -> tokenListResult.filter { tokenResult -> tokenResult.isValid }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({response -> onResponse(data, response)}, {t -> onFailure(data, t) })
        )

        return data
    }

    private fun getTokenResult(token: ERC20Token): Observable<TokenResult> {
        val apiResult = service.getTokensApi().getTokenBalance(address = token.address, apikey = Constants.apiKey)
        val balance = apiResult.blockingFirst()

        return Observable.just(TokenResult(isValid = balance.status == statusSuccess, symbol = token.symbol, balance = balance.result))
    }

    private fun onResponse(data: MutableLiveData<Resource<List<TokenResult>>>, tokenList: List<TokenResult>) {
        if(tokenList.isNotEmpty()) {
            data.value = Resource.Success(tokenList)
        } else {
            data.value = Resource.Failure(NoBalanceFoundException())
        }
    }

    private fun onFailure(data: MutableLiveData<Resource<List<TokenResult>>>, t: Throwable) {
        data.value = Resource.Failure(t)
    }

}
