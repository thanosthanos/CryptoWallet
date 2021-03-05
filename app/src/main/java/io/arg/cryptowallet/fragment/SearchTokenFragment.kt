package io.arg.cryptowallet.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.arg.cryptowallet.constant.Constants
import io.arg.cryptowallet.databinding.FragmentSearchTokenBinding
import io.arg.cryptowallet.server.api.TokensApi
import io.arg.cryptowallet.server.model.ERC20TokenList
import io.arg.cryptowallet.server.model.TokenBalance
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SearchTokenFragment : Fragment() {

    private lateinit var binding: FragmentSearchTokenBinding
    private val disposable = CompositeDisposable()
    private val _textInput = BehaviorSubject.create<String>()
    private val textInput = _textInput.toFlowable(BackpressureStrategy.LATEST)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentSearchTokenBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun initView() {
        binding.searchTokenEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                _textInput.onNext(binding.searchTokenEditText.text.toString())
            }

            override fun beforeTextChanged(charSeq: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSeq: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        // Avoid multiple network requests with debounce operator!
        disposable.add(
                textInput
                        .debounce(1_000, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { address ->
                            test(address = address)
                        })
    }

    private fun test(address: String) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
                TokensApi().getTokensApi().getTokens()
                        .map {
                            tokenList: ERC20TokenList -> getAddressFromTokenList(address, tokenList)
                        }
                        .flatMap { token ->
                            TokensApi().getTokensApi().getTokenBalance(token, Constants.apiKey)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({response -> onResponse(response)}, {t -> onFailure(t) })
        )
    }

    private fun getAddressFromTokenList(tokenName: String, list: ERC20TokenList): String {

        list.tokens.forEach { token ->
            if(tokenName == token.symbol) return token.address
        }

        return ""
    }

    private fun onResponse(tokenBalance: TokenBalance) {
        Toast.makeText(requireContext(), tokenBalance.result, Toast.LENGTH_SHORT).show()
    }

    private fun onFailure(t: Throwable) {
        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
    }

}