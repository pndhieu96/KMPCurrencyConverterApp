package presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import data.mappers.DataMapper
import data.remote.api.CurrencyApiServiceImpl
import data.repositories.CurrencyRepositoryImpl

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        LaunchedEffect(Unit) {
            val dataMapper = DataMapper()
            val currencyApiService = CurrencyApiServiceImpl()
            CurrencyRepositoryImpl(
                service = currencyApiService,
                dataMapper = dataMapper
            ).getLatestExchangeRates()
        }
    }
}