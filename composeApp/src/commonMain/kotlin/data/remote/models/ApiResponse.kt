package data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyApiResponse (
    val meta: CurrencyApiMeta,
    val data: Map<String, CurrencyApiData>
)

@Serializable
data class CurrencyApiMeta (
    @SerialName("last_updated_at")
    val lastUpdatedAt: String
)

@Serializable
data class CurrencyApiData (
    val code: String,
    val value: Double
)