package io.arg.cryptowallet.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.arg.cryptowallet.R
import io.arg.cryptowallet.constant.Constants.ethereumAccountAddress
import io.arg.cryptowallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {

        binding.ethereumAddressTextView.text = ethereumAccountAddress

        binding.showTokensButton.setOnClickListener {
            val intent = Intent(this, SearchTokenActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter_in, 0)
        }

    }

}