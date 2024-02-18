package com.raveline.stockmarketapplication.data.csv

import com.opencsv.CSVReader
import com.raveline.stockmarketapplication.domain.model.CompanyStocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * CSVParserImpl is a class that implements the CSVParser interface for CompanyStocks.
 * It is responsible for parsing CSV data into a list of CompanyStocks objects.
 *
 * @constructor Injects the dependencies needed by this class.
 */
class CSVParserImpl @Inject constructor() : CSVParser<CompanyStocks> {

    /**
     * This function parses an InputStream of CSV data into a list of CompanyStocks objects.
     * It reads all lines from the CSV file, drops the header line, and then maps each remaining line to a CompanyStocks object.
     * If any of the required fields (symbol, name, exchange) are missing, the line is skipped.
     *
     * @param stream The InputStream containing the CSV data.
     * @return A list of CompanyStocks objects parsed from the CSV data.
     */
    override suspend fun parse(stream: InputStream): List<CompanyStocks> {
        return CSVReader(InputStreamReader(stream)).use { csvReader ->
            withContext(Dispatchers.IO) {
                csvReader.readAll()
                    .drop(1) // drop the header
                    .mapNotNull { column ->
                        val symbol = column.getOrNull(0)
                        val name = column.getOrNull(1)
                        val exchange = column.getOrNull(2)
                        // Create a new CompanyStocks object with the parsed data.
                        // If any field is null, skip this line.
                        CompanyStocks(
                            symbol = symbol ?: return@mapNotNull null,
                            name = name ?: return@mapNotNull null,
                            exchange = exchange ?: return@mapNotNull null
                        )
                    }
            }
        }
    }
}