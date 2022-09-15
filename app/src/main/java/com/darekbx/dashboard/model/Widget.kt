package com.darekbx.dashboard.model

sealed class Widget

/**
 * Currency widget used to display current currency value
 *
 * @param from Currency to calculate from
 * @param to Currency to calculate to
 */
class Currency(val from: CurrencyType, val to: CurrencyType): Widget() {
    enum class CurrencyType {
        PLN, USD, EUR, CHF, GPB
    }
}

/**
 * 1oz stock Gold price chart
 */
class GoldPrice(): Widget()

/**
 * Bitcoin price index
 */
class BitcoinPrice(): Widget()

/**
 * Stock price of a company
 * @param companyCode Company code like PTON
 */
class StockPrice(val companyCode: String): Widget()

/**
 * Wisla river level, Bulwary station
 */
class WaterLevel(val stationId: Long, val stationName: String): Widget()

/**
 * Covid new cases
 * @param countryCode Two letter countr code
 * @param timeSpan Time span od data, 3 months, one month, ... TODO
 */
class CovidStatus(val countryCode: String, val timeSpan: Any): Widget()

/**
 * Notepad
 */
class Notepad(val noteId: Long): Widget()