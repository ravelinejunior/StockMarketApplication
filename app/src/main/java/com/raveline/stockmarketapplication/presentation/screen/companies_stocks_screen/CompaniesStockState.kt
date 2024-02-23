package com.raveline.stockmarketapplication.presentation.screen.companies_stocks_screen

import com.raveline.stockmarketapplication.domain.model.CompanyStocks

data class CompaniesStockState(
    val companies: List<CompanyStocks> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
