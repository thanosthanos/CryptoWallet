package io.arg.cryptowallet.data.repository

import androidx.lifecycle.MutableLiveData
import io.arg.cryptowallet.constant.Constants
import io.arg.cryptowallet.data.resource.Resource
import io.arg.cryptowallet.exception.NoBalanceFoundException
import io.arg.cryptowallet.data.server.api.TokensApi
import io.arg.cryptowallet.data.server.model.ERC20Token
import io.arg.cryptowallet.data.server.model.ERC20TokenList
import io.arg.cryptowallet.data.server.model.TokenBalance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TokenRepositoryImpl(private val service: TokensApi) : TokenRepository {

    private val statusSuccess = "1"
    private lateinit var filteredList: List<ERC20Token>

    override fun getTokenBalance(symbol: String, compositeDisposable: CompositeDisposable): MutableLiveData<Resource<TokenBalance>> {

        val data = MutableLiveData<Resource<TokenBalance>>()

        data.value = Resource.Loading()

        filteredList = ArrayList()
        compositeDisposable.add(
                service.getTokensApi().getTokens()
                        .flatMapIterable {
                            tokenList: ERC20TokenList -> getFilteredList(tokenList = tokenList, symbol = symbol)
                        }
                        .concatMap {
                            token -> service.getTokensApi().getTokenBalance(address =  token.address, apikey = Constants.apiKey)
                        }
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({response -> onResponse(data, response)}, {t -> onFailure(data, t) })
        )

        return data
    }

    private fun getFilteredList(tokenList: ERC20TokenList, symbol: String): List<ERC20Token> {
        filteredList = tokenList.tokens.filter { s -> s.symbol.contains(symbol) }

        return filteredList
    }

    private fun onResponse(data: MutableLiveData<Resource<TokenBalance>>, tokenBalance: List<TokenBalance>) {
        print("")
//        if(tokenBalance.status == statusSuccess) {
//            data.value = Resource.Success(tokenBalance)
//        } else {
//            data.value = Resource.Failure(NoBalanceFoundException())
//        }
    }

    private fun onFailure(data: MutableLiveData<Resource<TokenBalance>>, t: Throwable) {
        data.value = Resource.Failure(t)
    }

}
