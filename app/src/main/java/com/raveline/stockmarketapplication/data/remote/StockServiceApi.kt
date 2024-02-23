package com.raveline.stockmarketapplication.data.remote

import com.raveline.stockmarketapplication.data.remote.dto.CompanyStockInfoDto
import com.raveline.stockmarketapplication.util.Constants.API_KEY
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * `StockServiceApi` is an interface that defines the endpoints for the stock service API.
 * It uses Retrofit annotations to encode details about the parameters and request method.
 *
 * @GET("function=LISTING_STATUS") annotation indicates that this is a GET request to the `function=LISTING_STATUS` endpoint.
 *
 * @property getListStocks This is a suspending function that makes a GET request to the `function=LISTING_STATUS` endpoint.
 * @param apiKey This is the API key used for authentication. It defaults to `API_KEY`.
 * @return Returns a `ResponseBody` which contains the response from the server.
 */
interface StockServiceApi {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListStocks(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    /**
     * `getCompanyInfo` is a suspending function that makes a GET request to the `query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv` endpoint.
     *
     * @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv") annotation indicates that this is a GET request to the `query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv` endpoint.
     *
     * @param symbol This is the symbol of the company for which the information is requested.
     * @param apiKey This is the API key used for authentication. It defaults to `API_KEY`.
     * @return Returns a `ResponseBody` which contains the response from the server.
     */
    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY,
    ): ResponseBody

    /**
     * `getCompanyStockInfo` is a suspending function that makes a GET request to the `query?function=OVERVIEW` endpoint.
     *
     * @GET("query?function=OVERVIEW") annotation indicates that this is a GET request to the `query?function=OVERVIEW` endpoint.
     *
     * @param symbol This is the symbol of the company for which the stock information is requested.
     * @param apiKey This is the API key used for authentication. It defaults to `API_KEY`.
     * @return Returns a `CompanyStockInfoDto` which contains the company's stock information.
     */
    @GET("query?function=OVERVIEW")
    suspend fun getCompanyStockInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY,
    ): CompanyStockInfoDto
}