package com.raveline.stockmarketapplication.presentation.screen.company_info_screen

import com.raveline.stockmarketapplication.domain.model.CompanyStockInfoModel
import com.raveline.stockmarketapplication.domain.model.IntraDayInfoModel

data class CompanyStateInfo(
    val intraDaysInfo: List<IntraDayInfoModel> = emptyList(),
    val infoModel: CompanyStockInfoModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)