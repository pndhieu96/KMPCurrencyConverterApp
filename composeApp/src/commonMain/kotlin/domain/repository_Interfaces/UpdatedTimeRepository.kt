package domain.repository_Interfaces

interface UpdatedTimeRepository {
    suspend fun saveLastUpdatedTime(lastUpdated: String)
    suspend fun getLastUpdatedTime(): Long
}