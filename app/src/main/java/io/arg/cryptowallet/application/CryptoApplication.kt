package io.arg.cryptowallet.application

import android.app.Application
import android.content.Context
import io.arg.cryptowallet.di.networkModule
import io.arg.cryptowallet.di.repositoryModule
import io.arg.cryptowallet.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CryptoApplication: Application() {

    companion object {
        lateinit var ctx: Context
    }

    override fun onCreate() {
        super.onCreate()

        ctx = applicationContext
        startKoin()
    }

    private fun startKoin() {

        startKoin {
            // declare used Android context
            androidContext(ctx)
            // declare modules
            val modules = listOf(viewModelModule, repositoryModule, networkModule)
            modules(modules)
        }
    }

}