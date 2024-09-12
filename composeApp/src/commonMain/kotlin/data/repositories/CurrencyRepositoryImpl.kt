package data.repositories

import data.mappers.DataMapper
import data.remote.api.CurrencyApiService
import domain.models.Currency
import domain.models.CurrencyCode
import domain.models.RequestState
import domain.repositories.CurrencyRepository
import domain.repositories.PreferencesRepository

class CurrencyRepositoryImpl(
    private val service: CurrencyApiService,
    private val dataMapper: DataMapper
) : CurrencyRepository {

    override suspend fun getLatestExchangeRates(): Pair<RequestState<List<Currency>>, String> {
        return try {
            val response = service.getLatestExchangeRates()

            val currencyList = response.data.values.map {
                dataMapper.mapCurrency(it)
            }

            val availableCurrencyCodes = response.data.keys.filter {
                CurrencyCode.entries
                    .map { code -> code.name }
                    .toSet()
                    .contains(it)
            }

            val availableCurrencyList = currencyList.filter {
                availableCurrencyCodes.contains(it.code)
            }

            println("CurrencyRepository $availableCurrencyList")
            Pair(RequestState.Success(availableCurrencyList), response.meta.lastUpdatedAt)
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An error occurred. Please try again later."
            println("CurrencyRepository $errorMessage")
            Pair(RequestState.Error(errorMessage), "")
        }
    }
}