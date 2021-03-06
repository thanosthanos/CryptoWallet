package io.arg.cryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.arg.cryptowallet.data.resource.Resource
import io.arg.cryptowallet.data.repository.TokenRepositoryImpl
import io.arg.cryptowallet.data.server.model.TokenBalance
import io.reactivex.disposables.CompositeDisposable

class TokensViewModel(private val repository: TokenRepositoryImpl): ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val termMutableData = MutableLiveData<String>()

    val tokenBalance: LiveData<Resource<TokenBalance>> = Transformations.switchMap(termMutableData) { term ->
        repository.getTokenBalance(symbol = term, compositeDisposable = compositeDisposable)
    }

    fun getTokenBalance(term: String) {
        termMutableData.value = term
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}