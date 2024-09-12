package domain.repositories

import domain.models.Currency
import domain.models.RequestState

interface CurrencyRepository {
    suspend fun getLatestExchangeRates(): Pair<RequestState<List<Currency>>, String>
}