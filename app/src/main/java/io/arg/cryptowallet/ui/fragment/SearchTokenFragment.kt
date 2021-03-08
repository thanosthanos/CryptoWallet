package io.arg.cryptowallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.arg.cryptowallet.R
import io.arg.cryptowallet.adapter.TokenAdapter
import io.arg.cryptowallet.constant.Constants.debounceTimeout
import io.arg.cryptowallet.data.server.model.TokenResult
import io.arg.cryptowallet.databinding.FragmentSearchTokenBinding
import io.arg.cryptowallet.exception.NoBalanceFoundException
import io.arg.cryptowallet.exception.NoConnectivityException
import io.arg.cryptowallet.exception.NoTokenFoundForTermException
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

        initViews()
        initViewModel()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun initViews() {

        binding.searchTokenView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(text: String?): Boolean {
                // Start filtering the list as user start entering the characters
                _textInput.run { onNext("$text".trim()) }

                return false
            }
        })

        // Avoid multiple network requests with debounce operator!
        // Moreover we cannot make more than 2 requests per second with tis API
        disposable.add(
                textInput
                        .debounce(debounceTimeout, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { term ->
                            getTokenBalance(term = term)
                        })
    }

    private fun initViewModel() {

        viewModel.tokenBalance.observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
            run {
                resource.onLoading {
                    showProgressbar()
                }
                .onSuccess { tokensList ->
                    showTokenBalance(tokenList = tokensList)
                }
                .onFailure { error: Throwable ->
                    showError(error = error)
                }
            }
        })
    }

    private fun showProgressbar() {
        binding.progressBar.visibility = VISIBLE
        binding.recyclerView.visibility = GONE
    }

    private fun showTokenBalance(tokenList: List<TokenResult>) {
        binding.progressBar.visibility = INVISIBLE
        binding.recyclerView.visibility = VISIBLE

        val tokenAdapter = TokenAdapter(context = requireContext(), tokenList =  tokenList)
        binding.recyclerView.adapter = tokenAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showError(error: Throwable) {
        binding.progressBar.visibility = INVISIBLE

        when (error) {
            is NoConnectivityException -> {
                Toast.makeText(context, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show()
            }
            is NoTokenFoundForTermException -> {
                Toast.makeText(context, getString(R.string.error_no_valid_token_found), Toast.LENGTH_LONG).show()
            }
            is NoBalanceFoundException -> {
                Toast.makeText(context, getString(R.string.error_no_balance_found), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getTokenBalance(term: String) {
        if(term.isEmpty()) {
            binding.recyclerView.visibility = GONE
            return
        }

        viewModel.getTokenBalance(term = term)
    }
}