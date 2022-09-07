package com.darekbx.dashboard.ui.widgets.nbp

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice
import com.darekbx.dashboard.repository.nbp.CurrencyWrapper
import com.darekbx.dashboard.repository.nbp.GoldPriceWrapper
import com.darekbx.dashboard.repository.nbp.remote.NbpRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NbpViewModel @Inject constructor(
    private val currencyRepository: NbpRemoteRepository,
) : ViewModel() {

    suspend fun fetchCurrencyData(currency: Currency): Result<CurrencyWrapper> {
        return currencyRepository.fetchCurrencyData(currency)
    }

    suspend fun fetchGoldPriceData(goldPrice: GoldPrice): Result<GoldPriceWrapper> {
        return currencyRepository.fetchGoldPriceData(goldPrice)
    }
}
