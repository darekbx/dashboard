package com.darekbx.dashboard.ui.widgets.nbp

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.repository.nbp.BaseNbpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NbpViewModel @Inject constructor(
    private val currencyRepository: BaseNbpRepository,
) : ViewModel() {

    suspend fun fetchCurrencyData(currency: Currency): Result<CommonWrapper> {
        return currencyRepository.fetchCurrencyData(currency)
    }

    suspend fun fetchGoldPriceData(goldPrice: GoldPrice): Result<CommonWrapper> {
        return currencyRepository.fetchGoldPriceData(goldPrice)
    }
}
