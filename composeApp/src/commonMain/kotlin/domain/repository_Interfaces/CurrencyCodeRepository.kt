package domain.repository_Interfaces

import domain.models.CurrencyCode
import kotlinx.coroutines.flow.Flow

interface CurrencyCodeRepository {
    suspend fun saveSourceCurrencyCode(code: String)
    suspend fun saveTargetCurrencyCode(code: String)
    fun readSourceCurrencyCode(): Flow<CurrencyCode>
    fun readTargetCurrencyCode(): Flow<CurrencyCode>
}