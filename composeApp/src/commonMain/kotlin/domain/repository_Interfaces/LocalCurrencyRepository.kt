package domain.repository_Interfaces

import domain.models.Currency
import domain.models.RequestState
import kotlinx.coroutines.flow.Flow

interface LocalCurrencyRepository {
    suspend fun insertCurrencyData(currency: Currency)
    fun readCurrencyData(): Flow<RequestState<List<Currency>>>
    suspend fun cleanUp()
}