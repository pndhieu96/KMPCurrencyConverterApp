package data.mappers

import data.remote.models.CurrencyApiData
import data.remote.models.CurrencyApiResponse
import domain.models.Currency

class DataMapper {
    fun mapCurrency(currencyApiData: CurrencyApiData): Currency {
        return Currency(
            code = currencyApiData.code,
            value = currencyApiData.value
        )
    }
}