package io.arg.cryptowallet.data.repository

import androidx.lifecycle.MutableLiveData
import io.arg.cryptowallet.constant.Constants
import io.arg.cryptowallet.data.model.Resource
import io.arg.cryptowallet.exception.NoBalanceFoundException
import io.arg.cryptowallet.exception.NoTokenFoundForTermException
import io.arg.cryptowallet.server.api.TokensApi
import io.arg.cryptowallet.server.model.ERC20TokenList
import io.arg.cryptowallet.server.model.TokenBalance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TokenRepositoryImpl(private val service: TokensApi) : TokenRepository {

    private val statusSuccess = "1"

    override fun getTokenBalance(symbol: String, compositeDisposable: CompositeDisposable): MutableLiveData<Resource<TokenBalance>> {

        val data = MutableLiveData<Resource<TokenBalance>>()

        data.value = Resource.Loading()

        compositeDisposable.add(
                service.getTokensApi().getTokens()
                        .map {
                            tokenList: ERC20TokenList -> tokenList.tokens.first { s -> s.symbol == symbol }.address
                        }
                        .flatMap { token ->
                            service.getTokensApi().getTokenBalance(token, Constants.apiKey)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({response -> onResponse(data, response)}, {t -> onFailure(data, t) })
        )

        return data
    }

    private fun onResponse(data: MutableLiveData<Resource<TokenBalance>>, tokenBalance: TokenBalance) {
        if(tokenBalance.status == statusSuccess) {
            data.value = Resource.Success(tokenBalance)
        } else {
            data.value = Resource.Failure(NoBalanceFoundException())
        }
    }

    private fun onFailure(data: MutableLiveData<Resource<TokenBalance>>, t: Throwable) {
        data.value = Resource.Failure(t)
    }

}