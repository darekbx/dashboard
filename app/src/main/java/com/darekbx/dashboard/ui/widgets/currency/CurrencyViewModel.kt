package com.darekbx.dashboard.ui.widgets.currency

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.repository.currency.CurrencyWrapper
import com.darekbx.dashboard.repository.currency.nbp.NbpCurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyRepository: NbpCurrencyRepository,
) : ViewModel() {

    suspend fun fetchCurrencyData(currency: Currency): CurrencyWrapper {
        return currencyRepository.fetchData(currency)
    }
}
