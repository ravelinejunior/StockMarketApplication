package com.raveline.stockmarketapplication.data.csv

import com.opencsv.CSVReader
import com.raveline.stockmarketapplication.data.mapper.toIntraDayInfo
import com.raveline.stockmarketapplication.data.remote.dto.IntraDayInfoDto
import com.raveline.stockmarketapplication.domain.model.IntraDayInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CSVParserImpl is a class that implements the CSVParser interface for IntraDayInfoModel.
 * It is responsible for parsing CSV data into a list of IntraDayInfoModel objects.
 *
 * @constructor Injects the dependencies needed by this class.
 */
@Singleton
class CSVParserIntraDayInfoImpl @Inject constructor() : CSVParser<IntraDayInfoModel> {

    /**
     * This function parses an InputStream of CSV data into a list of IntraDayInfoModel objects.
     * It reads all lines from the CSV file, drops the header line, and then maps each remaining line to a IntraDayInfoModel object.
     * If any of the required fields (timestamp, close) are missing, the line is skipped.
     *
     * @param stream The InputStream containing the CSV data.
     * @return A list of IntraDayInfoModel objects parsed from the CSV data.
     */
    override suspend fun parse(stream: InputStream): List<IntraDayInfoModel> {
        return CSVReader(InputStreamReader(stream)).use { csvReader ->
            withContext(Dispatchers.IO) {
                csvReader
                    .readAll()
                    .drop(1) // drop the header
                    .mapNotNull { column ->
                        val timestamp = column.getOrNull(0)
                        val close = column.getOrNull(4)
                        val dto = IntraDayInfoDto(
                            timestamp = timestamp ?: return@mapNotNull null,
                            close = close?.toDouble() ?: return@mapNotNull null
                        )
                        dto.toIntraDayInfo()
                    }.filter {
                        it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                    }.sortedBy {
                        it.date.hour
                    }
            }
        }
    }
}