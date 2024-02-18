package com.raveline.stockmarketapplication.domain.repository

import com.raveline.stockmarketapplication.domain.model.CompanyStocks
import com.raveline.stockmarketapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockCompanyRepository {
    suspend fun getCompanyStocks(
        fetchFromRemote: Boolean,
        searchQuery:String
    ): Flow<Resource<List<CompanyStocks>>>
}