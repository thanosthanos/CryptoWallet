package io.arg.cryptowallet.data.repository

import androidx.lifecycle.MutableLiveData
import io.arg.cryptowallet.data.resource.Resource
import io.arg.cryptowallet.data.server.model.TokenResult
import io.reactivex.disposables.CompositeDisposable

interface TokenRepository {
    fun getTokenBalance(symbol: String, compositeDisposable: CompositeDisposable): MutableLiveData<Resource<List<TokenResult>>>
}