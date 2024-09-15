package domain.usecases

import domain.models.Currency
import domain.models.RequestState
import domain.repository_Interfaces.ApiCurrencyRepository
import domain.repository_Interfaces.LocalCurrencyRepository
import domain.repository_Interfaces.UpdatedTimeRepository

class GetAvailableCurrencyUseCase(
    private val apiCurrencyRepository: ApiCurrencyRepository,
    private val updateTimeRepository: UpdatedTimeRepository

) {
    suspend operator fun invoke(): RequestState<List<Currency>> {
        val (requestState, latestUpdateTime) = apiCurrencyRepository.getLatestExchangeRates()

        if(latestUpdateTime.isNotEmpty()) {
            updateTimeRepository.saveLastUpdatedTime(latestUpdateTime)
        }
        return requestState
    }
}