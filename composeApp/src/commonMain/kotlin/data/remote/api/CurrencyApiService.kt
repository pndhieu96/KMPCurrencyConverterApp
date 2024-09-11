package data.remote.api

import data.remote.models.CurrencyApiResponse

interface CurrencyApiService {
    suspend fun getLatestExchangeRates(): CurrencyApiResponse
}