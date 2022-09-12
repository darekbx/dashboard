package com.darekbx.dashboard.repository.crypto

import com.darekbx.dashboard.model.BitcoinPrice
import com.darekbx.dashboard.repository.CommonWrapper

interface BaseCryptoRepository {

    suspend fun fetchBitcoinPrice(bitcoinPrice: BitcoinPrice): Result<CommonWrapper>
}