package com.darekbx.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.darekbx.dashboard.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(): ViewModel() {

    fun fetchCurrencyData(currency: Currency) = liveData {
        delay(1000)
        if (currency.to == Currency.CurrencyType.EUR) {
            emit(listOf(4.56f, 4.57f, 4.60f, 4.59f, 4.53f, 4.61f, 4.64f, 4.69f))
        } else {
            emit(listOf(4.56f, 4.57f, 4.60f, 4.59f, 4.53f, 4.61f, 4.64f, 4.69f).map { it * 1.2F })
        }
    }
}