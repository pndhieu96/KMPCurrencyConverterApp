package di

import com.russhwolf.settings.Settings
import data.mappers.DataMapper
import data.remote.api.CurrencyApiService
import data.remote.api.CurrencyApiServiceImpl
import data.repositories.KtorApiCurrencyRepositoryImpl
import data.repositories.MongoLocalCurrencyRepositoryImpl
import data.repositories.PreferencesUpdateTimeRepositoryImpl
import domain.repository_Interfaces.ApiCurrencyRepository
import domain.repository_Interfaces.LocalCurrencyRepository
import domain.repository_Interfaces.UpdatedTimeRepository
import domain.usecases.CleanUpLocalCurrencyDataUseCase
import domain.usecases.GetAvailableCurrencyUseCase
import domain.usecases.InsertLocalCurrencyDataUseCase
import domain.usecases.ReadLocalCurrencyDataUseCase
import domain.usecases.ValidateFreshCurrencyDataUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screen.Home.HomeViewModel

val appModule = module {
    single { DataMapper() }
    single { Settings() }
    single<CurrencyApiService> { CurrencyApiServiceImpl() }
    single<ApiCurrencyRepository> { KtorApiCurrencyRepositoryImpl(get(), get()) }
    single<UpdatedTimeRepository> { PreferencesUpdateTimeRepositoryImpl(get()) }
    single<LocalCurrencyRepository> { MongoLocalCurrencyRepositoryImpl(get()) }
    single { GetAvailableCurrencyUseCase(get(), get()) }
    single { ValidateFreshCurrencyDataUseCase(get()) }
    single { CleanUpLocalCurrencyDataUseCase(get()) }
    single { InsertLocalCurrencyDataUseCase(get()) }
    single { ReadLocalCurrencyDataUseCase(get()) }
    factory {
        HomeViewModel(
            getAvailableCurrencyUc = get(),
            validateFreshCurrencyDataUc = get(),
            cleanUpLocalCurrencyDataUseCase = get(),
            insertLocalCurrencyDataUC = get(),
            readLocalCurrencyDataUC = get()
        )
    }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}