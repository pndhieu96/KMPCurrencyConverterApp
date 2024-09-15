package domain.usecases

import domain.models.Currency
import domain.models.RequestState
import domain.repository_Interfaces.LocalCurrencyRepository
import kotlinx.coroutines.flow.Flow

class ReadLocalCurrencyDataUseCase(
    private val localCurrencyRepository: LocalCurrencyRepository
) {
    operator fun invoke() : Flow<RequestState<List<Currency>>> {
        return localCurrencyRepository.readCurrencyData()
    }
}