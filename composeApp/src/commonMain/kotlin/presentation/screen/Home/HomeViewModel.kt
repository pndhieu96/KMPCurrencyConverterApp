package presentation.screen.Home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.models.Currency
import domain.models.RequestState
import domain.usecases.GetAvailableCurrencyUseCase
import domain.usecases.InsertLocalCurrencyDataUseCase
import domain.usecases.ReadLocalCurrencyDataUseCase
import domain.usecases.ReadSourceCurrencyCodeUseCase
import domain.usecases.ReadTargetCurrencyCodeUseCase
import domain.usecases.ValidateFreshCurrencyDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.uistate.RateStatus

sealed class HomeUiEvent {
    data object RefreshRates: HomeUiEvent()
    data object SwitchCurrencies: HomeUiEvent()
}

class HomeViewModel(
    private val getAvailableCurrencyUc: GetAvailableCurrencyUseCase,
    private val validateFreshCurrencyDataUc: ValidateFreshCurrencyDataUseCase,
    private val insertLocalCurrencyDataUC: InsertLocalCurrencyDataUseCase,
    private val readLocalCurrencyDataUC: ReadLocalCurrencyDataUseCase,
    private val cleanUpLocalCurrencyDataUseCase: ReadLocalCurrencyDataUseCase,
    private val readSourceCurrencyCodeUseCase: ReadSourceCurrencyCodeUseCase,
    private val readTargetCurrencyCodeUseCase: ReadTargetCurrencyCodeUseCase
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
            readTargetCurrencyCode()
            readSourceCurrencyCode()
        }
    }

    fun sendEvent(event: HomeUiEvent) {
        when(event) {
            is HomeUiEvent.RefreshRates -> {
                screenModelScope.launch {
                    fetchNewRates()
                }
            }
            is HomeUiEvent.SwitchCurrencies -> {
                switchCurrencies()
            }
        }
    }

    private fun switchCurrencies() {
        val source = _sourceCurrency.value
        val target = _targetCurrency.value
        _sourceCurrency.value = target
        _targetCurrency.value = source
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

    private suspend fun readTargetCurrencyCode() {
        screenModelScope.launch(Dispatchers.Main) {
            _targetCurrency.value = RequestState.Loading
            readTargetCurrencyCodeUseCase().collectLatest { currencyCode ->
                val selectedCurrency = _allCurrencies.find { it.code == currencyCode.name}
                if(selectedCurrency != null) {
                    _targetCurrency.value = RequestState.Success(data = selectedCurrency)
                } else {
                    _targetCurrency.value = RequestState.Error(message = "Couldn't find the selected")
                }
            }
        }
    }

    private suspend fun readSourceCurrencyCode() {
        screenModelScope.launch(Dispatchers.Main) {
            _sourceCurrency.value = RequestState.Loading
            readSourceCurrencyCodeUseCase().collectLatest { currencyCode ->
                val selectedCurrency = _allCurrencies.find { it.code == currencyCode.name}
                if(selectedCurrency != null) {
                    _sourceCurrency.value = RequestState.Success(data = selectedCurrency)
                } else {
                    _sourceCurrency.value = RequestState.Error(message = "Couldn't find the selected")
                }
            }
        }
    }

    private suspend fun getRateStatus() {
        val isDataFresh = validateFreshCurrencyDataUc()
        _rateStatus.value = if(isDataFresh) RateStatus.Fresh else RateStatus.Stale
    }
}