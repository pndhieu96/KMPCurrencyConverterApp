package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import data.mappers.DataMapper
import data.remote.api.CurrencyApiServiceImpl
import data.repositories.CurrencyRepositoryImpl
import presentation.component.HomeHeader
import presentation.uistate.RateStatus
import surfaceColor

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(surfaceColor)
        ) {
            HomeHeader(
                status = RateStatus.Fresh,
                onRateRefresh = {}
            )
        }
    }
}