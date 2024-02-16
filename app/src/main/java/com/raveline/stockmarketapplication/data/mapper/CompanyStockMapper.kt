package com.raveline.stockmarketapplication.data.mapper

import com.raveline.stockmarketapplication.data.local.entity.CompanyStocksEntity
import com.raveline.stockmarketapplication.domain.model.CompanyStocks

/**
 * Extension function to convert a [CompanyStocksEntity] to a [CompanyStocks].
 *
 * @receiver [CompanyStocksEntity] The entity to be converted.
 * @return [CompanyStocks] The converted domain model.
 */
fun CompanyStocksEntity.toCompanyStocks() = CompanyStocks(
    name = this.name,
    symbol = this.symbol,
    exchange = this.exchange
)

/**
 * Extension function to convert a [CompanyStocks] to a [CompanyStocksEntity].
 *
 * @receiver [CompanyStocks] The domain model to be converted.
 * @return [CompanyStocksEntity] The converted entity.
 */
fun CompanyStocks.toCompanyStocksEntity() = CompanyStocksEntity(
    name = this.name,
    symbol = this.symbol,
    exchange = this.exchange
)