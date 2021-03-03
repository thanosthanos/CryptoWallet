package io.arg.cryptowallet.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.arg.cryptowallet.R
import io.arg.cryptowallet.databinding.ActivitySearchTokenBinding
import io.arg.cryptowallet.fragment.SearchTokenFragment
import io.arg.cryptowallet.helper.ViewHelper.Companion.initToolBar

class SearchTokenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchTokenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(0, R.anim.exit_out)
    }

    private fun initView() {
        initToolBar(this, binding.toolbar)
        addSearchTokenFragment()
    }

    private fun addSearchTokenFragment() {
        val searchTokenFragment = SearchTokenFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_token_fragment, searchTokenFragment)
        transaction.commit()
    }

}