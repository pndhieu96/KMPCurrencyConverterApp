package domain.usecases

import domain.repository_Interfaces.CurrencyCodeRepository

class ReadTargetCurrencyCodeUseCase(
    private val currencyCodeRepository: CurrencyCodeRepository
) {
    operator fun invoke() = currencyCodeRepository.readTargetCurrencyCode()
}