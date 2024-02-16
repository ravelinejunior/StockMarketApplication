package com.raveline.stockmarketapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raveline.stockmarketapplication.data.local.entity.CompanyStocksEntity

@Dao
interface CompanyStockDao {

    /**
     * Inserts a list of [CompanyStocksEntity] into the database.
     * If a conflict occurs, the existing row will be replaced with the new data.
     *
     * @param stockCompanies The list of [CompanyStocksEntity] to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyStocks(stockCompanies: List<CompanyStocksEntity>)

    /**
     * Deletes all rows from the Stocks_Company_Table in the database.
     */
    @Query("DELETE FROM Stocks_Company_Table")
    suspend fun deleteAllCompanyStocks()

    /**
     * Searches for [CompanyStocksEntity] in the Stocks_Company_Table where the name or symbol matches the search query.
     * The search is case-insensitive.
     *
     * @param searchQuery The search query.
     * @return A list of [CompanyStocksEntity] that match the search query.
     */
    @Query(
        """
        SELECT * FROM Stocks_Company_Table
        WHERE LOWER(name) LIKE '%' || LOWER(:searchQuery) || '%' OR
        UPPER(:searchQuery) == symbol
    """
    )
    suspend fun searchCompanyStocks(searchQuery: String): List<CompanyStocksEntity>
}
