package domain.usecases

import domain.models.Currency
import domain.models.RequestState
import domain.repositories.CurrencyRepository
import domain.repositories.PreferencesRepository

class GetAvailableCurrencyUseCase(
    private val currencyRepository: CurrencyRepository,
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): RequestState<List<Currency>> {
        val (requestState, latestUpdateTime) = currencyRepository.getLatestExchangeRates()



        if(latestUpdateTime.isNotEmpty()) {
            preferencesRepository.saveLastUpdatedTime(latestUpdateTime)
        }
        return requestState
    }
}