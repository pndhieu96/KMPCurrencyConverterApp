package di

import com.russhwolf.settings.Settings
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
import presentation.screen.Home.HomeViewModel

val appModule = module {
    single { DataMapper() }
    single { Settings() }
    single<CurrencyApiService> { CurrencyApiServiceImpl() }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get(), get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single { GetAvailableCurrencyUseCase(get(), get()) }
    single { ValidateFreshCurrencyDataUseCase(get()) }
    factory {
        HomeViewModel(
            getAvailableCurrencyUseCase = get(),
            validateFreshCurrencyDataUseCase = get()
        )
    }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}