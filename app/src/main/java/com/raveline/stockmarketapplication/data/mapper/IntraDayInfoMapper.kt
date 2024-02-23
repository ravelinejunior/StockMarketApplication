package com.raveline.stockmarketapplication.data.mapper

import com.raveline.stockmarketapplication.data.remote.dto.IntraDayInfoDto
import com.raveline.stockmarketapplication.domain.model.IntraDayInfoModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Extension function to convert IntraDayInfoDto to IntraDayInfoModel.
 *
 * This function is used to map the data transfer object (DTO) to the domain model.
 * It parses the timestamp from the DTO into a LocalDateTime object using a specific pattern.
 * The pattern used for parsing is "yyyy-MM-dd HH:mm:ss".
 * The parsed LocalDateTime and the close price from the DTO are then used to create a new instance of IntraDayInfoModel.
 *
 * @receiver IntraDayInfoDto The DTO that needs to be converted to the domain model.
 * @return IntraDayInfoModel The domain model created from the DTO.
 */
fun IntraDayInfoDto.toIntraDayInfo(): IntraDayInfoModel {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntraDayInfoModel(
        date = localDateTime,
        close = close
    )
}