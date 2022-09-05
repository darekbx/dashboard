package com.darekbx.dashboard.ui.widgets.currency

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.repository.currency.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
    ): ViewModel() {

    suspend fun fetchCurrencyData(currency: Currency) : List<Float> {
        return currencyRepository.fetchData(currency)
    }
}