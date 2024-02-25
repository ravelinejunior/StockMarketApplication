package com.raveline.stockmarketapplication.data.remote.dto

import com.squareup.moshi.Json

/**
 * Data class representing the company stock information.
 *
 * This data class is used to parse the JSON response from the network request into a Kotlin object.
 * The JSON keys are specified using the @field:Json annotation.
 *
 * @property symbol The stock symbol of the company.
 * @property name The name of the company.
 * @property description The description of the company.
 * @property country The country where the company is located.
 * @property industry The industry the company belongs to.
 */
data class CompanyStockInfoDto(
    @field:Json(name = "Symbol") val symbol: String?,
    @field:Json(name = "Name") val name: String?,
    @field:Json(name = "Description") val description: String?,
    @field:Json(name = "Country") val country: String?,
    @field:Json(name = "Industry") val industry: String?,
)