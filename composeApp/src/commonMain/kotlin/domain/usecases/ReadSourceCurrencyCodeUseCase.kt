package domain.usecases

import domain.repository_Interfaces.CurrencyCodeRepository

class ReadSourceCurrencyCodeUseCase(
    private val currencyCodeRepository: CurrencyCodeRepository
) {
    operator fun invoke() = currencyCodeRepository.readSourceCurrencyCode()
}