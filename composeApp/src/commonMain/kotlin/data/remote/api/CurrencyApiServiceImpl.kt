package data.remote.api

import data.remote.models.CurrencyApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl : CurrencyApiService {
    companion object {
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
        const val API_KEY = "cur_live_wTMLyFA8lOneWC383NeahGr4Cf5DyTtcoziVPxL5"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
        install(DefaultRequest) {
            header("apiKey", API_KEY)
        }
    }

    override suspend fun getLatestExchangeRates(): CurrencyApiResponse {
        val response = httpClient.get(ENDPOINT)
        val apiResponse = Json.decodeFromString<CurrencyApiResponse>(response.body())
        return apiResponse
    }
}