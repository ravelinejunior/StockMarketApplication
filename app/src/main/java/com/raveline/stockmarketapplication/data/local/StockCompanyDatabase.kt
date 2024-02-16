package com.raveline.stockmarketapplication.data.local

import androidx.room.Database
import com.raveline.stockmarketapplication.data.local.dao.CompanyStockDao
import com.raveline.stockmarketapplication.data.local.entity.CompanyStocksEntity

@Database(entities = [CompanyStocksEntity::class], version = 1, exportSchema = false)
abstract class StockCompanyDatabase {
    abstract fun companyStockDao(): CompanyStockDao
}