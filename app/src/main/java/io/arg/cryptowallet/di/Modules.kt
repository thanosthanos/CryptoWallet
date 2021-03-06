package io.arg.cryptowallet.di

import io.arg.cryptowallet.data.repository.TokenRepositoryImpl
import io.arg.cryptowallet.data.server.api.TokensApi
import io.arg.cryptowallet.viewmodel.TokensViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule: Module = module {
    viewModel { TokensViewModel(get()) }
}

val repositoryModule: Module = module {
    single { TokenRepositoryImpl(get()) }
}

val networkModule: Module = module {
    single { TokensApi }
}