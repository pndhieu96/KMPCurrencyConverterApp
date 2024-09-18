package domain.usecases

import domain.repository_Interfaces.CurrencyCodeRepository

class SaveTargetCurrencyCodeUseCase(
    private val currencyCodeRepository: CurrencyCodeRepository
) {
    suspend operator fun invoke(code: String) {
        currencyCodeRepository.saveTargetCurrencyCode(code)
    }
}