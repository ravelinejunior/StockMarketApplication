package com.raveline.stockmarketapplication.presentation.screen.companies_stocks_screen

sealed class CompaniesStockEvent {
    data object Refresh : CompaniesStockEvent()
    data class SearchCompaniesStockEvent(val searchQuery: String) : CompaniesStockEvent()
}