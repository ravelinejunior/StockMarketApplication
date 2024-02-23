package com.raveline.stockmarketapplication.domain.model

data class CompanyStockInfoModel(
    val symbol: String,
    val name: String,
    val description: String,
    val country: String,
    val industry: String,
)