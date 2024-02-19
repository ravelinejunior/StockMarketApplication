package com.raveline.stockmarketapplication.domain.model

data class CompaniesStockState(
    val companies: List<CompanyStocks> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
