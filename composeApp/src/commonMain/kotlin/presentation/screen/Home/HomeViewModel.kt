package presentation.screen.Home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.usecases.GetAvailableCurrencyUseCase
import domain.usecases.ValidateFreshCurrencyDataUseCase
import kotlinx.coroutines.launch
import presentation.uistate.RateStatus

sealed class HomeUiEvent {
    data object RefreshRates: HomeUiEvent()
}

class HomeViewModel(
    private val getAvailableCurrencyUseCase: GetAvailableCurrencyUseCase,
    private val validateFreshCurrencyDataUseCase: ValidateFreshCurrencyDataUseCase
) : ScreenModel {
    private var _rateStatus = mutableStateOf(RateStatus.Idle)
    val rateStatus : State<RateStatus> = _rateStatus

    init {
        screenModelScope.launch {
            fetchNewRates()
        }
    }

    fun sendEvent(event: HomeUiEvent) {
        when(event) {
            is HomeUiEvent.RefreshRates -> {
                screenModelScope.launch {
                    fetchNewRates()
                }
            }
        }
    }

    private suspend fun fetchNewRates() {
        try {
            getAvailableCurrencyUseCase()
            getRateStatus()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private suspend fun getRateStatus() {
        val isDataFresh = validateFreshCurrencyDataUseCase()
        _rateStatus.value = if(isDataFresh) RateStatus.Fresh else RateStatus.Stale
    }
}