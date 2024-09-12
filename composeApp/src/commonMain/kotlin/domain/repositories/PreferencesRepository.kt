package domain.repositories

interface PreferencesRepository {
    suspend fun saveLastUpdatedTime(lastUpdated: String)
    suspend fun getLastUpdatedTime(): Long
}