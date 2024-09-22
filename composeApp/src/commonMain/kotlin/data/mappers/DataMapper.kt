package data.mappers

import data.local.realm_object.CurrencyRO
import data.remote.models.CurrencyApiData
import data.remote.models.CurrencyApiResponse
import domain.models.Currency
import org.mongodb.kbson.ObjectId

class DataMapper {
    fun mapCurrencyApiDateToCurrency(currencyApiData: CurrencyApiData): Currency {
        return Currency(
            code = currencyApiData.code,
            value = currencyApiData.value,
            id = ""
        )
    }
    fun mapCurrencyToCurrencyRO(currency: Currency): CurrencyRO {
        val currencyRO = CurrencyRO()
        currencyRO.code = currency.code
        currencyRO.value = currency.value
        return currencyRO
    }
    fun mapCurrencyROToCurrency(currencyRO: CurrencyRO): Currency {
        return Currency(
            code = currencyRO.code,
            value = currencyRO.value,
            id = currencyRO._id.toHexString()
        )
    }
}