package com.raveline.stockmarketapplication.data.remote

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
    @GET("function=LISTING_STATUS")
    suspend fun getListStocks(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody
}