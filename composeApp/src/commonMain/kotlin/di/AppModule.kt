package di

import com.russhwolf.settings.Settings
import data.mappers.DataMapper
import data.remote.api.CurrencyApiService
import data.remote.api.CurrencyApiServiceImpl
import data.repositories.KtorApiCurrencyRepositoryImpl
import data.repositories.MongoLocalCurrencyRepositoryImpl
import data.repositories.PreferencesRepositoryImpl
import domain.repository_Interfaces.ApiCurrencyRepository
import domain.repository_Interfaces.CurrencyCodeRepository
import domain.repository_Interfaces.LocalCurrencyRepository
import domain.repository_Interfaces.UpdatedTimeRepository
import domain.usecases.CleanUpLocalCurrencyDataUseCase
import domain.usecases.GetAvailableCurrencyUseCase
import domain.usecases.InsertLocalCurrencyDataUseCase
import domain.usecases.ReadLocalCurrencyDataUseCase
import domain.usecases.ReadSourceCurrencyCodeUseCase
import domain.usecases.ReadTargetCurrencyCodeUseCase
import domain.usecases.ValidateFreshCurrencyDataUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screen.Home.HomeViewModel

val appModule = module {
    single { DataMapper() }
    single { Settings() }
    single<CurrencyApiService> { CurrencyApiServiceImpl() }
    single<ApiCurrencyRepository> { KtorApiCurrencyRepositoryImpl(get(), get()) }
    single<UpdatedTimeRepository> { PreferencesRepositoryImpl(get()) }
    single<LocalCurrencyRepository> { MongoLocalCurrencyRepositoryImpl(get()) }
    single<CurrencyCodeRepository> { PreferencesRepositoryImpl(get()) }
    single { GetAvailableCurrencyUseCase(get(), get()) }
    single { ValidateFreshCurrencyDataUseCase(get()) }
    single { CleanUpLocalCurrencyDataUseCase(get()) }
    single { InsertLocalCurrencyDataUseCase(get()) }
    single { ReadLocalCurrencyDataUseCase(get()) }
    single { ReadSourceCurrencyCodeUseCase(get()) }
    single { ReadTargetCurrencyCodeUseCase(get()) }
    factory {
        HomeViewModel(
            getAvailableCurrencyUc = get(),
            validateFreshCurrencyDataUc = get(),
            cleanUpLocalCurrencyDataUseCase = get(),
            insertLocalCurrencyDataUC = get(),
            readLocalCurrencyDataUC = get(),
            readTargetCurrencyCodeUseCase = get(),
            readSourceCurrencyCodeUseCase = get()
        )
    }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}