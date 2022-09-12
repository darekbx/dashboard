package com.darekbx.dashboard.ui.widgets.crypto

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.BitcoinPrice
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.repository.crypto.BaseCryptoRepository
import com.darekbx.dashboard.repository.nbp.BaseNbpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val cryptoRepository: BaseCryptoRepository,
    private val nbpRepository: BaseNbpRepository
) : ViewModel() {

    suspend fun fetchBitcoinPrice(bitcoinPrice: BitcoinPrice): Result<CommonWrapper> {
        val latestUsdPrice = nbpRepository.fetchCurrencyData(
            Currency(from = Currency.CurrencyType.PLN, to = Currency.CurrencyType.USD)
        )
        return cryptoRepository.fetchBitcoinPrice(bitcoinPrice).apply {
            this.getOrNull()?.let { data ->
                latestUsdPrice.getOrNull()?.let { currency ->
                    val latest = currency.rates.last()
                    data.rates = data.rates.map { it * latest }
                }
            }
        }
    }
}
