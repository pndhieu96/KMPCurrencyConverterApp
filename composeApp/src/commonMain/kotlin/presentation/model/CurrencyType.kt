package presentation.model

import domain.models.Currency
import domain.models.CurrencyCode

sealed class CurrencyType(val code: CurrencyCode) {
    data class Source(val currencyCode: CurrencyCode): CurrencyType(currencyCode)
    data class Target(val currencyCode: CurrencyCode): CurrencyType(currencyCode)
    data object Default: CurrencyType(CurrencyCode.USD)
}