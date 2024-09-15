package presentation.screen.Home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.models.Currency
import domain.models.RequestState
import domain.usecases.CleanUpLocalCurrencyDataUseCase
import domain.usecases.GetAvailableCurrencyUseCase
import domain.usecases.InsertLocalCurrencyDataUseCase
import domain.usecases.ReadLocalCurrencyDataUseCase
import domain.usecases.ValidateFreshCurrencyDataUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.uistate.RateStatus

sealed class HomeUiEvent {
    data object RefreshRates: HomeUiEvent()
}

class HomeViewModel(
    private val getAvailableCurrencyUc: GetAvailableCurrencyUseCase,
    private val validateFreshCurrencyDataUc: ValidateFreshCurrencyDataUseCase,
    private val insertLocalCurrencyDataUC: InsertLocalCurrencyDataUseCase,
    private val readLocalCurrencyDataUC: ReadLocalCurrencyDataUseCase,
    private val cleanUpLocalCurrencyDataUseCase: ReadLocalCurrencyDataUseCase
) : ScreenModel {
    private val _rateStatus = mutableStateOf(RateStatus.Idle)
    val rateStatus : State<RateStatus> = _rateStatus

    private val _sourceCurrency = mutableStateOf<RequestState<Currency>> (RequestState.Idle)
    val sourceCurrency : State<RequestState<Currency>>  = _sourceCurrency

    private val _targetCurrency = mutableStateOf<RequestState<Currency>> (RequestState.Idle)
    val targetCurrency : State<RequestState<Currency>> = _targetCurrency

    private val _allCurrencies = mutableStateListOf<Currency>()
    val allCurrency : List<Currency> = _allCurrencies

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
            val localCache = readLocalCurrencyDataUC().first()
            if(localCache.isSuccess()) {
                if(localCache.getSuccessData().isNotEmpty()) {
                    println("HomeViewModel: DATABASE IS FULL")
                    _allCurrencies.addAll(localCache.getSuccessData())
                    if(!validateFreshCurrencyDataUc()) {
                        cacheTheData()
                    } else {
                        println("HomeViewModel: DATABASE IS FRESH")
                    }
                } else {
                    println("HomeViewModel: DATABASE NEEDS DATA")
                    cacheTheData()
                }
            } else if (localCache.isError()) {
                println("HomeViewModel: ERROR READING LOCAL DATABASE ${localCache.getErrorMessage()}")
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private suspend fun cacheTheData() {
        val fetchData = getAvailableCurrencyUc()
        if(fetchData.isSuccess()) {
            cleanUpLocalCurrencyDataUseCase()
            println("HomeViewModel: ADDING ${fetchData.getSuccessData().toList()}")
            fetchData.getSuccessData().forEach {
                insertLocalCurrencyDataUC(it)
            }
            _allCurrencies.addAll(fetchData.getSuccessData())
        } else if (fetchData.isError()) {
            println("HomeViewModel: ${fetchData.getErrorMessage()}")
        }
        getRateStatus()
    }

    private suspend fun getRateStatus() {
        val isDataFresh = validateFreshCurrencyDataUc()
        _rateStatus.value = if(isDataFresh) RateStatus.Fresh else RateStatus.Stale
    }
}