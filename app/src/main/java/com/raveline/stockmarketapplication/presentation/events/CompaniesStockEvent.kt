package com.raveline.stockmarketapplication.presentation.events

sealed class CompaniesStockEvent {
    data object Refresh : CompaniesStockEvent()
    data class SearchCompaniesStockEvent(val searchQuery: String) : CompaniesStockEvent()
}