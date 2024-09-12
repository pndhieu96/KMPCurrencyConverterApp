package di

import data.mappers.DataMapper
import data.remote.api.CurrencyApiService
import data.remote.api.CurrencyApiServiceImpl
import data.repositories.CurrencyRepositoryImpl
import data.repositories.PreferencesRepositoryImpl
import domain.repositories.CurrencyRepository
import domain.repositories.PreferencesRepository
import domain.usecases.GetAvailableCurrencyUseCase
import domain.usecases.ValidateFreshCurrencyDataUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single { DataMapper() }
    single<CurrencyApiService> { CurrencyApiServiceImpl() }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get(), get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single { GetAvailableCurrencyUseCase(get(), get()) }
    single { ValidateFreshCurrencyDataUseCase(get()) }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}