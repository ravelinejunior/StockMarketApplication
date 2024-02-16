package com.raveline.stockmarketapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This is an Entity class for the Room database. It represents a table within the database.
 * The table name is "Stocks_Company_Table".
 *
 * @property id The primary key for the table. It is an optional integer that defaults to null.
 * @property name The name of the company. It is a non-nullable string.
 * @property symbol The stock symbol of the company. It is a non-nullable string.
 * @property exchange The stock exchange where the company's stock is listed. It is a non-nullable string.
 */
@Entity(tableName = "Stocks_Company_Table")
data class CompanyStocksEntity(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val symbol: String,
    val exchange: String,
)
