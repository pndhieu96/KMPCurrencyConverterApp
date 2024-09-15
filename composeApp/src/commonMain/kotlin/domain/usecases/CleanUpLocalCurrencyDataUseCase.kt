package domain.usecases

import domain.models.Currency
import domain.models.RequestState
import domain.repository_Interfaces.LocalCurrencyRepository
import kotlinx.coroutines.flow.Flow

class CleanUpLocalCurrencyDataUseCase(
    private val localCurrencyRepository: LocalCurrencyRepository
) {
    suspend fun invoke() {
        return localCurrencyRepository.cleanUp()
    }
}