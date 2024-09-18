package domain.usecases

import domain.repository_Interfaces.CurrencyCodeRepository

class SaveSourceCurrencyCodeUseCase(
    private val currencyCodeRepository: CurrencyCodeRepository
) {
    suspend operator fun invoke(code: String) {
        currencyCodeRepository.saveSourceCurrencyCode(code)
    }
}