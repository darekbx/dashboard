package com.darekbx.dashboard.ui.widgets.stock

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.StockPrice
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.repository.stockprice.BaseStockPriceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StockPriceViewModel @Inject constructor(
    private val stockPriceRepository: BaseStockPriceRepository
) : ViewModel() {

    suspend fun fetchStockPrice(stockPrice: StockPrice): Result<CommonWrapper> {
        return stockPriceRepository.fetchStockPrice(stockPrice)
    }
}
