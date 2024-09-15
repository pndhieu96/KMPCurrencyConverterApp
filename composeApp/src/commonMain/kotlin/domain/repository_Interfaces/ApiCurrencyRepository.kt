package domain.repository_Interfaces

import domain.models.Currency
import domain.models.RequestState

interface ApiCurrencyRepository {
    suspend fun getLatestExchangeRates(): Pair<RequestState<List<Currency>>, String>
}