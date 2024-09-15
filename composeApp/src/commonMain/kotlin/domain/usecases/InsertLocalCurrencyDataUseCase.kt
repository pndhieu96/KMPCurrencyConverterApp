package domain.usecases

import domain.models.Currency
import domain.repository_Interfaces.LocalCurrencyRepository

class InsertLocalCurrencyDataUseCase(
    private val localCurrencyRepository: LocalCurrencyRepository
) {
    suspend operator fun invoke(currency: Currency) {
        localCurrencyRepository.insertCurrencyData(currency)
    }
}