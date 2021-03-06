package io.arg.cryptowallet.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.arg.cryptowallet.databinding.FragmentSearchTokenBinding
import io.arg.cryptowallet.viewmodel.TokensViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SearchTokenFragment : Fragment() {

    private val viewModel by viewModel<TokensViewModel>()
    private lateinit var binding: FragmentSearchTokenBinding
    private val disposable = CompositeDisposable()
    private val _textInput = BehaviorSubject.create<String>()
    private val textInput = _textInput.toFlowable(BackpressureStrategy.LATEST)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentSearchTokenBinding.inflate(inflater, container, false)

        initView()
        initViewModel()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun initView() {

        binding.searchTokenEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                _textInput.onNext(binding.searchTokenEditText.text.toString().trim())
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
                        .subscribe { term ->
                            getTokenBalance(term = term)
                        })
    }

    private fun showProgressbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showTokenBalance() {
        binding.progressBar.visibility = View.INVISIBLE
        // TODO
    }

    private fun showError() {
        binding.progressBar.visibility = View.INVISIBLE
        // TODO
    }

    private fun initViewModel() {

        viewModel.tokenBalance.observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
            run {
                resource.onLoading {
                    showProgressbar()
                }
                .onSuccess { tokenBalance ->
                    showTokenBalance()
                }
                .onFailure { error: Throwable ->
                    showError()
                }
            }
        })
    }

    private fun getTokenBalance(term: String) {
        viewModel.getTokenBalance(term = term)
    }
}