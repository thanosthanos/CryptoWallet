package io.arg.cryptowallet.data.repository

import androidx.lifecycle.MutableLiveData
import io.arg.cryptowallet.data.model.Resource
import io.arg.cryptowallet.server.model.TokenBalance
import io.reactivex.disposables.CompositeDisposable

interface TokenRepository {
    fun getTokenBalance(symbol: String, compositeDisposable: CompositeDisposable): MutableLiveData<Resource<TokenBalance>>
}