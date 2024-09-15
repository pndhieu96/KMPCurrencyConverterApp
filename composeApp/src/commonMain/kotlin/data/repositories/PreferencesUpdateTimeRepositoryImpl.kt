package data.repositories

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.repository_Interfaces.UpdatedTimeRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant

@OptIn(ExperimentalSettingsApi::class)
class PreferencesUpdateTimeRepositoryImpl(
    private val settings: Settings
) : UpdatedTimeRepository {
    companion object {
        const val TIMESTAMP_KEY = "lastUpdated"
    }

    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()

    override suspend fun saveLastUpdatedTime(lastUpdated: String) {
        flowSettings.putLong(
            key = TIMESTAMP_KEY,
            value = Instant.parse(lastUpdated).toEpochMilliseconds()
        )
    }

    override suspend fun getLastUpdatedTime(): Long {
        val lastUpdated = flowSettings.getLongFlow(
            key = TIMESTAMP_KEY,
            defaultValue = 0L
        ).first()
        return lastUpdated
    }
}