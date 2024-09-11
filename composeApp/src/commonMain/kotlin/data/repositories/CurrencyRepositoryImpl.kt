package data.repositories

import data.mappers.DataMapper
import data.remote.api.CurrencyApiService
import domain.models.Currency
import domain.models.RequestState
import domain.repositories.CurrencyRepository

class CurrencyRepositoryImpl(
    private val service: CurrencyApiService,
    private val dataMapper: DataMapper
) : CurrencyRepository {

    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val response = service.getLatestExchangeRates()
            val currencyList = response.data.values.map {
                dataMapper.mapCurrency(it)
            }
            println("CurrencyRepository $currencyList")
            RequestState.Success(currencyList)
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An error occurred. Please try again later."
            println("CurrencyRepository $errorMessage")
            RequestState.Error(errorMessage)
        }
    }
}